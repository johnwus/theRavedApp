const { getDefaultConfig } = require('expo/metro-config');
const path = require('path');

const config = getDefaultConfig(__dirname);

// Enable support for monorepo structure
config.watchFolders = [
  path.resolve(__dirname, 'client'),
  path.resolve(__dirname, 'shared'),
];

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

// Configure for better caching
config.cacheStores = [
  {
    name: 'filesystem',
    options: {
      cacheDirectory: path.resolve(__dirname, 'node_modules/.cache/metro'),
    },
  },
];

// Enable symlinks support for monorepo
config.resolver.unstable_enableSymlinks = true;

module.exports = config;
