import { MMKV } from 'react-native-mmkv'

// Create MMKV storage instance
export const storage = new MMKV({
  id: 'raved-app-storage',
  encryptionKey: 'raved-app-encryption-key'
})

// Helper functions for common storage operations
export const storageUtils = {
  // String operations
  setString: (key: string, value: string) => storage.set(key, value),
  getString: (key: string) => storage.getString(key),
  deleteString: (key: string) => storage.delete(key),
  
  // Boolean operations
  setBoolean: (key: string, value: boolean) => storage.set(key, value),
  getBoolean: (key: string) => storage.getBoolean(key),
  
  // Number operations
  setNumber: (key: string, value: number) => storage.set(key, value),
  getNumber: (key: string) => storage.getNumber(key),
  
  // Object operations
  setObject: (key: string, value: object) => storage.set(key, JSON.stringify(value)),
  getObject: <T>(key: string): T | null => {
    const value = storage.getString(key)
    if (value) {
      try {
        return JSON.parse(value) as T
      } catch {
        return null
      }
    }
    return null
  },
  
  // Array operations
  setArray: (key: string, value: any[]) => storage.set(key, JSON.stringify(value)),
  getArray: <T>(key: string): T[] | null => {
    const value = storage.getString(key)
    if (value) {
      try {
        return JSON.parse(value) as T[]
      } catch {
        return null
      }
    }
    return null
  },
  
  // Check if key exists
  contains: (key: string) => storage.contains(key),
  
  // Get all keys
  getAllKeys: () => storage.getAllKeys(),
  
  // Clear all data
  clearAll: () => storage.clearAll(),
}
