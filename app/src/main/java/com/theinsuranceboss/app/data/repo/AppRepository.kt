package com.theinsuranceboss.app.data.repo

import android.net.Uri
import com.google.gson.Gson
import com.theinsuranceboss.app.BuildConfig
import com.theinsuranceboss.app.data.api.AddPolicyRequest
import com.theinsuranceboss.app.data.api.ApiError
import com.theinsuranceboss.app.data.api.AuthResponse
import com.theinsuranceboss.app.data.api.BookCallRequest
import com.theinsuranceboss.app.data.api.CalculatorRequest
import com.theinsuranceboss.app.data.api.CalculatorResponse
import com.theinsuranceboss.app.data.api.ContentResponse
import com.theinsuranceboss.app.data.api.LoginRequest
import com.theinsuranceboss.app.data.api.OkResponse
import com.theinsuranceboss.app.data.api.PoliciesResponse
import com.theinsuranceboss.app.data.api.QuoteRequest
import com.theinsuranceboss.app.data.api.ReferralCodeResponse
import com.theinsuranceboss.app.data.api.ReferralSubmitRequest
import com.theinsuranceboss.app.data.api.SignupRequest
import com.theinsuranceboss.app.data.api.UserDto
import com.theinsuranceboss.app.data.session.SessionStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(val message: String) : Result<Nothing>()
}

class AppRepository(private val session: SessionStore) {
    private val gson = Gson()

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    private val api = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL.trimEnd('/') + "/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(com.theinsuranceboss.app.data.api.BossApi::class.java)

    private suspend fun <T> wrap(block: suspend () -> T): Result<T> = withContext(Dispatchers.IO) {
        try {
            Result.Success(block())
        } catch (e: HttpException) {
            val body = e.response()?.errorBody()?.string()
            val msg = try {
                gson.fromJson(body, ApiError::class.java).messageOrFallback()
            } catch (_: Exception) {
                body ?: e.message() ?: "HTTP ${e.code()}"
            }
            Result.Failure(msg)
        } catch (e: Exception) {
            Result.Failure(e.message ?: "Network error")
        }
    }

    suspend fun health() = wrap { api.health() }

    suspend fun signup(username: String, email: String, password: String, fullName: String?): Result<AuthResponse> =
        wrap {
            val res = api.signup(SignupRequest(username, email, password, fullName))
            if (res.ok && res.token != null && res.user != null) {
                session.save(res.token, gson.toJson(res.user))
            }
            res
        }

    suspend fun login(login: String, password: String): Result<AuthResponse> =
        wrap {
            val res = api.login(LoginRequest(login, password))
            if (res.ok && res.token != null && res.user != null) {
                session.save(res.token, gson.toJson(res.user))
            }
            res
        }

    suspend fun refresh(): Result<UserDto?> = wrap {
        val token = session.token() ?: return@wrap null
        val res = api.refresh(mapOf("token" to token), "Bearer $token")
        res.user
    }

    suspend fun logout(): Result<Boolean> = wrap {
        val token = session.token()
        if (token != null) {
            runCatching { api.logout(mapOf("token" to token)) }
        }
        session.clear()
        true
    }

    suspend fun currentUser(): UserDto? {
        val json = session.userJson() ?: return null
        return runCatching { gson.fromJson(json, UserDto::class.java) }.getOrNull()
    }

    suspend fun submitQuote(
        name: String,
        email: String,
        phone: String?,
        coverageType: String?,
        notes: String?,
    ): Result<OkResponse> = wrap {
        api.submitQuote(
            QuoteRequest(
                name = name,
                email = email,
                phone = phone,
                coverageType = coverageType,
                notes = notes,
                token = session.token(),
            )
        )
    }

    suspend fun saveCalculator(
        income: Double,
        debts: Double,
        years: Int,
        coverageEstimate: Double,
        name: String?,
        email: String?,
        phone: String?,
    ): Result<CalculatorResponse> = wrap {
        api.saveCalculator(
            CalculatorRequest(
                income = income,
                debts = debts,
                years = years,
                coverageEstimate = coverageEstimate,
                name = name,
                email = email,
                phone = phone,
                detailsJson = """{"income":$income,"debts":$debts,"years":$years}""",
                token = session.token(),
            )
        )
    }

