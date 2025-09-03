package com.playwright.stealth;

/**
 * Contains all the JavaScript stealth scripts used to make Playwright pages appear more like regular browsers.
 * These scripts are injected into pages to hide automation indicators and mimic normal browser behavior.
 */
public class StealthScripts {
    
    public static final String UTILS = """
        // Utils
        const utils = {
            stripProxyFromErrors: (handler = {}) => {
                const newHandler = {}
                const traps = Object.getOwnPropertyNames(handler)
                traps.forEach(trap => {
                    newHandler[trap] = function () {
                        try {
                            return handler[trap].apply(this, arguments || [])
                        } catch (err) {
                            throw err
                        }
                    }
                })
                return newHandler
            },
            replaceProperty: (obj, propName, descriptor) => {
                return Object.defineProperty(obj, propName, {
                    ...descriptor,
                    configurable: true
                })
            },
            preloadCache: new Map(),
            stripErrorWithAnchor: (fn, anchor) => (...args) => {
                try {
                    return fn.apply(this, args)
                } catch (error) {
                    throw error
                }
            },
            mockWithProxy: (target, handler, options) => {
                return new Proxy(target, utils.stripProxyFromErrors(handler))
            }
        }
        """;
    
    public static final String GENERATE_MAGIC_ARRAYS = """
        // Generate magic arrays for plugins
        const generateMagicArray = (dataArray, proto = MimeTypeArray.prototype, itemProto = MimeType.prototype, itemMainProp = 'type') => {
            const arr = Object.create(proto)
            const typeArray = Object.create(proto)
            
            dataArray.forEach((data, index) => {
                arr[index] = Object.create(itemProto)
                typeArray[data[itemMainProp]] = Object.create(itemProto)
                
                Object.keys(data).forEach(prop => {
                    if (prop === itemMainProp) {
                        utils.replaceProperty(arr[index], prop, { value: data[prop] })
                        utils.replaceProperty(typeArray[data[prop]], prop, { value: data[prop] })
                    } else {
                        utils.replaceProperty(arr[index], prop, { value: data[prop] })
                        utils.replaceProperty(typeArray[data[prop]], prop, { value: data[prop] })
                    }
                })
            })
            
            utils.replaceProperty(arr, 'length', { value: dataArray.length })
            utils.replaceProperty(typeArray, 'length', { value: dataArray.length })
            
            return { arr, typeArray }
        }
        """;
    
    public static final String WEBDRIVER = """
        // Hide webdriver property
        Object.defineProperty(Object.getPrototypeOf(navigator), 'webdriver', {
            set: undefined,
            enumerable: true,
            configurable: true,
            get: new Proxy(
                Object.getOwnPropertyDescriptor(Object.getPrototypeOf(navigator), 'webdriver').get,
                { apply: (target, thisArg, args) => {
                    Reflect.apply(target, thisArg, args);
                    return false;
                }}
            )
        });
        """;
    
    public static final String NAVIGATOR_USER_AGENT = """
        // Replace Headless references in default useragent
        const current_ua = navigator.userAgent;
        Object.defineProperty(Object.getPrototypeOf(navigator), 'userAgent', {
            get: () => opts.navigatorUserAgent || current_ua.replace('HeadlessChrome/', 'Chrome/')
        });
        """;
    
    public static final String NAVIGATOR_VENDOR = """
        // Override navigator.vendor
        Object.defineProperty(Object.getPrototypeOf(navigator), 'vendor', {
            get: () => opts.navigatorVendor || 'Google Inc.'
        });
        """;
    
    public static final String NAVIGATOR_PLATFORM = """
        // Override navigator.platform
        if (opts.navigatorPlatform) {
            Object.defineProperty(Object.getPrototypeOf(navigator), 'platform', {
                get: () => opts.navigatorPlatform
            });
        }
        """;
    
    public static final String NAVIGATOR_LANGUAGES = """
        // Override navigator.languages
        Object.defineProperty(Object.getPrototypeOf(navigator), 'languages', {
            get: () => opts.languages || ['en-US', 'en']
        });
        """;
    
