package com.theinsuranceboss.app.data.api

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

data class ApiError(val ok: Boolean? = null, val error: String? = null, val message: String? = null) {
    fun messageOrFallback(): String = error ?: message ?: "Request failed"
}

data class HealthResponse(val ok: Boolean, val service: String? = null, val time: Long? = null)

data class UserDto(
    val id: String? = null,
    val username: String? = null,
    val email: String? = null,
    @SerializedName("full_name") val fullName: String? = null,
    @SerializedName("agency_name") val agencyName: String? = null,
    @SerializedName("is_admin") val isAdmin: Boolean? = null,
    val tier: String? = null,
    @SerializedName("avatar_url") val avatarUrl: String? = null,
)

data class AuthResponse(
    val ok: Boolean,
    val token: String? = null,
    val user: UserDto? = null,
    val error: String? = null,
)

data class RefreshResponse(val ok: Boolean, val user: UserDto? = null, val error: String? = null)

data class OkResponse(val ok: Boolean, val error: String? = null, val id: String? = null, val source: String? = null)

data class HealthEnvelope(val ok: Boolean, val error: String? = null)

data class QuoteRequest(
    val name: String,
    val email: String,
    val phone: String? = null,
    val coverageType: String? = null,
    val notes: String? = null,
    val source: String = "android_app_quote",
    val token: String? = null,
)

data class CalculatorRequest(
    val income: Double,
    val debts: Double,
    val years: Int,
    val coverageEstimate: Double,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val detailsJson: String? = null,
    val source: String = "android_app_calculator",
    val token: String? = null,
)

data class CalculatorResponse(
    val ok: Boolean,
    val id: String? = null,
    val coverageEstimate: Double? = null,
    val source: String? = null,
    val error: String? = null,
)

data class PolicyDto(
    @SerializedName("_id") val id: String,
    val title: String,
    val carrier: String? = null,
    @SerializedName("policyNumber") val policyNumber: String? = null,
    @SerializedName("policyType") val policyType: String? = null,
    val premium: String? = null,
    val renewalDate: String? = null,
    val notes: String? = null,
    val source: String? = null,
    @SerializedName("created_at") val createdAt: Long? = null,
)

data class PoliciesResponse(val ok: Boolean, val policies: List<PolicyDto>? = null, val error: String? = null)

data class AddPolicyRequest(
    val token: String,
    val title: String,
    val carrier: String? = null,
    val policyNumber: String? = null,
    val policyType: String? = null,
    val premium: String? = null,
    val renewalDate: String? = null,
    val notes: String? = null,
    val source: String = "android_app_wallet",
)

data class BookCallRequest(
    val name: String,
    val email: String,
    val phone: String,
    val date: String,
    val time: String,
    val topic: String? = null,
    val notes: String? = null,
    val source: String = "android_app_book_call",
    val token: String? = null,
)

data class ReferralSubmitRequest(
    val code: String,
    val referredName: String? = null,
    val referredEmail: String? = null,
    val referredPhone: String? = null,
    val referrerName: String? = null,
    val source: String = "android_app_referral",
    val token: String? = null,
)

data class ReferralCodeResponse(val ok: Boolean, val code: String? = null, val rewardsEnabled: Boolean? = null, val error: String? = null)

data class ContentItem(
    @SerializedName("_id") val id: String? = null,
    val title: String,
    val body: String,
    val category: String? = null,
    val icon: String? = null,
    val order: Int? = null,
    val published: Boolean? = null,
)

data class ContentResponse(val ok: Boolean, val items: List<ContentItem>? = null, val error: String? = null)

data class UploadUrlResponse(val ok: Boolean, val uploadUrl: String? = null, val error: String? = null)

data class SignupRequest(
    val username: String,
    val email: String,
    val password: String,
    @SerializedName("fullName") val fullName: String? = null,
    @SerializedName("agencyName") val agencyName: String? = null,
)

data class LoginRequest(val login: String, val password: String)

