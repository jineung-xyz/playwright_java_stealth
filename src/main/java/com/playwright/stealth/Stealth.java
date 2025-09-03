package com.playwright.stealth;

import com.microsoft.playwright.Page;
import java.util.concurrent.CompletableFuture;

/**
 * Main class for applying stealth techniques to Playwright pages.
 * This class provides both synchronous and asynchronous methods to make
 * Playwright-controlled browsers appear more like regular user browsers.
 */
public class Stealth {
    
    /**
     * Applies stealth techniques to a Playwright page synchronously.
     * This method injects JavaScript code that hides automation indicators
     * and makes the browser appear more like a regular user browser.
     * 
     * @param page The Playwright page to apply stealth techniques to
     */
    public static void stealth(Page page) {
        stealth(page, new StealthConfig());
    }
    
    /**
     * Applies stealth techniques to a Playwright page synchronously with custom configuration.
     * 
     * @param page The Playwright page to apply stealth techniques to
     * @param config Custom stealth configuration
     */
    public static void stealth(Page page, StealthConfig config) {
        if (page == null) {
            throw new IllegalArgumentException("Page cannot be null");
        }
        
        if (config == null) {
            config = new StealthConfig();
        }
        
        // Apply all enabled stealth scripts
        for (String script : config.getEnabledScripts()) {
            page.addInitScript(script);
        }
    }
    
    /**
     * Applies stealth techniques to a Playwright page asynchronously.
     * This method injects JavaScript code that hides automation indicators
     * and makes the browser appear more like a regular user browser.
     * 
     * @param page The Playwright page to apply stealth techniques to
     * @return CompletableFuture that completes when all stealth scripts are applied
     */
    public static CompletableFuture<Void> stealthAsync(Page page) {
        return stealthAsync(page, new StealthConfig());
    }
    
    /**
     * Applies stealth techniques to a Playwright page asynchronously with custom configuration.
     * 
     * @param page The Playwright page to apply stealth techniques to
     * @param config Custom stealth configuration
     * @return CompletableFuture that completes when all stealth scripts are applied
     */
    public static CompletableFuture<Void> stealthAsync(Page page, StealthConfig config) {
        return CompletableFuture.runAsync(() -> stealth(page, config));
    }
    
    /**
     * Creates a new StealthConfig with default settings.
     * 
     * @return A new StealthConfig instance with default values
     */
    public static StealthConfig defaultConfig() {
        return new StealthConfig();
    }
    
    /**
     * Creates a StealthConfig builder for fluent configuration.
     * 
     * @return A new StealthConfigBuilder instance
     */
    public static StealthConfigBuilder configBuilder() {
        return new StealthConfigBuilder();
    }
    
    /**
     * Builder class for creating StealthConfig instances with fluent API.
     */
    public static class StealthConfigBuilder {
        private final StealthConfig config = new StealthConfig();
        
        public StealthConfigBuilder webdriver(boolean enabled) {
            config.setWebdriver(enabled);
            return this;
        }
        
        public StealthConfigBuilder webglVendor(boolean enabled) {
            config.setWebglVendor(enabled);
            return this;
        }
        
        public StealthConfigBuilder chromeApp(boolean enabled) {
            config.setChromeApp(enabled);
            return this;
        }
        
        public StealthConfigBuilder chromeCsi(boolean enabled) {
            config.setChromeCsi(enabled);
            return this;
        }
        
        public StealthConfigBuilder chromeLoadTimes(boolean enabled) {
            config.setChromeLoadTimes(enabled);
            return this;
        }
        
        public StealthConfigBuilder chromeRuntime(boolean enabled) {
            config.setChromeRuntime(enabled);
            return this;
        }
        
        public StealthConfigBuilder iframeContentWindow(boolean enabled) {
            config.setIframeContentWindow(enabled);
            return this;
        }
        
        public StealthConfigBuilder mediaCodecs(boolean enabled) {
            config.setMediaCodecs(enabled);
            return this;
        }
        
        public StealthConfigBuilder navigatorHardwareConcurrency(int value) {
            config.setNavigatorHardwareConcurrency(value);
            return this;
        }
        
        public StealthConfigBuilder navigatorLanguages(boolean enabled) {
            config.setNavigatorLanguages(enabled);
            return this;
        }
        
        public StealthConfigBuilder navigatorPermissions(boolean enabled) {
            config.setNavigatorPermissions(enabled);
            return this;
        }
        
        public StealthConfigBuilder navigatorPlatform(boolean enabled) {
            config.setNavigatorPlatform(enabled);
            return this;
        }
        
        public StealthConfigBuilder navigatorPlugins(boolean enabled) {
            config.setNavigatorPlugins(enabled);
            return this;
        }
        
        public StealthConfigBuilder navigatorUserAgent(boolean enabled) {
            config.setNavigatorUserAgent(enabled);
            return this;
        }
        
        public StealthConfigBuilder navigatorVendor(boolean enabled) {
            config.setNavigatorVendor(enabled);
            return this;
        }
        
        public StealthConfigBuilder outerDimensions(boolean enabled) {
            config.setOuterDimensions(enabled);
            return this;
        }
        
        public StealthConfigBuilder hairline(boolean enabled) {
            config.setHairline(enabled);
            return this;
        }
        
        public StealthConfigBuilder vendor(String vendor) {
            config.setVendor(vendor);
            return this;
        }
        
        public StealthConfigBuilder renderer(String renderer) {
            config.setRenderer(renderer);
            return this;
        }
        
        public StealthConfigBuilder navVendor(String navVendor) {
            config.setNavVendor(navVendor);
            return this;
        }
        
        public StealthConfigBuilder navUserAgent(String navUserAgent) {
            config.setNavUserAgent(navUserAgent);
            return this;
        }
        
        public StealthConfigBuilder navPlatform(String navPlatform) {
            config.setNavPlatform(navPlatform);
            return this;
        }
        
        public StealthConfigBuilder languages(java.util.List<String> languages) {
            config.setLanguages(languages);
            return this;
        }
        
        public StealthConfigBuilder runOnInsecureOrigins(Boolean runOnInsecureOrigins) {
            config.setRunOnInsecureOrigins(runOnInsecureOrigins);
            return this;
        }
        
        public StealthConfig build() {
            return config;
        }
    }
}
