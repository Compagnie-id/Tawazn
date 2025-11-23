# Desktop Notification Icon

## Icon Requirements

Place a notification icon file named `ic_notification.png` in this directory.

### Specifications

- **Format:** PNG with transparency
- **Size:** 256x256 pixels (recommended)
- **Color:** Use app branding colors (Indigo/Purple from theme)
- **Background:** Transparent
- **Style:** Should match the app's liquid glass design aesthetic

### Recommended Icon

Create an icon featuring:
- A bell or notification symbol
- Optional: Balance/scale motif (representing "Tawazn" - balance)
- Clean, modern design
- Visible at small sizes (system tray notifications)

### Design Tools

You can create this icon using:
- **Figma** - Free, web-based
- **Canva** - Easy templates
- **Adobe Illustrator** - Professional
- **Inkscape** - Free, open-source

### Quick Start

1. Create or download a 256x256px PNG icon
2. Name it `ic_notification.png`
3. Place it in this directory (`composeApp/src/jvmMain/resources/`)
4. The app will automatically use it for desktop notifications

### Temporary Solution

Until you create a custom icon, the system will use a default notification icon.
The app will still function normally without a custom icon.

---

**Note:** This icon will appear in:
- Windows system tray notifications
- macOS notification center
- Linux desktop notifications
