// Mock for Expo SecureStore
const mockStorage = {};

export const setItemAsync = jest.fn((key, value) => {
  mockStorage[key] = value;
  return Promise.resolve();
});

export const getItemAsync = jest.fn((key) => {
  return Promise.resolve(mockStorage[key] || null);
});

export const deleteItemAsync = jest.fn((key) => {
  delete mockStorage[key];
  return Promise.resolve();
});

export const isAvailableAsync = jest.fn(() => Promise.resolve(true));

export default {
  setItemAsync,
  getItemAsync,
  deleteItemAsync,
  isAvailableAsync,
};