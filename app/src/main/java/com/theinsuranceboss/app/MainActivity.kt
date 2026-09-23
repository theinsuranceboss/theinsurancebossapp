package com.theinsuranceboss.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.theinsuranceboss.app.ui.nav.Routes
import com.theinsuranceboss.app.ui.screens.AuthScreen
import com.theinsuranceboss.app.ui.screens.AdminScreen
import com.theinsuranceboss.app.ui.screens.AgentsScreen
import com.theinsuranceboss.app.ui.screens.AuditScreen
import com.theinsuranceboss.app.ui.screens.BookCallScreen
import com.theinsuranceboss.app.ui.screens.CalculatorScreen
import com.theinsuranceboss.app.ui.screens.ChatScreen
import com.theinsuranceboss.app.ui.screens.ClaimsScreen
import com.theinsuranceboss.app.ui.screens.HomeScreen
import com.theinsuranceboss.app.ui.screens.LearnScreen
import com.theinsuranceboss.app.ui.screens.NeedsScreen
import com.theinsuranceboss.app.ui.screens.NewsScreen
import com.theinsuranceboss.app.ui.screens.OnboardingScreen
import com.theinsuranceboss.app.ui.screens.QuoteScreen
import com.theinsuranceboss.app.ui.screens.ReferralScreen
import com.theinsuranceboss.app.ui.screens.SuccessScreen
import com.theinsuranceboss.app.ui.screens.WalletScreen
import com.theinsuranceboss.app.ui.theme.BossTheme
import com.theinsuranceboss.app.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val notifPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            notifPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        setContent {
            BossTheme {
                val vm: MainViewModel = viewModel()
                BossNav(vm)
            }
        }
    }
}