    public static final String WEBGL_VENDOR = """
        // Override WebGL vendor and renderer
        const getParameter = WebGLRenderingContext.prototype.getParameter;
        WebGLRenderingContext.prototype.getParameter = function(parameter) {
            if (parameter === 37445) {
                return opts.webglVendor || 'Intel Inc.';
            }
            if (parameter === 37446) {
                return opts.webglRenderer || 'Intel Iris OpenGL Engine';
            }
            return getParameter.apply(this, arguments);
        };
        
        const getParameter2 = WebGL2RenderingContext.prototype.getParameter;
        WebGL2RenderingContext.prototype.getParameter = function(parameter) {
            if (parameter === 37445) {
                return opts.webglVendor || 'Intel Inc.';
            }
            if (parameter === 37446) {
                return opts.webglRenderer || 'Intel Iris OpenGL Engine';
            }
            return getParameter2.apply(this, arguments);
        };
        """;
    
    public static final String CHROME_APP = """
        // Add chrome.app
        if (!window.chrome) {
            Object.defineProperty(window, 'chrome', {
                writable: true,
                enumerable: true,
                configurable: false,
                value: {}
            })
        }
        
        if (!window.chrome.app) {
            window.chrome.app = {
                isInstalled: false,
                InstallState: {
                    DISABLED: 'disabled',
                    INSTALLED: 'installed',
                    NOT_INSTALLED: 'not_installed'
                },
                RunningState: {
                    CANNOT_RUN: 'cannot_run',
                    READY_TO_RUN: 'ready_to_run',
                    RUNNING: 'running'
                }
            }
        }
        """;
    
    public static final String CHROME_CSI = """
        // Add chrome.csi
        if (!window.chrome) {
            Object.defineProperty(window, 'chrome', {
                writable: true,
                enumerable: true,
                configurable: false,
                value: {}
            })
        }
        
        if (!window.chrome.csi) {
            window.chrome.csi = function() {}
        }
        """;
    
    public static final String CHROME_LOAD_TIMES = """
        // Add chrome.loadTimes
        if (!window.chrome) {
            Object.defineProperty(window, 'chrome', {
                writable: true,
                enumerable: true,
                configurable: false,
                value: {}
            })
        }
        
        if (!window.chrome.loadTimes) {
            window.chrome.loadTimes = function() {
                return {
                    requestTime: performance.timing.navigationStart / 1000,
                    startLoadTime: performance.timing.navigationStart / 1000,
                    commitLoadTime: performance.timing.responseStart / 1000,
                    finishDocumentLoadTime: performance.timing.domContentLoadedEventStart / 1000,
                    finishLoadTime: performance.timing.loadEventStart / 1000,
                    firstPaintTime: performance.timing.responseStart / 1000,
                    firstPaintAfterLoadTime: 0,
                    navigationType: 'Other',
                    wasFetchedViaSpdy: false,
                    wasNpnNegotiated: false,
                    npnNegotiatedProtocol: 'unknown',
                    wasAlternateProtocolAvailable: false,
                    connectionInfo: 'http/1.1'
                }
            }
        }
        """;
    
    public static final String CHROME_RUNTIME = """
        // Add chrome.runtime
        const STATIC_DATA = {
            "OnInstalledReason": {
                "CHROME_UPDATE": "chrome_update",
                "INSTALL": "install",
                "SHARED_MODULE_UPDATE": "shared_module_update",
                "UPDATE": "update"
            },
            "OnRestartRequiredReason": {
                "APP_UPDATE": "app_update",
                "OS_UPDATE": "os_update",
                "PERIODIC": "periodic"
            },
            "PlatformArch": {
                "ARM": "arm",
                "ARM64": "arm64",
                "MIPS": "mips",
                "MIPS64": "mips64",
                "X86_32": "x86-32",
                "X86_64": "x86-64"
            },
            "PlatformNaclArch": {
                "ARM": "arm",
                "MIPS": "mips",
                "MIPS64": "mips64",
                "X86_32": "x86-32",
                "X86_64": "x86-64"
            },
            "PlatformOs": {
                "ANDROID": "android",
                "CROS": "cros",
                "LINUX": "linux",
                "MAC": "mac",
                "OPENBSD": "openbsd",
                "WIN": "win"
            },
            "RequestUpdateCheckStatus": {
                "NO_UPDATE": "no_update",
                "THROTTLED": "throttled",
                "UPDATE_AVAILABLE": "update_available"
            }
        }
        
        if (!window.chrome) {
            Object.defineProperty(window, 'chrome', {
                writable: true,
                enumerable: true,
                configurable: false,
                value: {}
            })
        }
        
        const existsAlready = 'runtime' in window.chrome
        const isNotSecure = !window.location.protocol.startsWith('https')
        if (!(existsAlready || (isNotSecure && !opts.runOnInsecureOrigins))) {
            window.chrome.runtime = {
                ...STATIC_DATA,
                get id() {
                    return undefined
                },
                connect: null,
                sendMessage: null
            }
        }
        """;
    
