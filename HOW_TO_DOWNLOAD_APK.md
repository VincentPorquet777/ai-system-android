# 📱 How to Download Your APK from GitHub Actions

## Complete Beginner's Guide

Never used GitHub Actions before? No problem! Follow these exact steps.

---

## Step 1: Open Your Repository

1. Open your web browser
2. Go to: **https://github.com/VincentPorquet777/ai-system-android**
3. Make sure you're **logged into GitHub**

---

## Step 2: Navigate to Actions Tab

Look at the top of your repository page. You'll see several tabs:

```
< > Code    Issues    Pull requests    Actions    Projects    Wiki    Settings
```

**Click on the "Actions" tab** (4th tab from the left)

---

## Step 3: Find the Latest Workflow Run

You should now see a page titled "**All workflows**" with a list of workflow runs.

Each row shows:
- ✅ A green checkmark (successful build) or ❌ red X (failed build)
- Workflow name: "**Android CI - Build APK**"
- Commit message (e.g., "Add comprehensive deployment guide")
- Who triggered it (your username)
- When it ran (e.g., "5 minutes ago")

**Click on the FIRST row** (the most recent one with a green ✅ checkmark)

---

## Step 4: Wait for Build to Complete (if needed)

If you see:
- 🟡 **Yellow circle** = Build is currently running
  - **Wait 5-10 minutes** for it to complete
  - Refresh the page to check status

- ✅ **Green checkmark** = Build completed successfully
  - **Proceed to Step 5**

- ❌ **Red X** = Build failed
  - Click on it to see error logs
  - Usually means a configuration issue (rare in our case)

---

## Step 5: Scroll Down to Artifacts Section

After clicking on a workflow run, you'll see:

1. **Top section:** Summary, build logs, job details
2. **Middle section:** Job steps (Setup Java, Build APK, etc.)
3. **Bottom section:** "**Artifacts**" heading

**Scroll all the way to the bottom** until you see:

```
Artifacts
Produced during runtime
─────────────────────────────────
Name              Size
📦 app-debug      ~15 MB    [Download button]
```

---

## Step 6: Download the APK

1. **Click the download icon or "app-debug" link**
2. A file named `app-debug.zip` will download to your computer
3. **Find the downloaded file** (usually in your Downloads folder)

---

## Step 7: Extract the ZIP File

The download is a ZIP file containing the APK.

**On Windows:**
1. Right-click on `app-debug.zip`
2. Select "**Extract All...**"
3. Choose a destination folder (or use default)
4. Click "**Extract**"

**You should now have:** `app-debug.apk`

---

## Step 8: Transfer APK to Your Phone

### Method A: USB Cable (Easiest)

1. **Connect your phone** to PC via USB cable
2. **Enable File Transfer mode** on your phone
   - When connected, swipe down notification bar
   - Tap "USB for file transfer" or similar
   - Select "File Transfer" or "Transfer files"
3. **Open your phone** in File Explorer (appears as a device)
4. **Copy `app-debug.apk`** to your phone's `Downloads` folder
5. **Disconnect phone**

### Method B: Cloud Storage

1. **Upload APK** to Google Drive / Dropbox / OneDrive
2. **Open the cloud app** on your phone
3. **Download the APK** file
4. It will be in your phone's `Downloads` folder

### Method C: Email

1. **Email yourself** the `app-debug.apk` file
2. **Open the email** on your phone
3. **Download the attachment**

---

## Step 9: Install APK on Your Phone

1. **Open your phone's file manager** (Files, My Files, or Downloads app)
2. **Navigate to Downloads** folder
3. **Tap on `app-debug.apk`**

You may see a warning:

```
⚠️ "For your security, your phone is not allowed to install
    unknown apps from this source"
```

**This is normal!** Android blocks app installs from outside the Play Store by default.

### Enable Installation:

**Option A: During install**
1. Tap "**Settings**" in the warning dialog
2. Toggle "**Allow from this source**" to ON
3. Go back and tap the APK again

**Option B: Before install**
1. Open phone **Settings**
2. Go to **Security** or **Privacy**
3. Find "**Install unknown apps**" or "**Unknown sources**"
4. Select your **file manager** (e.g., "Files")
5. Toggle "**Allow from this source**" to ON
6. Go back and install the APK

