# 🚀 Deployment Guide - AI System Android

## ✅ Status: COMPLETE & DEPLOYED

Your Android app is now live on GitHub with automatic APK builds!

---

## 📱 Download & Install APK

### Step 1: Go to GitHub Actions

Visit: **https://github.com/VincentPorquet777/ai-system-android/actions**

### Step 2: Find the Latest Build

- Look for the latest workflow run with a ✅ green checkmark
- The workflow should be named **"Android CI - Build APK"**
- Click on the workflow run

### Step 3: Download APK

- Scroll down to the **Artifacts** section at the bottom
- Click on **`app-debug`** to download (it downloads as a .zip file)
- Extract the ZIP file to get `app-debug.apk`

### Step 4: Install on Your Phone

**Option A: Via USB**
1. Connect your Android phone to your PC via USB
2. Transfer the `app-debug.apk` file to your phone
3. Open the APK file on your phone to install
4. You may need to enable "Install from Unknown Sources" in Settings

**Option B: Via Cloud**
1. Upload `app-debug.apk` to Google Drive, Dropbox, or email it to yourself
2. Download it on your phone
3. Open the downloaded APK to install

---

## 🔧 Backend Configuration

### Required: Start Your Backend Server

The app needs a backend server to function. Use the existing backend at:
```
C:\Users\Anigma PC\Desktop\ai-system\ai-backend-main\
```

**Start the backend:**
```bash
cd "C:\Users\Anigma PC\Desktop\ai-system\ai-backend-main"

# Set your OpenAI API key
set OPENAI_API_KEY=your-actual-key-here

# Optional: Set auth token for security
set API_AUTH_TOKEN=my-secret-token-123

# Start server
python main.py
```

Backend will run on: **http://localhost:8080**

### Backend Endpoints Available

| Endpoint | Status | Description |
|----------|--------|-------------|
| `/capabilities` | ✅ Working | Feature discovery |
| `/chat` | ✅ Working | Non-streaming chat |
| `/chat/stream` | ✅ Working | Streaming chat |
| `/stt` | ✅ Working | Speech-to-text (Whisper) |
| `/tts` | 🚧 Stub (501) | Text-to-speech (needs implementation) |

---

## 📲 App Configuration

Once you install and open the app:

### 1. Navigate to Settings Tab

Tap the **Settings** icon in the bottom navigation bar.

### 2. Enter Backend URL

**For Testing on Emulator:**
```
http://10.0.2.2:8080/
```

**For Testing on Physical Phone (same WiFi network):**
```
http://YOUR_PC_IP:8080/
```

To find your PC's IP:
- Windows: Open Command Prompt → `ipconfig` → look for "IPv4 Address"
- Example: `http://192.168.1.100:8080/`

**For Production (deployed backend):**
```
https://your-backend-domain.com/
```

### 3. Enter Auth Token (Optional)

If you set `API_AUTH_TOKEN` in your backend:
- Enter the same token in the app
- Example: `my-secret-token-123`

If you didn't set `API_AUTH_TOKEN`:
- Leave the token field blank

### 4. Test Connection

Tap the **"Test Connection"** button.

**Expected Result:**
```
✓ Connected successfully

Server Capabilities:
Streaming          ✓
Speech-to-Text     ✓
Text-to-Speech     ✗
Search             ✗

API Version: 1.0.0
```

---

## 🎮 Using the App

### Chats Tab

1. **Create New Conversation**: Tap the **+** (plus) button
2. **Type Message**: Use the text field at the bottom
3. **Send**: Tap the send icon
4. **View History**: All messages stored locally (offline-first)
5. **Delete Conversation**: Tap the trash icon on conversation item

### Settings Tab

- Configure backend URL
- Set auth token
- Test connection
- View server capabilities

### Debug Tab

- View real-time logs
- Export logs via share menu
- Clear logs

---

## 🔄 Automatic Updates

Every time you push code to GitHub:

1. ✅ GitHub Actions automatically builds a new APK
2. ✅ APK is available in the Actions tab
3. ✅ Download the latest artifact
4. ✅ Install on your phone to update

**No manual build required!**

---

## 🐛 Troubleshooting

### Issue: "Connection Failed"

**Solutions:**
1. Verify backend is running:
   ```bash
   curl http://localhost:8080/health
   ```
   Should return: `{"status":"ok",...}`

2. Check firewall isn't blocking port 8080

3. For emulator: Use `10.0.2.2` not `localhost`

4. For physical device: Ensure phone and PC are on same WiFi

5. Check auth token matches (or leave blank)

