# Playwright Java Stealth

A Java port of the popular [playwright_stealth](https://github.com/AtuboDad/playwright_stealth) Python library. This library makes Playwright-controlled browsers appear more like regular user browsers by hiding automation indicators and spoofing browser features.

## Features

- **Hide WebDriver Property**: Removes the `navigator.webdriver` property that indicates automation
- **User Agent Spoofing**: Replaces "HeadlessChrome" with "Chrome" in user agent strings
- **WebGL Vendor/Renderer Override**: Customizes WebGL vendor and renderer information
- **Chrome Runtime Simulation**: Adds realistic `chrome.runtime` properties
- **Navigator Properties**: Overrides various navigator properties (vendor, platform, languages, etc.)
- **Plugin Simulation**: Adds realistic browser plugins and MIME types
- **Media Codec Support**: Customizes media codec support detection
- **Window Dimensions**: Fixes outer window dimensions for headless browsers

## Installation

### Gradle

Add the following to your `build.gradle`:

```gradle
dependencies {
    implementation 'com.microsoft.playwright:playwright:1.40.0'
    implementation 'com.fasterxml.jackson.core:jackson-core:2.15.2'
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.15.2'
}
```

### Maven

Add the following to your `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>com.microsoft.playwright</groupId>
        <artifactId>playwright</artifactId>
        <version>1.40.0</version>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-core</artifactId>
        <version>2.15.2</version>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.15.2</version>
    </dependency>
</dependencies>
```

## Usage

### Basic Usage

```java
import com.microsoft.playwright.*;
import com.playwright.stealth.Stealth;

public class Example {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();
            
            // Apply stealth techniques
            Stealth.stealth(page);
            
            // Navigate to your target website
            page.navigate("https://bot.sannysoft.com/");
            
            // Take a screenshot to verify stealth is working
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("result.png")));
            
            browser.close();
        }
    }
}
```

### Custom Configuration

```java
import com.playwright.stealth.Stealth;
import com.playwright.stealth.StealthConfig;
import java.util.Arrays;

// Create custom configuration
StealthConfig config = new StealthConfig();
config.setNavUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
config.setNavPlatform("Win32");
config.setLanguages(Arrays.asList("en-US", "en", "es"));
config.setVendor("NVIDIA Corporation");
config.setRenderer("NVIDIA GeForce GTX 1080");

// Apply stealth with custom configuration
Stealth.stealth(page, config);
```

### Builder Pattern

```java
import com.playwright.stealth.Stealth;

StealthConfig config = Stealth.configBuilder()
    .webdriver(true)
    .navigatorUserAgent(true)
    .navigatorVendor(true)
    .webglVendor(true)
    .navUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36")
    .navPlatform("MacIntel")
    .languages(Arrays.asList("en-US", "en"))
    .vendor("Intel Inc.")
    .renderer("Intel Iris Pro OpenGL Engine")
    .build();

Stealth.stealth(page, config);
```

### Asynchronous Usage

```java
import java.util.concurrent.CompletableFuture;

// Apply stealth asynchronously
CompletableFuture<Void> stealthFuture = Stealth.stealthAsync(page);

// Wait for completion
stealthFuture.join();

// Or with custom configuration
CompletableFuture<Void> customStealthFuture = Stealth.stealthAsync(page, config);
customStealthFuture.join();
```

## Configuration Options

The `StealthConfig` class provides the following configuration options:

### Script Enable/Disable Flags

- `webdriver` (default: true) - Hide navigator.webdriver property
- `webglVendor` (default: true) - Override WebGL vendor/renderer
- `chromeApp` (default: true) - Add chrome.app properties
- `chromeCsi` (default: true) - Add chrome.csi function
- `chromeLoadTimes` (default: true) - Add chrome.loadTimes function
- `chromeRuntime` (default: true) - Add chrome.runtime properties
- `iframeContentWindow` (default: true) - Fix iframe.contentWindow
- `mediaCodecs` (default: true) - Override media codec support
- `navigatorLanguages` (default: true) - Override navigator.languages
- `navigatorPermissions` (default: true) - Override navigator.permissions
- `navigatorPlatform` (default: true) - Override navigator.platform
- `navigatorPlugins` (default: true) - Override navigator.plugins
- `navigatorUserAgent` (default: true) - Override navigator.userAgent
- `navigatorVendor` (default: true) - Override navigator.vendor
- `outerDimensions` (default: true) - Fix window outer dimensions
- `hairline` (default: true) - Fix hairline rendering

### Configuration Values

- `vendor` (default: "Intel Inc.") - WebGL vendor string
- `renderer` (default: "Intel Iris OpenGL Engine") - WebGL renderer string
- `navVendor` (default: "Google Inc.") - Navigator vendor string
- `navUserAgent` (default: null) - Custom user agent string
- `navPlatform` (default: null) - Custom platform string
- `languages` (default: ["en-US", "en"]) - Supported languages
- `runOnInsecureOrigins` (default: null) - Run chrome.runtime on insecure origins

## Testing Bot Detection

You can test if the stealth techniques are working by visiting these websites:

- [bot.sannysoft.com](https://bot.sannysoft.com/) - Comprehensive bot detection tests
- [intoli.com/blog/not-possible-to-block-chrome-headless](https://intoli.com/blog/not-possible-to-block-chrome-headless/test.html) - Chrome headless detection
- [arh.antoinevastel.com/bots/areyouheadless](https://arh.antoinevastel.com/bots/areyouheadless) - Headless browser detection

## Examples

The library includes several example classes:

- `BasicExample.java` - Simple usage demonstration
- `AdvancedExample.java` - Custom configuration and async usage examples

Run the examples:

```bash
./gradlew run -PmainClass=com.playwright.stealth.examples.BasicExample
./gradlew run -PmainClass=com.playwright.stealth.examples.AdvancedExample
```

## How It Works

This library works by injecting JavaScript code into pages before they load. The JavaScript code:

1. **Hides automation indicators** like `navigator.webdriver`
2. **Spoofs browser properties** to match real browsers
3. **Adds missing browser APIs** that are expected in real browsers
4. **Overrides detection methods** commonly used by anti-bot systems

The stealth techniques are based on the original [puppeteer-extra-plugin-stealth](https://github.com/berstend/puppeteer-extra/tree/master/packages/puppeteer-extra-plugin-stealth) and have been adapted for Playwright Java.

## Limitations

- **Not 100% undetectable**: Advanced bot detection systems may still detect automation
- **Requires maintenance**: As detection methods evolve, the stealth techniques need updates
- **Performance impact**: Injecting JavaScript code adds some overhead
- **Browser-specific**: Some techniques work better with certain browsers

## Contributing

Contributions are welcome! Please feel free to submit issues and pull requests.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Original Python [playwright_stealth](https://github.com/AtuboDad/playwright_stealth) library
- [puppeteer-extra-plugin-stealth](https://github.com/berstend/puppeteer-extra/tree/master/packages/puppeteer-extra-plugin-stealth) for the stealth techniques
- [Microsoft Playwright](https://playwright.dev/) for the excellent browser automation framework
