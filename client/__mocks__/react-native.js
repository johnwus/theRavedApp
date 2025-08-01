// Mock for React Native components and APIs
export const Alert = {
  alert: jest.fn(),
};

export const Dimensions = {
  get: jest.fn(() => ({
    width: 375,
    height: 812,
  })),
  addEventListener: jest.fn(),
  removeEventListener: jest.fn(),
};

export const Platform = {
  OS: 'ios',
  select: jest.fn((obj) => obj.ios),
};

export const StyleSheet = {
  create: jest.fn((styles) => styles),
  flatten: jest.fn((style) => style),
};

export const View = 'View';
export const Text = 'Text';
export const TextInput = 'TextInput';
export const TouchableOpacity = 'TouchableOpacity';
export const ScrollView = 'ScrollView';
export const Image = 'Image';
export const SafeAreaView = 'SafeAreaView';

export default {
  Alert,
  Dimensions,
  Platform,
  StyleSheet,
  View,
  Text,
  TextInput,
  TouchableOpacity,
  ScrollView,
  Image,
  SafeAreaView,
};