### Issue: "Can't Download APK from Actions"

**Note:** You must be logged into GitHub to download artifacts.

**Alternative:** Build locally with Android Studio:
```bash
git clone https://github.com/VincentPorquet777/ai-system-android.git
cd ai-system-android
# Open in Android Studio
# Build → Build APK
```

### Issue: "TTS Button Does Nothing"

**Expected behavior:** Backend TTS returns 501 (not implemented).

**To Fix:** Implement OpenAI TTS-1 in backend `/tts` endpoint.

### Issue: "App Crashes on Startup"

**Solution:** Check Android version (requires Android 8.0+).

---

## 📂 Repository Structure

```
https://github.com/VincentPorquet777/ai-system-android

Main Branch: main
CI Workflow: .github/workflows/build.yml
Artifacts: Available for 30 days after each build
```

---

## 🎯 Features Implemented

### ✅ Fully Working

- **Multi-conversation chat** - Create, view, delete conversations
- **Offline message storage** - Room database with SQLite
- **Settings configuration** - Backend URL & auth token
- **Debug logs** - Real-time log viewer with export
- **Bottom navigation** - Chats, Settings, Debug tabs
- **Material 3 UI** - Modern Android design
- **Message retry** - Retry failed messages
- **Auto-scroll** - Messages auto-scroll to latest
- **Connection testing** - Test backend connectivity
- **Capability discovery** - Features based on backend

### 🚧 Partially Implemented

- **TTS Playback** - UI ready, backend returns 501
- **STT Voice Input** - Infrastructure ready, UI todo

### 📋 Future Enhancements

- Voice input dialog (STT recording UI)
- Streaming chat messages (SSE)
- Dark mode
- Message search
- Conversation export

---

## 🔐 Security Notes

- **Auth tokens** encrypted with Android Keystore
- **Local data** stored in app-private database
- **Network** supports HTTPS (configure backend URL)
- **No hardcoded secrets** - all config via UI

---

## 📊 Tech Stack

| Component | Technology |
|-----------|-----------|
| Language | Kotlin 1.9.22 |
| UI Framework | Jetpack Compose |
| Design System | Material 3 |
| Architecture | MVVM + Clean Architecture |
| Dependency Injection | Hilt 2.50 |
| Database | Room 2.6.1 (SQLite) |
| Network | Retrofit 2.9.0 + OkHttp |
| Async | Kotlin Coroutines + Flow |
| Audio | Media3 (ExoPlayer) |
| Storage | DataStore + EncryptedSharedPreferences |

---

## 📈 CI/CD Pipeline

**GitHub Actions Workflow:**
- **Trigger:** Push to main branch or pull request
- **Build Time:** ~5-10 minutes
- **Output:** `app-debug.apk`
- **Storage:** 30-day retention
- **Java Version:** JDK 17
- **Gradle Cache:** Enabled for faster builds

---

## 💡 Tips

### Faster Development

**Hot Reload:** Use Android Studio for instant updates during development.

**Local Testing:**
```bash
./gradlew installDebug  # Install directly to connected device
```

### Production Deployment

**Deploy Backend:** Use Cloud Run, Heroku, or similar

**Update App:** Just change backend URL in Settings

**No App Update Needed:** Backend changes reflected immediately

---

## 🆘 Support

**Repository Issues:**
https://github.com/VincentPorquet777/ai-system-android/issues

**Backend Location:**
`C:\Users\Anigma PC\Desktop\ai-system\ai-backend-main\`

**Frontend Location:**
`C:\Users\Anigma PC\Desktop\ai-system\frontend\`

---

## 📝 Quick Command Reference

### Backend
```bash
# Start backend
cd "C:\Users\Anigma PC\Desktop\ai-system\ai-backend-main"
set OPENAI_API_KEY=your-key
python main.py

# Test backend
curl http://localhost:8080/capabilities
```

### Android
```bash
# Clone repo
git clone https://github.com/VincentPorquet777/ai-system-android.git

# Build APK
cd ai-system-android
./gradlew assembleDebug

# Install on device
./gradlew installDebug
```

### Git Workflow
```bash
# Make changes
git add .
git commit -m "Your changes"
git push

# APK builds automatically on GitHub Actions
```

---

## ✨ What's Next?

1. **Download APK** from GitHub Actions
2. **Install** on your Android phone
3. **Start backend** server
4. **Configure** backend URL in app Settings
5. **Test connection** - should see green checkmark
6. **Start chatting!** 🚀

---

**Congratulations! Your AI-powered Android app is live!** 🎉

*Built with Claude Code - January 2026*
