package com.playwright.stealth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Configuration class for Playwright stealth functionality.
 * Contains all the stealth strategies and their configuration options.
 */
public class StealthConfig {
    
    // Script enable/disable flags
    private boolean webdriver = true;
    private boolean webglVendor = true;
    private boolean chromeApp = true;
    private boolean chromeCsi = true;
    private boolean chromeLoadTimes = true;
    private boolean chromeRuntime = true;
    private boolean iframeContentWindow = true;
    private boolean mediaCodecs = true;
    private int navigatorHardwareConcurrency = 4;
    private boolean navigatorLanguages = true;
    private boolean navigatorPermissions = true;
    private boolean navigatorPlatform = true;
    private boolean navigatorPlugins = true;
    private boolean navigatorUserAgent = true;
    private boolean navigatorVendor = true;
    private boolean outerDimensions = true;
    private boolean hairline = true;
    
    // Configuration options
    private String vendor = "Intel Inc.";
    private String renderer = "Intel Iris OpenGL Engine";
    private String navVendor = "Google Inc.";
    private String navUserAgent = null;
    private String navPlatform = null;
    private List<String> languages = Arrays.asList("en-US", "en");
    private Boolean runOnInsecureOrigins = null;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public StealthConfig() {}
    
    // Getters and setters
    public boolean isWebdriver() { return webdriver; }
    public void setWebdriver(boolean webdriver) { this.webdriver = webdriver; }
    
    public boolean isWebglVendor() { return webglVendor; }
    public void setWebglVendor(boolean webglVendor) { this.webglVendor = webglVendor; }
    
    public boolean isChromeApp() { return chromeApp; }
    public void setChromeApp(boolean chromeApp) { this.chromeApp = chromeApp; }
    
    public boolean isChromeCsi() { return chromeCsi; }
    public void setChromeCsi(boolean chromeCsi) { this.chromeCsi = chromeCsi; }
    
    public boolean isChromeLoadTimes() { return chromeLoadTimes; }
    public void setChromeLoadTimes(boolean chromeLoadTimes) { this.chromeLoadTimes = chromeLoadTimes; }
    
    public boolean isChromeRuntime() { return chromeRuntime; }
    public void setChromeRuntime(boolean chromeRuntime) { this.chromeRuntime = chromeRuntime; }
    
    public boolean isIframeContentWindow() { return iframeContentWindow; }
    public void setIframeContentWindow(boolean iframeContentWindow) { this.iframeContentWindow = iframeContentWindow; }
    
    public boolean isMediaCodecs() { return mediaCodecs; }
    public void setMediaCodecs(boolean mediaCodecs) { this.mediaCodecs = mediaCodecs; }
    
    public int getNavigatorHardwareConcurrency() { return navigatorHardwareConcurrency; }
    public void setNavigatorHardwareConcurrency(int navigatorHardwareConcurrency) { this.navigatorHardwareConcurrency = navigatorHardwareConcurrency; }
    
    public boolean isNavigatorLanguages() { return navigatorLanguages; }
    public void setNavigatorLanguages(boolean navigatorLanguages) { this.navigatorLanguages = navigatorLanguages; }
    
    public boolean isNavigatorPermissions() { return navigatorPermissions; }
    public void setNavigatorPermissions(boolean navigatorPermissions) { this.navigatorPermissions = navigatorPermissions; }
    
    public boolean isNavigatorPlatform() { return navigatorPlatform; }
    public void setNavigatorPlatform(boolean navigatorPlatform) { this.navigatorPlatform = navigatorPlatform; }
    
    public boolean isNavigatorPlugins() { return navigatorPlugins; }
    public void setNavigatorPlugins(boolean navigatorPlugins) { this.navigatorPlugins = navigatorPlugins; }
    
    public boolean isNavigatorUserAgent() { return navigatorUserAgent; }
    public void setNavigatorUserAgent(boolean navigatorUserAgent) { this.navigatorUserAgent = navigatorUserAgent; }
    
    public boolean isNavigatorVendor() { return navigatorVendor; }
    public void setNavigatorVendor(boolean navigatorVendor) { this.navigatorVendor = navigatorVendor; }
    