@androidx.compose.runtime.Composable
private fun BossNav(vm: MainViewModel) {
    val nav = rememberNavController()
    val user by vm.user.collectAsState()
    val health by vm.health.collectAsState()
    val quote by vm.quote.collectAsState()
    val audit by vm.audit.collectAsState()
    val calculator by vm.calculator.collectAsState()
    val policies by vm.policies.collectAsState()
    val book by vm.book.collectAsState()
    val referral by vm.referral.collectAsState()
    val referralSubmit by vm.referralSubmit.collectAsState()
    val content by vm.content.collectAsState()
    val auth by vm.auth.collectAsState()
    val addPolicy by vm.addPolicy.collectAsState()
    val chatItems by vm.chatItems.collectAsState()
    val chatThinking by vm.chatThinking.collectAsState()
    val chatError by vm.chatError.collectAsState()
    val news by vm.news.collectAsState()
    val adminLeads by vm.adminLeads.collectAsState()
    val agentRequest by vm.agentRequest.collectAsState()

    var onboardDone by remember { mutableStateOf(false) }

    LaunchedEffect(auth.data) {
        if (auth.data == true) {
            vm.resetAuth()
            nav.navigate(Routes.HOME) {
                popUpTo(Routes.AUTH) { inclusive = true }
            }
        }
    }

    LaunchedEffect(quote.data) {
        if (quote.data != null) {
            nav.navigate(Routes.QUOTE_DONE)
        }
    }

    LaunchedEffect(audit.data) {
        if (audit.data != null) {
            nav.navigate(Routes.AUDIT_DONE)
        }
    }

    LaunchedEffect(book.data) {
        if (book.data != null) {
            nav.navigate(Routes.BOOK_DONE)
        }
    }

    LaunchedEffect(user) {
        if (user != null) {
            vm.loadPolicies()
            vm.loadReferralCode()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        containerColor = Color.Black,
        contentColor = Color.White,
    ) { _ ->
        NavHost(
            navController = nav,
            startDestination = if (onboardDone) Routes.HOME else Routes.ONBOARDING,
            modifier = Modifier.fillMaxSize().background(Color.Black),
        ) {
            composable(Routes.ONBOARDING) {
                OnboardingScreen(
                    onDone = {
                        onboardDone = true
                        nav.navigate(Routes.NEEDS) {
                            popUpTo(Routes.ONBOARDING) { inclusive = true }
                        }
                    },
                )
            }
            composable(Routes.NEEDS) {
                NeedsScreen(
                    onContinue = {
                        onboardDone = true
                        nav.navigate(Routes.HOME) {
                            popUpTo(Routes.NEEDS) { inclusive = true }
                        }
                    },
                    onSkip = {
                        onboardDone = true
                        nav.navigate(Routes.HOME) {
                            popUpTo(Routes.NEEDS) { inclusive = true }
                        }
                    },
                )
            }
            composable(Routes.HOME) {
                HomeScreen(
                    userName = user?.fullName ?: user?.username,
                    healthOk = health.data,
                    onNav = { route -> nav.navigate(route) },
                )
            }
            composable(Routes.QUOTE) {
                QuoteScreen(
                    loading = quote.loading,
                    error = quote.error,
                    onBack = { nav.popBackStack() },
                    onSubmit = { name, email, phone, coverage, notes ->
                        vm.submitQuote(name, email, phone, coverage, notes)
                    },
                )
            }
            composable(Routes.QUOTE_DONE) {
                SuccessScreen(
                    section = "Quote request",
                    title = "You're on the list",
                    body = "A licensed agent will reach out about your ${quote.data ?: "android_app_quote"} request. Availability varies by state.",
                    sourceTag = quote.data ?: "android_app_quote",
                    primaryText = "Back to home",
                    onPrimary = {
                        vm.resetQuote()
                        nav.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    },
                    secondaryText = "Get another quote",
                    onSecondary = {
                        vm.resetQuote()
                        nav.navigate(Routes.QUOTE)
                    },
                )
            }
            composable(Routes.AUDIT) {
                AuditScreen(
                    loading = audit.loading,
                    error = audit.error,
                    onBack = { nav.popBackStack() },
                    onSubmit = { name, email, phone, notes, photos ->
                        vm.submitAudit(name, email, phone, notes, photos)
                    },
                )
            }
            composable(Routes.AUDIT_DONE) {
                SuccessScreen(
                    section = "Free policy audit",
                    title = "Audit received",
                    body = "Upload the source ${audit.data ?: "android_app_audit"} is confirmed. An agent will review your declarations page and follow up.",
                    sourceTag = audit.data ?: "android_app_audit",
                    primaryText = "Back to home",
                    onPrimary = {
                        vm.resetAudit()
                        nav.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    },
                )
            }
            composable(Routes.CALCULATOR) {
                CalculatorScreen(
                    savedCoverage = calculator.data,
                    loading = calculator.loading,
                    error = calculator.error,
                    onBack = { nav.popBackStack() },
                    onRun = { income, debts, years -> vm.runCalculator(income, debts, years) },
                    onSaveReport = { name, email, phone, coverage, income, debts, years ->
                        vm.saveCalculatorReport(name, email, phone, coverage, income, debts, years)
                    },
                    onReset = { vm.resetCalculator() },
                )
            }
            composable(Routes.WALLET) {
                WalletScreen(
                    policiesState = policies,
                    addState = addPolicy,
                    isLogged = user != null,
                    onBack = { nav.popBackStack() },
                    onLogin = { nav.navigate(Routes.AUTH) },
                    onRefresh = { vm.loadPolicies() },
                    onAdd = { title, carrier, number, type, premium ->
                        vm.addPolicy(title, carrier, number, type, premium, null, null)
                    },
                    onResetAdd = { vm.resetAddPolicy() },
                )
            }
            composable(Routes.BOOK) {
                BookCallScreen(
                    loading = book.loading,
                    error = book.error,
                    onBack = { nav.popBackStack() },
                    onSubmit = { name, email, phone, date, time, topic, notes ->
                        vm.bookCall(name, email, phone, date, time, topic, notes)
                    },
                )
            }
            composable(Routes.BOOK_DONE) {
                SuccessScreen(
                    section = "Appointment",
                    title = "Call booked",
                    body = "Your request (source ${book.data ?: "android_app_book_call"}) was received. We'll confirm by text or email.",
                    sourceTag = "android_app_book_call",
                    primaryText = "Back to home",
                    onPrimary = {
                        vm.resetBook()
                        nav.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    },
                )
            }
            composable(Routes.REFERRAL) {
                ReferralScreen(
                    codeState = referral,
                    submitState = referralSubmit,
                    isLogged = user != null,
                    onBack = { nav.popBackStack() },
                    onLoadCode = { vm.loadReferralCode() },
                    onLogin = { nav.navigate(Routes.AUTH) },
                    onSubmit = { code, name, email, phone ->
                        vm.submitReferral(code, name, email, phone)
                    },
                    onResetSubmit = { vm.resetReferralSubmit() },
                )
            }
            composable(Routes.CLAIMS) {
                ClaimsScreen(onBack = { nav.popBackStack() })
            }
            composable(Routes.LEARN) {
                LearnScreen(
                    contentState = content,
                    onBack = { nav.popBackStack() },
                    onRetry = { vm.loadContent() },
                )
            }
            composable(Routes.AUTH) {
                AuthScreen(
                    authState = auth,
                    userName = user?.fullName ?: user?.username,
                    onBack = { nav.popBackStack() },
                    onLogin = { login, password -> vm.login(login, password) },
                    onSignup = { username, email, password, fullName ->
                        vm.signup(username, email, password, fullName)
                    },
                    onReset = { vm.resetAuth() },
                    onLogout = { vm.logout() },
                    onDone = {
                        nav.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    },
                )
            }
            composable(Routes.CHAT) {
                LaunchedEffect(Unit) {
                    vm.setChatUser(user?.fullName ?: user?.username, user?.email)
                }
                ChatScreen(
                    chatItems = chatItems,
                    thinking = chatThinking,
                    error = chatError,
                    onBack = { nav.popBackStack() },
                    onSend = { msg -> vm.sendChat(msg) },
                    onReset = { vm.resetChat() },
                    onUserReady = { name, email -> vm.setChatUser(name, email) },
                )
            }
            composable(Routes.NEWS) {
                LaunchedEffect(Unit) { vm.loadNews() }
                NewsScreen(
                    state = news,
                    onBack = { nav.popBackStack() },
                    onRetry = { vm.loadNews() },
                )
            }
            composable(Routes.AGENTS) {
                AgentsScreen(
                    requestState = agentRequest,
                    onBack = { nav.popBackStack() },
                    onSubmit = { name, email, phone, notes ->
                        vm.submitAgentRequest(name, email, phone, notes)
                    },
                    onReset = { vm.resetAgentRequest() },
                    onOpenLogin = { nav.navigate(Routes.AUTH) },
                )
            }
            composable(Routes.ADMIN) {
                AdminScreen(
                    leadsState = adminLeads,
                    onBack = { nav.popBackStack() },
                    onLoad = { password -> vm.loadAdminLeads(password) },
                    onReset = { vm.resetAdminLeads() },
                )
            }
        }
    }
}
