/* eslint-env node */
// Learn more https://docs.expo.io/guides/customizing-metro
const { getDefaultConfig } = require("expo/metro-config")

/** @type {import('expo/metro-config').MetroConfig} */
const config = getDefaultConfig(__dirname)

// Add support for additional file extensions
config.resolver.assetExts.push(
  // Fonts
  "ttf",
  "otf",
  "woff",
  "woff2",
  // Images
  "svg",
  "png",
  "jpg",
  "jpeg",
  "gif",
  "webp",
  // Audio/Video
  "mp3",
  "mp4",
  "mov",
  "avi",
  "webm",
  // Documents
  "pdf",
  "doc",
  "docx",
)

// Add support for TypeScript and JSX
config.resolver.sourceExts.push("ts", "tsx", "js", "jsx", "json", "mjs")

config.transformer.getTransformOptions = async () => ({
  transform: {
    // Inline requires are very useful for deferring loading of large dependencies/components.
    // For example, we use it in app.tsx to conditionally load Reactotron.
    // However, this comes with some gotchas.
    // Read more here: https://reactnative.dev/docs/optimizing-javascript-loading
    // And here: https://github.com/expo/expo/issues/27279#issuecomment-1971610698
    inlineRequires: true,
    ...config.transformer,
    babelTransformerPath: require.resolve("react-native-svg-transformer"),
    unstable_allowRequireContext: true,
  },
})

// Exclude SVG from asset extensions since we're using svg-transformer
config.resolver.assetExts = config.resolver.assetExts.filter((ext) => ext !== "svg")

// Add SVG to source extensions
config.resolver.sourceExts.push("svg")

// Configure for better caching (simplified)
config.cacheVersion = "1.0"

// This is a temporary fix that helps fixing an issue with axios/apisauce.
// See the following issues in Github for more details:
// https://github.com/infinitered/apisauce/issues/331
// https://github.com/axios/axios/issues/6899
// The solution was taken from the following issue:
// https://github.com/facebook/metro/issues/1272
config.resolver.unstable_conditionNames = ["require", "default", "browser"]

// This helps support certain popular third-party libraries
// such as Firebase that use the extension cjs.
config.resolver.sourceExts.push("cjs")

// Fix for React 19 and Expo 53 compatibility
config.resolver.platforms = ["ios", "android", "native", "web"]

module.exports = config