    suspend fun listPolicies(): Result<PoliciesResponse> = wrap {
        val token = session.token() ?: throw Exception("Unauthorized")
        api.listPolicies("Bearer $token")
    }

    suspend fun addPolicy(
        title: String,
        carrier: String?,
        policyNumber: String?,
        policyType: String?,
        premium: String?,
        renewalDate: String?,
        notes: String?,
    ): Result<OkResponse> = wrap {
        val token = session.token() ?: throw Exception("Unauthorized")
        api.addPolicy(
            AddPolicyRequest(
                token = token,
                title = title,
                carrier = carrier,
                policyNumber = policyNumber,
                policyType = policyType,
                premium = premium,
                renewalDate = renewalDate,
                notes = notes,
            )
        )
    }

    suspend fun bookCall(
        name: String,
        email: String,
        phone: String,
        date: String,
        time: String,
        topic: String?,
        notes: String?,
    ): Result<OkResponse> = wrap {
        api.bookCall(
            BookCallRequest(
                name = name,
                email = email,
                phone = phone,
                date = date,
                time = time,
                topic = topic,
                notes = notes,
                token = session.token(),
            )
        )
    }

    suspend fun submitReferral(
        code: String,
        name: String?,
        email: String?,
        phone: String?,
    ): Result<ReferralCodeResponse> = wrap {
        api.submitReferral(
            ReferralSubmitRequest(
                code = code,
                referredName = name,
                referredEmail = email,
                referredPhone = phone,
                token = session.token(),
            )
        )
    }

    suspend fun referralCode(): Result<ReferralCodeResponse> = wrap {
        val token = session.token() ?: throw Exception("Unauthorized")
        api.referralCode(mapOf("token" to token), "Bearer $token")
    }

    suspend fun content(): Result<ContentResponse> = wrap { api.content() }

    suspend fun submitAudit(
        name: String,
        email: String,
        phone: String?,
        notes: String?,
        photoUris: List<Uri>,
        resolver: android.content.ContentResolver,
        cacheDir: File,
    ): Result<OkResponse> = withContext(Dispatchers.IO) {
        try {
            val parts = photoUris.mapIndexed { index, uri ->
                val tmp = File(cacheDir, "audit_$index.jpg")
                resolver.openInputStream(uri)?.use { input ->
                    tmp.outputStream().use { output -> input.copyTo(output) }
                } ?: throw Exception("Could not read photo")
                MultipartBody.Part.createFormData(
                    "photos",
                    tmp.name,
                    tmp.asRequestBody("image/*".toMediaTypeOrNull()),
                )
            }
            val res = api.submitAudit(
                name = name.toRequestBody("text/plain".toMediaTypeOrNull()),
                email = email.toRequestBody("text/plain".toMediaTypeOrNull()),
                phone = phone?.toRequestBody("text/plain".toMediaTypeOrNull()),
                notes = notes?.toRequestBody("text/plain".toMediaTypeOrNull()),
                source = "android_app_audit".toRequestBody("text/plain".toMediaTypeOrNull()),
                token = session.token()?.toRequestBody("text/plain".toMediaTypeOrNull()),
                photos = parts,
            )
            Result.Success(res)
        } catch (e: HttpException) {
            val body = e.response()?.errorBody()?.string()
            val msg = try {
                gson.fromJson(body, ApiError::class.java).messageOrFallback()
            } catch (_: Exception) {
                body ?: e.message() ?: "HTTP ${e.code()}"
            }
            Result.Failure(msg)
        } catch (e: Exception) {
            Result.Failure(e.message ?: "Upload failed")
        }
    }

    suspend fun downloadLogo(cacheDir: File): File? = withContext(Dispatchers.IO) {
        try {
            val f = File(cacheDir, "boss_logo.png")
            if (f.exists() && f.length() > 0) return@withContext f
            val req = Request.Builder()
                .url("https://lh3.googleusercontent.com/d/1Lr3oT5chJbkjpbHTHW8f-A32Achcby6v")
                .build()
            client.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return@withContext null
                val body = resp.body ?: return@withContext null
                f.writeBytes(body.bytes())
                f
            }
        } catch (_: Exception) {
            null
        }
    }
}
