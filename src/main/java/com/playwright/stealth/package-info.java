/**
 * Playwright Stealth Java Library
 * 
 * <p>This package provides stealth techniques for Playwright-controlled browsers to make them
 * appear more like regular user browsers. It hides automation indicators and spoofs various
 * browser properties to avoid detection by anti-bot systems.</p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Hide navigator.webdriver property</li>
 *   <li>Override user agent strings to remove "HeadlessChrome" references</li>
 *   <li>Spoof WebGL vendor and renderer information</li>
 *   <li>Add realistic Chrome runtime properties</li>
 *   <li>Override navigator properties (vendor, platform, languages, etc.)</li>
 *   <li>Simulate browser plugins and MIME types</li>
 *   <li>Customize media codec support</li>
 *   <li>Fix window outer dimensions for headless browsers</li>
 * </ul>
 * 
 * <h2>Basic Usage:</h2>
 * <pre>{@code
 * import com.microsoft.playwright.*;
 * import com.playwright.stealth.Stealth;
 * 
 * try (Playwright playwright = Playwright.create()) {
 *     Browser browser = playwright.chromium().launch();
 *     Page page = browser.newPage();
 *     
 *     // Apply stealth techniques
 *     Stealth.stealth(page);
 *     
 *     // Navigate to your target website
 *     page.navigate("https://example.com");
 *     
 *     browser.close();
 * }
 * }</pre>
 * 
 * <h2>Custom Configuration:</h2>
 * <pre>{@code
 * StealthConfig config = Stealth.configBuilder()
 *     .webdriver(true)
 *     .navigatorUserAgent(true)
 *     .navUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
 *     .navPlatform("Win32")
 *     .languages(Arrays.asList("en-US", "en"))
 *     .build();
 * 
 * Stealth.stealth(page, config);
 * }</pre>
 * 
 * <p>This library is a Java port of the Python playwright_stealth library, which is based on
 * the puppeteer-extra-plugin-stealth techniques.</p>
 * 
 * @author Playwright Stealth Java
 * @version 1.0.0
 * @since 1.0.0
 */
package com.playwright.stealth;
