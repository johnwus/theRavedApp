/** @type {import('@babel/core').TransformOptions} */
module.exports = function (api) {
  api.cache(true)
  return {
    presets: ["babel-preset-expo"],
    plugins: [
      // Module resolver for absolute imports
      [
        "module-resolver",
        {
          root: ["./src/app"],
          alias: {
            "@": "./src/app",
            "@config": "./src/app/config",
            "@components": "./src/app/components",
            "@screens": "./src/app/screens",
            "@navigation": "./src/app/navigation",
            "@services": "./src/app/services",
            "@styles": "./src/app/styles",
            "@store": "./src/app/store",
            "@utils": "./src/app/utils",
            "@types": "./src/app/types",
            "@assets": "./src/app/assets",
            "@hooks": "./src/app/hooks",
            "@constants": "./src/app/constants",
          },
          extensions: [
            ".ios.ts",
            ".android.ts",
            ".native.ts",
            ".ts",
            ".ios.tsx",
            ".android.tsx",
            ".native.tsx",
            ".tsx",
            ".ios.js",
            ".android.js",
            ".native.js",
            ".js",
            ".jsx",
            ".json",
          ],
        },
      ],

      // React Native Reanimated (must be last)
      "react-native-reanimated/plugin",
    ],
    env: {
      production: {
        plugins: ["transform-remove-console"],
      },
    },
  }
}
