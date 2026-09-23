# The Insurance Boss — Android App

Native Android lead-generation app for [theinsuranceboss.com](https://theinsuranceboss.com), sharing the same Convex backend as the web app.

## Stack

- Kotlin + Jetpack Compose (Material 3)
- Navigation Compose
- Retrofit + OkHttp + Gson
- DataStore (session)
- Coil (images)
- Convex HTTP REST API (`/api/app/*`)

## Branding

| Token | Value |
|-------|-------|
| Primary Yellow | `#FAC000` |
| Black | `#000000` (backgrounds) |
| Dark Gray | `#333333` (elements) |
| Light Gray | `#D9D9D9` (accents / body text) |
| Headlines / body | Bitter (Light–Black variable) |
| Labels / buttons | JetBrains Mono (Bold, uppercase) |
| Phone | 732-COVERED (268-3373) → `+17322683373` |
| Email | info@theinsuranceboss.com |

Voice: **bold · strategic · confident · authoritative**

## Build

Requirements:

- JDK 17 (not 25 — AGP/Kotlin DSL fail on Java 25)
- Android SDK platform 35 + build-tools 35.x
- Gradle 8.11.1

```powershell
$env:JAVA_HOME = 'C:\path\to\jdk-17'
$env:ANDROID_HOME = "$env:LOCALAPPDATA\Android\Sdk"
.\gradlew.bat assembleDebug   # or open in Android Studio
```

APK: `app/build/outputs/apk/debug/app-debug.apk`

### Install on emulator/device

```powershell
adb install -r app\build\outputs\apk\debug\app-debug.apk
adb shell am start -n com.theinsuranceboss.app/.MainActivity
```

## Configuration

`local.properties` (not committed):

```properties
sdk.dir=C\:\\Users\\...\\Android\\Sdk
API_BASE_URL=https://polished-rook-569.convex.site
```

`BuildConfig` fields: `API_BASE_URL`, `PHONE_DISPLAY`, `PHONE_DIAL`, `PRIVACY_URL`, `FCM_ENABLED`.

For a local Convex HTTP proxy on the host, use `http://10.0.2.2:PORT` from the emulator.

## Lead source tags

All lead mutations send a server-enforced `source` starting with `android_app_`:

| Action | Source |
|--------|--------|
| Quote | `android_app_quote` |
| Policy audit | `android_app_audit` |
| Calculator report | `android_app_calculator` |
| Book a call | `android_app_book_call` |
| Referral | `android_app_referral` |
| Wallet add policy | `android_app_wallet` |

Guests can quote/audit/calc/book. Login (same `agent_profiles` as web) unlocks policy wallet + referral codes via `app_sessions` tokens.

## Screens

Onboarding → Needs → Home → Quote / Audit / Calculator / Wallet / Book / Referral / Claims / Learn / Auth.

## REST endpoints (Convex HTTP)

Base: `https://polished-rook-569.convex.site/api/app`

- `GET /health`
- `POST /signup`, `/login`, `/refresh`, `/logout`
- `POST /quote`, `/audit`, `/calculator`, `/book-call`, `/referral`
- `GET|POST /policies`, `GET /content`
- `POST /referral/code`, `/upload-url`

## Firebase Cloud Messaging

Scaffold only. Place `google-services.json`, apply the Google Services Gradle plugin, and uncomment Firebase deps in `app/build.gradle.kts`. Set `FCM_ENABLED=true` when ready.

## E2E verified

- Debug APK built and installed on `emulator-5554`
- Quote UI → Convex `app_leads` `source=android_app_quote`
- Calculator report UI → Convex `source=android_app_calculator`
- Book a Call UI → Convex `source=android_app_book_call` (+ appointments row)
- Free Policy Audit UI (photo picker + multipart upload) → Convex `source=android_app_audit`
- REST paths also verified: `POST /calculator`, `POST /audit` (multipart)
- Gradle wrapper (`gradlew` / `gradlew.bat` + `gradle/wrapper/*`) for reproducible builds

## Repo

https://github.com/theinsuranceboss/theinsurancebossapp
