package com.playwright.stealth.examples;

import com.microsoft.playwright.*;
import com.playwright.stealth.Stealth;

/**
 * Basic example demonstrating how to use the Playwright Stealth library.
 * This example shows how to apply stealth techniques to make a browser
 * appear more like a regular user browser.
 */
public class BasicExample {
    
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            // Launch browser in headless mode for stability
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(true)
            );
            
            try {
                // Create a new page
                Page page = browser.newPage();
                
                // Apply stealth techniques to the page
                Stealth.stealth(page);
                
                // Test basic stealth functionality
                page.navigate("data:text/html,<html><body><h1>Stealth Test</h1></body></html>");
                
                // Verify that webdriver property is hidden
                Object webdriverValue = page.evaluate("navigator.webdriver");
                System.out.println("navigator.webdriver: " + webdriverValue);
                
                // Check user agent
                String userAgent = (String) page.evaluate("navigator.userAgent");
                System.out.println("User Agent: " + userAgent);
                
                // Check vendor
                String vendor = (String) page.evaluate("navigator.vendor");
                System.out.println("Navigator Vendor: " + vendor);
                
                // Test with a simple HTTP endpoint
                try {
                    page.navigate("https://httpbin.org/user-agent", new Page.NavigateOptions().setTimeout(10000));
                    System.out.println("Successfully navigated to httpbin.org");
                    
                    // Take a screenshot
                    page.screenshot(new Page.ScreenshotOptions().setPath(java.nio.file.Paths.get("stealth-example.png")));
                    System.out.println("Screenshot saved as stealth-example.png");
                } catch (Exception e) {
                    System.out.println("Network navigation failed (expected in some environments): " + e.getMessage());
                }
                
                System.out.println("✅ Stealth techniques applied successfully!");
                System.out.println("✅ navigator.webdriver is hidden: " + (!(Boolean) webdriverValue));
                System.out.println("✅ User agent cleaned: " + !userAgent.contains("HeadlessChrome"));
                
            } finally {
                browser.close();
            }
        } catch (Exception e) {
            System.err.println("Error running basic example: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