    public boolean isOuterDimensions() { return outerDimensions; }
    public void setOuterDimensions(boolean outerDimensions) { this.outerDimensions = outerDimensions; }
    
    public boolean isHairline() { return hairline; }
    public void setHairline(boolean hairline) { this.hairline = hairline; }
    
    public String getVendor() { return vendor; }
    public void setVendor(String vendor) { this.vendor = vendor; }
    
    public String getRenderer() { return renderer; }
    public void setRenderer(String renderer) { this.renderer = renderer; }
    
    public String getNavVendor() { return navVendor; }
    public void setNavVendor(String navVendor) { this.navVendor = navVendor; }
    
    public String getNavUserAgent() { return navUserAgent; }
    public void setNavUserAgent(String navUserAgent) { this.navUserAgent = navUserAgent; }
    
    public String getNavPlatform() { return navPlatform; }
    public void setNavPlatform(String navPlatform) { this.navPlatform = navPlatform; }
    
    public List<String> getLanguages() { return languages; }
    public void setLanguages(List<String> languages) { this.languages = languages; }
    
    public Boolean getRunOnInsecureOrigins() { return runOnInsecureOrigins; }
    public void setRunOnInsecureOrigins(Boolean runOnInsecureOrigins) { this.runOnInsecureOrigins = runOnInsecureOrigins; }
    
    /**
     * Generates the list of enabled JavaScript scripts to be injected into the page.
     * @return List of JavaScript code strings
     */
    public List<String> getEnabledScripts() {
        List<String> scripts = new ArrayList<>();
        
        // Create options object for JavaScript
        StealthOptions opts = new StealthOptions();
        opts.webglVendor = this.vendor;
        opts.webglRenderer = this.renderer;
        opts.navigatorVendor = this.navVendor;
        opts.navigatorPlatform = this.navPlatform;
        opts.navigatorUserAgent = this.navUserAgent;
        opts.languages = this.languages;
        opts.runOnInsecureOrigins = this.runOnInsecureOrigins;
        
        try {
            String optsJson = objectMapper.writeValueAsString(opts);
            scripts.add("const opts = " + optsJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize stealth options", e);
        }
        
        // Add utility scripts first
        scripts.add(StealthScripts.UTILS);
        scripts.add(StealthScripts.GENERATE_MAGIC_ARRAYS);
        
        // Add enabled scripts
        if (chromeApp) scripts.add(StealthScripts.CHROME_APP);
        if (chromeCsi) scripts.add(StealthScripts.CHROME_CSI);
        if (hairline) scripts.add(StealthScripts.CHROME_HAIRLINE);
        if (chromeLoadTimes) scripts.add(StealthScripts.CHROME_LOAD_TIMES);
        if (chromeRuntime) scripts.add(StealthScripts.CHROME_RUNTIME);
        if (iframeContentWindow) scripts.add(StealthScripts.IFRAME_CONTENT_WINDOW);
        if (mediaCodecs) scripts.add(StealthScripts.MEDIA_CODECS);
        if (navigatorLanguages) scripts.add(StealthScripts.NAVIGATOR_LANGUAGES);
        if (navigatorPermissions) scripts.add(StealthScripts.NAVIGATOR_PERMISSIONS);
        if (navigatorPlatform) scripts.add(StealthScripts.NAVIGATOR_PLATFORM);
        if (navigatorPlugins) scripts.add(StealthScripts.NAVIGATOR_PLUGINS);
        if (navigatorUserAgent) scripts.add(StealthScripts.NAVIGATOR_USER_AGENT);
        if (navigatorVendor) scripts.add(StealthScripts.NAVIGATOR_VENDOR);
        if (webdriver) scripts.add(StealthScripts.WEBDRIVER);
        if (outerDimensions) scripts.add(StealthScripts.OUTER_DIMENSIONS);
        if (webglVendor) scripts.add(StealthScripts.WEBGL_VENDOR);
        
        return scripts;
    }
    
    /**
     * Internal class for JSON serialization of options
     */
    private static class StealthOptions {
        public String webglVendor;
        public String webglRenderer;
        public String navigatorVendor;
        public String navigatorPlatform;
        public String navigatorUserAgent;
        public List<String> languages;
        public Boolean runOnInsecureOrigins;
    }
}
