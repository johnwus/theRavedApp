module.exports = function (api) {
  api.cache(true);
  
  return {
    presets: [
      ['babel-preset-expo', { jsxImportSource: 'nativewind' }],
    ],
    plugins: [
      // NativeWind support
      'nativewind/babel',
      
      // React Native Reanimated (must be last)
      'react-native-reanimated/plugin',
      
      // Module resolver for absolute imports
      [
        'module-resolver',
        {
          root: ['./'],
          alias: {
            '@': './client/src',
            '@components': './client/src/components',
            '@screens': './client/src/screens',
            '@navigation': './client/src/navigation',
            '@services': './client/src/services',
            '@store': './client/src/store',
            '@utils': './client/src/utils',
            '@types': './client/src/types',
            '@assets': './client/src/assets',
            '@hooks': './client/src/hooks',
            '@constants': './client/src/constants',
          },
          extensions: [
            '.ios.ts',
            '.android.ts',
            '.native.ts',
            '.ts',
            '.ios.tsx',
            '.android.tsx',
            '.native.tsx',
            '.tsx',
            '.ios.js',
            '.android.js',
            '.native.js',
            '.js',
            '.jsx',
            '.json',
          ],
        },
      ],
    ],
    env: {
      production: {
        plugins: [
          'react-native-paper/babel',
          'transform-remove-console',
        ],
      },
    },
  };
};