data class ChatMessageDto(val role: String, val text: String)

data class ChatRequest(
    val message: String,
    val history: List<ChatMessageDto> = emptyList(),
    val userName: String? = null,
    val userEmail: String? = null,
)

data class ChatResponse(val ok: Boolean, val reply: String? = null, val error: String? = null)

data class NewsArticleDto(
    val id: String? = null,
    val title: String? = null,
    val excerpt: String? = null,
    val body: String? = null,
    val content: String? = null,
    val categoryId: String? = null,
    val category: String? = null,
    val imageUrl: String? = null,
    val publishedAt: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    val date: String? = null,
    val author: String? = null,
)

data class NewsCategoryDto(
    val id: String? = null,
    val name: String? = null,
)

data class NewsResponse(
    val ok: Boolean,
    val articles: List<NewsArticleDto>? = null,
    val categories: List<NewsCategoryDto>? = null,
    val error: String? = null,
)

data class AdminLeadsRequest(val password: String, val source: String? = null)

data class LeadDto(
    @SerializedName("_id") val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val source: String? = null,
    val kind: String? = null,
    val status: String? = null,
    val notes: String? = null,
    val details: String? = null,
    @SerializedName("created_at") val createdAt: Long? = null,
)

data class AdminLeadsResponse(val ok: Boolean, val leads: List<LeadDto>? = null, val error: String? = null)

data class AgentRequest(
    val name: String,
    val email: String,
    val phone: String? = null,
    val notes: String? = null,
    val token: String? = null,
)

interface BossApi {
    @GET("api/app/health")
    suspend fun health(): HealthResponse

    @POST("api/app/signup")
    suspend fun signup(@Body body: SignupRequest): AuthResponse

    @POST("api/app/login")
    suspend fun login(@Body body: LoginRequest): AuthResponse

    @POST("api/app/refresh")
    suspend fun refresh(
        @Body body: Map<String, String>,
        @Header("Authorization") bearer: String? = null,
    ): RefreshResponse

    @POST("api/app/logout")
    suspend fun logout(@Body body: Map<String, String>): HealthEnvelope

    @POST("api/app/quote")
    suspend fun submitQuote(@Body body: QuoteRequest): OkResponse

    @POST("api/app/calculator")
    suspend fun saveCalculator(@Body body: CalculatorRequest): CalculatorResponse

    @GET("api/app/policies")
    suspend fun listPolicies(@Header("Authorization") bearer: String): PoliciesResponse

    @POST("api/app/policies")
    suspend fun addPolicy(@Body body: AddPolicyRequest): OkResponse

    @POST("api/app/book-call")
    suspend fun bookCall(@Body body: BookCallRequest): OkResponse

    @POST("api/app/referral")
    suspend fun submitReferral(@Body body: ReferralSubmitRequest): ReferralCodeResponse

    @POST("api/app/referral/code")
    suspend fun referralCode(
        @Body body: Map<String, String>,
        @Header("Authorization") bearer: String? = null,
    ): ReferralCodeResponse

    @GET("api/app/content")
    suspend fun content(): ContentResponse

    @POST("api/app/upload-url")
    suspend fun uploadUrl(): UploadUrlResponse

    @Multipart
    @POST("api/app/audit")
    suspend fun submitAudit(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody?,
        @Part("notes") notes: RequestBody?,
        @Part("source") source: RequestBody,
        @Part("token") token: RequestBody?,
        @Part photos: List<MultipartBody.Part>,
    ): OkResponse

    @POST("api/app/chat")
    suspend fun chat(@Body body: ChatRequest): ChatResponse

    @GET("api/app/news")
    suspend fun news(): NewsResponse

    @POST("api/app/admin/leads")
    suspend fun adminLeads(@Body body: AdminLeadsRequest): AdminLeadsResponse

    @POST("api/app/agent-request")
    suspend fun agentRequest(@Body body: AgentRequest): OkResponse
}