---

## Step 10: Open the App

1. After installation completes, tap "**Open**"
2. Or find "**AI System**" in your app drawer
3. The app will launch!

---

## 🎯 Quick Visual Guide

```
Browser
└─> https://github.com/VincentPorquet777/ai-system-android
    └─> Click "Actions" tab
        └─> Click latest green ✅ workflow run
            └─> Scroll down to "Artifacts"
                └─> Click "app-debug" to download
                    └─> Extract ZIP file
                        └─> Transfer APK to phone
                            └─> Install APK
                                └─> Open app!
```

---

## 🔍 What You're Looking For

### On GitHub Actions Page

```
┌─────────────────────────────────────────┐
│ All workflows                           │
├─────────────────────────────────────────┤
│ ✅ Android CI - Build APK          ← CLICK THIS
│    Add comprehensive deployment guide   │
│    #3 by VincentPorquet777             │
│    5 minutes ago                        │
├─────────────────────────────────────────┤
│ ✅ Android CI - Build APK               │
│    Update README with correct username  │
│    #2 by VincentPorquet777             │
│    10 minutes ago                       │
└─────────────────────────────────────────┘
```

### On Workflow Run Page (at bottom)

```
┌─────────────────────────────────────────┐
│ Artifacts                               │
│ Produced during runtime                 │
├─────────────────────────────────────────┤
│ 📦 app-debug        15.2 MB   [⬇]  ← DOWNLOAD THIS
└─────────────────────────────────────────┘
```

---

## ❓ Troubleshooting

### Issue: "I don't see the Actions tab"

**Solution:** Make sure you're logged into GitHub with your account (VincentPorquet777)

### Issue: "No workflow runs appear"

**Check:**
1. Is the Actions tab empty?
2. Go to repository Settings → Actions → General
3. Ensure "Allow all actions" is selected
4. Go back to Actions tab and check again

The workflows should have already run since we pushed code!

### Issue: "Workflow is still running (yellow circle)"

**Solution:** Wait 5-10 minutes. GitHub Actions needs time to:
1. Set up build environment
2. Download dependencies
3. Compile Kotlin code
4. Build APK
5. Upload artifact

Refresh the page to check status.

### Issue: "Workflow failed (red X)"

**Click on the failed run** to see error logs. Common issues:
- Gradle version mismatch (unlikely - we configured correctly)
- Syntax error in code (unlikely - code was tested)
- GitHub Actions quota exceeded (unlikely for new accounts)

### Issue: "I can't find the APK after extracting"

**Location:** Same folder as the ZIP file, look for `app-debug.apk`

**Check:** Make sure you fully extracted the ZIP (some extract in place, some create a new folder)

### Issue: "Phone won't install the APK"

**Solution:** You must enable "Install from Unknown Sources"
1. Settings → Security → Install unknown apps
2. Select your file manager
3. Toggle ON

This is a security feature. Apps outside the Play Store require explicit permission.

---

## 🎉 Success!

Once installed, you should see:

```
[📱 Phone Screen]
┌─────────────────────┐
│  AI System  🤖      │ ← Your app icon
└─────────────────────┘
```

**Open it and configure your backend URL in Settings!**

---

## 🔄 Future Updates

**When you want to update the app:**

1. Push new code to GitHub: `git push`
2. GitHub Actions builds new APK automatically (5-10 min)
3. Go to Actions tab
4. Download latest artifact
5. Install new APK (will overwrite old version)

**No need to uninstall first!** Android will update the app in place.

---

## 📞 Still Stuck?

If you're having trouble:

1. **Check if workflow ran:**
   - Go to: https://github.com/VincentPorquet777/ai-system-android/actions
   - Should see green checkmarks

2. **Wait for build:**
   - First build can take 10-15 minutes
   - Subsequent builds are faster (~5 min) due to caching

3. **Verify artifact exists:**
   - Click workflow run
   - Scroll to bottom
   - "Artifacts" section should show `app-debug`

4. **Alternative: Build Locally**
   - If GitHub Actions isn't working, you can build with Android Studio
   - See README.md for instructions

---

**Happy building! 🚀**

*Your APK is generated fresh every time you push code to GitHub!*
