const { getDefaultConfig } = require('expo/metro-config');
const path = require('path');

const config = getDefaultConfig(__dirname);

// Add support for additional file extensions
config.resolver.assetExts.push(
  // Fonts
  'ttf',
  'otf',
  'woff',
  'woff2',
  // Images
  'svg',
  'png',
  'jpg',
  'jpeg',
  'gif',
  'webp',
  // Audio/Video
  'mp3',
  'mp4',
  'mov',
  'avi',
  'webm',
  // Documents
  'pdf',
  'doc',
  'docx'
);

// Add support for TypeScript and JSX
config.resolver.sourceExts.push(
  'ts',
  'tsx',
  'js',
  'jsx',
  'json',
  'mjs'
);

// Configure transformer for better performance
config.transformer = {
  ...config.transformer,
  babelTransformerPath: require.resolve('react-native-svg-transformer'),
  unstable_allowRequireContext: true,
};

// Exclude SVG from asset extensions since we're using svg-transformer
config.resolver.assetExts = config.resolver.assetExts.filter(ext => ext !== 'svg');

// Add SVG to source extensions
config.resolver.sourceExts.push('svg');

// Configure for better caching (simplified)
config.cacheVersion = '1.0';

module.exports = config;