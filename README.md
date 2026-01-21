# 🤖 AI System - Android Client

A capability-driven Android client for AI-powered conversations with offline-first architecture, voice support, and feature discovery.

[![Android CI](https://github.com/VincentPorquet777/ai-system-android/actions/workflows/build.yml/badge.svg)](https://github.com/VincentPorquet777/ai-system-android/actions/workflows/build.yml)
[![Platform](https://img.shields.io/badge/Platform-Android%208.0+-green.svg)](https://android.com)
[![Language](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org)

## 📱 Features

### Core Features
- ✅ **Multi-conversation Chat** - Manage multiple AI conversations
- ✅ **Offline-first** - Room database with local message storage
- ✅ **Settings Screen** - Backend URL & auth token configuration
- ✅ **Debug Logs** - Built-in log viewer with export functionality
- ✅ **Material 3 Design** - Modern, clean UI with bottom navigation

### AI Capabilities (Backend-driven)
- 🤖 **LLM Chat** - OpenAI GPT-powered conversations
- 🎤 **Speech-to-Text** - Whisper STT for voice input (coming soon)
- 🔊 **Text-to-Speech** - Audio message playback (stubbed, backend needs implementation)
- 🔍 **Search** - Semantic search (ready for backend integration)
- 🔄 **Streaming** - Real-time message streaming support

## 🏗️ Architecture

**Pattern**: MVVM + Clean Architecture
**DI**: Hilt
**UI**: Jetpack Compose + Material 3
**Navigation**: Navigation Compose
**Storage**: Room + DataStore + EncryptedSharedPreferences
**Networking**: Retrofit + OkHttp

### Module Structure
```
frontend/
├── app/                     # Main app module with navigation
├── core/
│   ├── network/            # Retrofit API client
│   ├── storage/            # Room DB + DataStore
│   └── common/             # Shared models & utilities
└── feature/
    ├── chat/               # Chat screens & conversation list
    ├── voice/              # STT/TTS audio processing
    ├── settings/           # Backend configuration
    └── debug/              # Log viewer & diagnostics
```

## 📥 Download & Install

### Option 1: Download APK from GitHub Actions

1. Go to [Actions tab](https://github.com/VincentPorquet777/ai-system-android/actions)
2. Click on the latest successful build (green checkmark)
3. Scroll down to **Artifacts** section
4. Download `app-debug.zip`
5. Extract and install `app-debug.apk` on your Android device

### Option 2: Build Locally

**Requirements**: Android Studio Hedgehog or newer

```bash
# Clone the repo
git clone https://github.com/VincentPorquet777/ai-system-android.git
cd ai-system-android

# Open in Android Studio
# Wait for Gradle sync to complete
# Build: Build → Build APK

# Or build via command line:
./gradlew assembleDebug
# APK location: app/build/outputs/apk/debug/app-debug.apk
```

## 🚀 Quick Start

### 1. Start the Backend Server

This app requires the AI backend server. Set up your backend locally or deploy it.

```bash
# Example backend setup
cd ai-backend
export OPENAI_API_KEY=your-key-here
export API_AUTH_TOKEN=optional-token
python main.py
# Server runs on http://localhost:8080
```

### 2. Configure the App

Launch the app and tap the **Settings** tab:

**For Emulator**:
- Backend URL: `http://10.0.2.2:8080/`
- Auth Token: Enter your token (or leave blank if backend has no auth)

**For Physical Device (same network)**:
- Backend URL: `http://YOUR_PC_IP:8080/`
- Auth Token: Enter your token

Tap **"Test Connection"** to verify - you should see server capabilities.

### 3. Start Chatting

1. Tap the **Chats** tab
2. Tap the **+** button to create a new conversation
3. Type your message and tap send
4. Messages are stored locally for offline access

## 🔧 Configuration

### Required Settings

| Setting | Description | Example |
|---------|-------------|---------|
| Backend URL | AI backend server endpoint | `http://10.0.2.2:8080/` |
| Auth Token | Bearer token (optional) | `my-secret-token` |

### Backend Capabilities

The app discovers available features from the backend `/capabilities` endpoint:

```json
{
  "streaming": true,          // Real-time message streaming
  "stt": true,                // Speech-to-text (Whisper)
  "tts": false,               // Text-to-speech (not yet implemented)
  "search": false,            // Semantic search
  "model_selection": false,   // Choose LLM model
  "max_message_length": 4000,
  "api_version": "1.0.0"
}
```

Features are automatically enabled/disabled in the UI based on backend capabilities.

## 🛠️ Development

### Building

```bash
# Debug build
./gradlew assembleDebug

# Release build (requires signing config)
./gradlew assembleRelease

# Run tests
./gradlew test

# Run on connected device
./gradlew installDebug
```

### GitHub Actions CI

Every push to `main` triggers automatic APK build:

- ✅ Compiles debug APK
- ✅ Uploads as artifact (30-day retention)
- ✅ Validates Gradle wrapper
- ✅ Caches Gradle dependencies

**Download latest APK**: Actions → Latest run → Artifacts → `app-debug`

### Adding New Features

1. Check backend capabilities via `/capabilities`
2. Add UI only if capability is enabled
3. Handle 501/503 errors gracefully for unimplemented features

Example:
```kotlin
val capabilities by viewModel.capabilities.collectAsState()

if (capabilities?.tts == true) {
    // Show TTS playback button
}
```

## 🔐 Security

- **Auth tokens** stored in `EncryptedSharedPreferences` (Android Keystore)
- **Fallback** to standard preferences if encryption unavailable
- **TLS/HTTPS** ready (configure backend URL with https://)
- **No hardcoded secrets** - all config via UI

## 📱 Screens

### 1. Conversations List
- View all conversations
- Create new chats
- Delete conversations (with confirmation)
- Auto-sorted by last update time

### 2. Chat Screen
- Message bubbles (user = right, assistant = left)
- Auto-scroll to latest message
- Retry failed messages
- Voice button (if STT enabled)
- Audio playback (if TTS enabled)
- Offline message queueing

### 3. Settings
- Backend URL configuration
- Auth token management (encrypted)
- Connection testing
- Live capability display

### 4. Debug Logs
- Real-time log viewer
- Filter by level (DEBUG, INFO, WARNING, ERROR)
- Export logs via share intent
- Auto-scroll to latest

## 🐛 Troubleshooting

### Connection Failed

**Problem**: "Connection failed" when testing backend

**Solutions**:
- ✅ Verify backend is running: `curl http://localhost:8080/health`
- ✅ Use `10.0.2.2` for emulator (not `localhost`)
- ✅ Check firewall allows port 8080
- ✅ Ensure correct auth token (or blank if no auth)

### TTS Button Does Nothing

**Expected**: Backend TTS endpoint returns 501 (not implemented)

**Solution**: Backend needs to implement OpenAI TTS-1 API in `/tts` endpoint

### App Crashes on Startup

**Solution**:
1. View Logcat in Android Studio
2. Check for missing dependencies or Room migration errors
3. Try: Build → Clean Project → Rebuild Project

### Gradle Sync Failed

**Solution**:
- File → Invalidate Caches → Invalidate and Restart
- Check internet connection for dependency downloads
- Ensure JDK 17+ is installed

## 📊 Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin 1.9.22 |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM, Clean Architecture |
| DI | Hilt 2.50 |
| Database | Room 2.6.1 |
| Networking | Retrofit 2.9.0, OkHttp 4.12.0 |
| Async | Coroutines, Flow |
| Audio | Media3 (ExoPlayer), MediaRecorder |
| Storage | DataStore, EncryptedSharedPreferences |

## 🎯 Roadmap

- [ ] Voice input dialog (STT recording UI)
- [ ] Backend TTS implementation
- [ ] Streaming chat messages (SSE)
- [ ] Message search
- [ ] Dark mode
- [ ] Conversation export (markdown/PDF)
- [ ] Push notifications
- [ ] Multi-device sync

## 📝 API Contract

### Required Headers (Auto-added)

```
X-Client: android
X-App-Version: 1.0.0
X-Device: MODEL-Android-SDK_INT
X-Api-Version: 1.0.0
Authorization: Bearer {token}
```

### Endpoints Used

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/capabilities` | Feature discovery |
| POST | `/chat` | Send message (non-streaming) |
| POST | `/chat/stream` | Send message (streaming) |
| POST | `/stt` | Transcribe audio |
| POST | `/tts` | Synthesize speech (501 stub) |

## 📄 License

MIT License - See [LICENSE](LICENSE) file

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 💬 Support

- **Issues**: [GitHub Issues](https://github.com/VincentPorquet777/ai-system-android/issues)

---

**Built with ❤️ using Claude Code**

*Last updated: January 2026*