    public static final String CHROME_HAIRLINE = """
        // Fix hairline rendering
        Object.defineProperty(HTMLDivElement.prototype, 'offsetHeight', {
            get() {
                return 1
            }
        })
        """;
    
    public static final String IFRAME_CONTENT_WINDOW = """
        // Fix iframe.contentWindow
        try {
            if (Element.prototype.attachShadow) {
                Element.prototype.attachShadow = utils.mockWithProxy(
                    Element.prototype.attachShadow,
                    {
                        apply: function(target, thisArg, argArray) {
                            return target.apply(thisArg, argArray)
                        }
                    }
                )
            }
        } catch (err) {}
        """;
    
    public static final String MEDIA_CODECS = """
        // Override media codec support
        const canPlayType = HTMLMediaElement.prototype.canPlayType
        HTMLMediaElement.prototype.canPlayType = function(type) {
            if (type === 'video/ogg; codecs="theora"') return ''
            if (type === 'video/mp4; codecs="avc1.42E01E"') return 'probably'
            if (type === 'video/webm; codecs="vp8, vorbis"') return 'probably'
            return canPlayType.apply(this, arguments)
        }
        """;
    
    public static final String NAVIGATOR_PERMISSIONS = """
        // Override navigator.permissions
        const originalQuery = window.navigator.permissions.query
        window.navigator.permissions.query = (parameters) => (
            parameters.name === 'notifications' ?
                Promise.resolve({ state: Notification.permission }) :
                originalQuery(parameters)
        )
        """;
    
    public static final String NAVIGATOR_PLUGINS = """
        // Override navigator.plugins
        const pluginData = [
            {
                name: 'Chrome PDF Plugin',
                filename: 'internal-pdf-viewer',
                description: 'Portable Document Format',
                type: 'application/x-google-chrome-pdf',
                suffixes: 'pdf',
                enabledPlugin: 'Chrome PDF Plugin'
            },
            {
                name: 'Chrome PDF Viewer',
                filename: 'mhjfbmdgcfjbbpaeojofohoefgiehjai',
                description: '',
                type: 'application/pdf',
                suffixes: 'pdf',
                enabledPlugin: 'Chrome PDF Viewer'
            },
            {
                name: 'Native Client',
                filename: 'internal-nacl-plugin',
                description: '',
                type: 'application/x-nacl',
                suffixes: '',
                enabledPlugin: 'Native Client'
            }
        ]
        
        const mimeTypeData = pluginData.map(plugin => ({
            type: plugin.type,
            suffixes: plugin.suffixes,
            description: plugin.description,
            enabledPlugin: plugin.name
        }))
        
        const { arr: plugins } = generateMagicArray(pluginData, PluginArray.prototype, Plugin.prototype, 'name')
        const { arr: mimeTypes } = generateMagicArray(mimeTypeData, MimeTypeArray.prototype, MimeType.prototype, 'type')
        
        Object.defineProperty(navigator, 'plugins', {
            get: () => plugins
        })
        
        Object.defineProperty(navigator, 'mimeTypes', {
            get: () => mimeTypes
        })
        """;
    
    public static final String OUTER_DIMENSIONS = """
        // Override window outer dimensions
        try {
            if (window.outerHeight === 0) {
                Object.defineProperty(window, 'outerHeight', {
                    get: () => window.innerHeight
                })
            }
            if (window.outerWidth === 0) {
                Object.defineProperty(window, 'outerWidth', {
                    get: () => window.innerWidth
                })
            }
        } catch (err) {}
        """;
}
