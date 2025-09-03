package com.playwright.stealth.examples;

import com.microsoft.playwright.*;
import com.playwright.stealth.Stealth;
import com.playwright.stealth.StealthConfig;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * Advanced example demonstrating custom configuration and async usage
 * of the Playwright Stealth library.
 */
public class AdvancedExample {
    
    public static void main(String[] args) {
        // Example 1: Custom configuration
        customConfigurationExample();
        
        // Example 2: Async usage
        asyncExample();
        
        // Example 3: Builder pattern
        builderPatternExample();
    }
    
    /**
     * Demonstrates how to use custom stealth configuration.
     */
    public static void customConfigurationExample() {
        System.out.println("Running custom configuration example...");
        
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(true)
            );
            
            Page page = browser.newPage();
            
            // Create custom stealth configuration
            StealthConfig config = new StealthConfig();
            config.setNavUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            config.setNavPlatform("Win32");
            config.setLanguages(Arrays.asList("en-US", "en", "es"));
            config.setVendor("NVIDIA Corporation");
            config.setRenderer("NVIDIA GeForce GTX 1080");
            
            // Apply stealth with custom configuration
            Stealth.stealth(page, config);
            
            // Navigate and test
            page.navigate("https://httpbin.org/user-agent");
            System.out.println("Custom user agent applied successfully");
            
            browser.close();
        }
    }
    
    /**
     * Demonstrates asynchronous usage of stealth techniques.
     */
    public static void asyncExample() {
        System.out.println("Running async example...");
        
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(true)
            );
            
            Page page = browser.newPage();
            
            // Apply stealth asynchronously
            CompletableFuture<Void> stealthFuture = Stealth.stealthAsync(page);
            
            // Wait for stealth to be applied
            stealthFuture.join();
            
            // Navigate and test
            page.navigate("https://httpbin.org/headers");
            System.out.println("Async stealth application completed");
            
            browser.close();
        }
    }
    
    /**
     * Demonstrates using the builder pattern for configuration.
     */
    public static void builderPatternExample() {
        System.out.println("Running builder pattern example...");
        
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(true)
            );
            
            Page page = browser.newPage();
            
            // Use builder pattern for configuration
            StealthConfig config = Stealth.configBuilder()
                .webdriver(true)
                .navigatorUserAgent(true)
                .navigatorVendor(true)
                .webglVendor(true)
                .chromeRuntime(true)
                .navUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36")
                .navPlatform("MacIntel")
                .languages(Arrays.asList("en-US", "en"))
                .vendor("Intel Inc.")
                .renderer("Intel Iris Pro OpenGL Engine")
                .build();
            
            // Apply stealth with builder configuration
            Stealth.stealth(page, config);
            
            // Navigate and test
            page.navigate("https://httpbin.org/headers");
            System.out.println("Builder pattern configuration applied successfully");
            
            browser.close();
        }
    }
}
