package com.theinsuranceboss.app.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.theinsuranceboss.app.BossApp
import com.theinsuranceboss.app.data.api.ContentItem
import com.theinsuranceboss.app.data.api.PolicyDto
import com.theinsuranceboss.app.data.api.UserDto
import com.theinsuranceboss.app.data.repo.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState<T>(
    val loading: Boolean = false,
    val data: T? = null,
    val error: String? = null,
) {
    val isSuccess: Boolean get() = !loading && error == null && data != null
}

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = (app as BossApp).repository
    private val session = (app as BossApp).session

    private val _user = MutableStateFlow<UserDto?>(null)
    val user: StateFlow<UserDto?> = _user.asStateFlow()

    private val _health = MutableStateFlow<UiState<Boolean>>(UiState())
    val health: StateFlow<UiState<Boolean>> = _health.asStateFlow()

    private val _quote = MutableStateFlow<UiState<String>>(UiState())
    val quote: StateFlow<UiState<String>> = _quote.asStateFlow()

    private val _audit = MutableStateFlow<UiState<String>>(UiState())
    val audit: StateFlow<UiState<String>> = _audit.asStateFlow()

    private val _calculator = MutableStateFlow<UiState<Double>>(UiState())
    val calculator: StateFlow<UiState<Double>> = _calculator.asStateFlow()

    private val _policies = MutableStateFlow<UiState<List<PolicyDto>>>(UiState())
    val policies: StateFlow<UiState<List<PolicyDto>>> = _policies.asStateFlow()

    private val _book = MutableStateFlow<UiState<String>>(UiState())
    val book: StateFlow<UiState<String>> = _book.asStateFlow()

    private val _referral = MutableStateFlow<UiState<String>>(UiState())
    val referral: StateFlow<UiState<String>> = _referral.asStateFlow()

    private val _referralSubmit = MutableStateFlow<UiState<Boolean>>(UiState())
    val referralSubmit: StateFlow<UiState<Boolean>> = _referralSubmit.asStateFlow()

    private val _content = MutableStateFlow<UiState<List<ContentItem>>>(UiState())
    val content: StateFlow<UiState<List<ContentItem>>> = _content.asStateFlow()

    private val _auth = MutableStateFlow<UiState<Boolean>>(UiState())
    val auth: StateFlow<UiState<Boolean>> = _auth.asStateFlow()

    private val _addPolicy = MutableStateFlow<UiState<Boolean>>(UiState())
    val addPolicy: StateFlow<UiState<Boolean>> = _addPolicy.asStateFlow()

    init {
        viewModelScope.launch {
            _user.value = repo.currentUser()
            checkHealth()
            loadContent()
        }
    }

    fun checkHealth() {
        viewModelScope.launch {
            _health.update { UiState(loading = true) }
            when (val r = repo.health()) {
                is Result.Success -> _health.update { UiState(data = r.data.ok) }
                is Result.Failure -> _health.update { UiState(error = r.message) }
            }
        }
    }

    fun signup(username: String, email: String, password: String, fullName: String?) {
        viewModelScope.launch {
            _auth.update { UiState(loading = true) }
            when (val r = repo.signup(username, email, password, fullName)) {
                is Result.Success -> {
                    _user.value = r.data.user
                    _auth.update { UiState(data = true) }
                }
                is Result.Failure -> _auth.update { UiState(error = r.message) }
            }
        }
    }

    fun login(login: String, password: String) {
        viewModelScope.launch {
            _auth.update { UiState(loading = true) }
            when (val r = repo.login(login, password)) {
                is Result.Success -> {
                    _user.value = r.data.user
                    _auth.update { UiState(data = true) }
                }
                is Result.Failure -> _auth.update { UiState(error = r.message) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
            _user.value = null
        }
    }

    fun resetAuth() = _auth.update { UiState() }

    fun submitQuote(name: String, email: String, phone: String?, coverageType: String?, notes: String?) {
        viewModelScope.launch {
            _quote.update { UiState(loading = true) }
            when (val r = repo.submitQuote(name, email, phone, coverageType, notes)) {
                is Result.Success -> _quote.update { UiState(data = r.data.source ?: "android_app_quote") }
                is Result.Failure -> _quote.update { UiState(error = r.message) }
            }
        }
    }

    fun resetQuote() = _quote.update { UiState() }

    fun submitAudit(
        name: String,
        email: String,
        phone: String?,
        notes: String?,
        photos: List<Uri>,
    ) {
        viewModelScope.launch {
            _audit.update { UiState(loading = true) }
            val app = getApplication<BossApp>()
            when (
                val r = repo.submitAudit(
                    name = name,
                    email = email,
                    phone = phone,
                    notes = notes,
                    photoUris = photos,
                    resolver = app.contentResolver,
                    cacheDir = app.cacheDir,
                )
            ) {
                is Result.Success -> _audit.update { UiState(data = r.data.source ?: "android_app_audit") }
                is Result.Failure -> _audit.update { UiState(error = r.message) }
            }
        }
    }

    fun resetAudit() = _audit.update { UiState() }

    fun runCalculator(income: Double, debts: Double, years: Int) {
        val estimate = ((income * 0.7) + debts) * years
        viewModelScope.launch {
            _calculator.update { UiState(loading = true, data = null) }
            _calculator.update { UiState(data = estimate) }
        }
    }

    fun saveCalculatorReport(name: String, email: String, phone: String?, coverage: Double, income: Double, debts: Double, years: Int) {
        viewModelScope.launch {
            _calculator.update { it.copy(loading = true, error = null) }
            when (val r = repo.saveCalculator(income, debts, years, coverage, name, email, phone)) {
                is Result.Success -> _calculator.update { UiState(data = r.data.coverageEstimate ?: coverage) }
                is Result.Failure -> _calculator.update { UiState(error = r.message, data = coverage) }
            }
        }
    }

    fun resetCalculator() = _calculator.update { UiState() }

    fun loadPolicies() {
        viewModelScope.launch {
            _policies.update { UiState(loading = true) }
            when (val r = repo.listPolicies()) {
                is Result.Success -> _policies.update { UiState(data = r.data.policies ?: emptyList()) }
                is Result.Failure -> _policies.update { UiState(error = r.message) }
            }
        }
    }

    fun addPolicy(
        title: String,
        carrier: String?,
        policyNumber: String?,
        policyType: String?,
        premium: String?,
        renewalDate: String?,
        notes: String?,
    ) {
        viewModelScope.launch {
            _addPolicy.update { UiState(loading = true) }
            when (val r = repo.addPolicy(title, carrier, policyNumber, policyType, premium, renewalDate, notes)) {
                is Result.Success -> {
                    _addPolicy.update { UiState(data = true) }
                    loadPolicies()
                }
                is Result.Failure -> _addPolicy.update { UiState(error = r.message) }
            }
        }
    }

    fun resetAddPolicy() = _addPolicy.update { UiState() }

    fun bookCall(name: String, email: String, phone: String, date: String, time: String, topic: String?, notes: String?) {
        viewModelScope.launch {
            _book.update { UiState(loading = true) }
            when (val r = repo.bookCall(name, email, phone, date, time, topic, notes)) {
                is Result.Success -> _book.update { UiState(data = r.data.id ?: "ok") }
                is Result.Failure -> _book.update { UiState(error = r.message) }
            }
        }
    }

    fun resetBook() = _book.update { UiState() }

    fun loadReferralCode() {
        viewModelScope.launch {
            _referral.update { UiState(loading = true) }
            when (val r = repo.referralCode()) {
                is Result.Success -> _referral.update { UiState(data = r.data.code) }
                is Result.Failure -> _referral.update { UiState(error = r.message) }
            }
        }
    }

    fun submitReferral(code: String, name: String?, email: String?, phone: String?) {
        viewModelScope.launch {
            _referralSubmit.update { UiState(loading = true) }
            when (val r = repo.submitReferral(code, name, email, phone)) {
                is Result.Success -> _referralSubmit.update { UiState(data = true) }
                is Result.Failure -> _referralSubmit.update { UiState(error = r.message) }
            }
        }
    }

    fun resetReferralSubmit() = _referralSubmit.update { UiState() }

    fun loadContent() {
        viewModelScope.launch {
            _content.update { it.copy(loading = true, error = null) }
            when (val r = repo.content()) {
                is Result.Success -> _content.update { UiState(data = r.data.items ?: emptyList()) }
                is Result.Failure -> _content.update { UiState(error = r.message) }
            }
        }
    }

    data class ChatItem(val role: String, val text: String)

    private val _chatItems = MutableStateFlow<List<ChatItem>>(emptyList())
    val chatItems: StateFlow<List<ChatItem>> = _chatItems.asStateFlow()

    private val _chatThinking = MutableStateFlow(false)
    val chatThinking: StateFlow<Boolean> = _chatThinking.asStateFlow()

    private val _chatError = MutableStateFlow<String?>(null)
    val chatError: StateFlow<String?> = _chatError.asStateFlow()

    private val _news = MutableStateFlow<UiState<com.theinsuranceboss.app.data.api.NewsResponse>>(UiState())
    val news: StateFlow<UiState<com.theinsuranceboss.app.data.api.NewsResponse>> = _news.asStateFlow()

    private val _adminLeads = MutableStateFlow<UiState<List<com.theinsuranceboss.app.data.api.LeadDto>>>(UiState())
    val adminLeads: StateFlow<UiState<List<com.theinsuranceboss.app.data.api.LeadDto>>> = _adminLeads.asStateFlow()

    private val _agentRequest = MutableStateFlow<UiState<Boolean>>(UiState())
    val agentRequest: StateFlow<UiState<Boolean>> = _agentRequest.asStateFlow()

    private var chatName: String? = null
    private var chatEmail: String? = null

    fun setChatUser(name: String?, email: String?) {
        if (!name.isNullOrBlank()) chatName = name
        if (!email.isNullOrBlank()) chatEmail = email
    }

    fun resetChat() {
        _chatItems.value = emptyList()
        _chatThinking.value = false
        _chatError.value = null
    }

    fun sendChat(message: String) {
        val text = message.trim()
        if (text.isEmpty() || _chatThinking.value) return
        val history = _chatItems.value.map { com.theinsuranceboss.app.data.api.ChatMessageDto(it.role, it.text) }
        _chatItems.update { it + ChatItem("user", text) }
        _chatThinking.value = true
        _chatError.value = null
        viewModelScope.launch {
            when (val r = repo.chat(text, history, chatName, chatEmail)) {
                is Result.Success -> {
                    val reply = r.data.reply ?: "I'm having a little trouble connecting. Please try again or call us at 732-COVERED!"
                    _chatItems.update { it + ChatItem("bot", reply) }
                }
                is Result.Failure -> _chatError.value = r.message
            }
            _chatThinking.value = false
        }
    }

    fun loadNews() {
        viewModelScope.launch {
            _news.update { UiState(loading = true) }
            when (val r = repo.news()) {
                is Result.Success -> _news.update { UiState(data = r.data) }
                is Result.Failure -> _news.update { UiState(error = r.message) }
            }
        }
    }

    fun loadAdminLeads(password: String) {
        viewModelScope.launch {
            _adminLeads.update { UiState(loading = true) }
            when (val r = repo.adminLeads(password)) {
                is Result.Success -> _adminLeads.update { UiState(data = r.data.leads ?: emptyList()) }
                is Result.Failure -> _adminLeads.update { UiState(error = r.message) }
            }
        }
    }

    fun resetAdminLeads() = _adminLeads.update { UiState() }

    fun submitAgentRequest(name: String, email: String, phone: String?, notes: String?) {
        viewModelScope.launch {
            _agentRequest.update { UiState(loading = true) }
            when (val r = repo.agentRequest(name, email, phone, notes)) {
                is Result.Success -> _agentRequest.update { UiState(data = true) }
                is Result.Failure -> _agentRequest.update { UiState(error = r.message) }
            }
        }
    }

    fun resetAgentRequest() = _agentRequest.update { UiState() }
}
