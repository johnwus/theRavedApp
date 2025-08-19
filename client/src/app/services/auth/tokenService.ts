import { MMKV } from "react-native-mmkv"

const storage = new MMKV()
const ACCESS_TOKEN_KEY = "access_token"
const REFRESH_TOKEN_KEY = "refresh_token"

class TokenService {
  async setTokens(accessToken: string, refreshToken: string): Promise<void> {
    storage.set(ACCESS_TOKEN_KEY, accessToken)
    storage.set(REFRESH_TOKEN_KEY, refreshToken)
  }

  async getAccessToken(): Promise<string | null> {
    return storage.getString(ACCESS_TOKEN_KEY) ?? null
  }

  async getRefreshToken(): Promise<string | null> {
    return storage.getString(REFRESH_TOKEN_KEY) ?? null
  }

  async setAccessToken(accessToken: string): Promise<void> {
    storage.set(ACCESS_TOKEN_KEY, accessToken)
  }

  async clearTokens(): Promise<void> {
    storage.delete(ACCESS_TOKEN_KEY)
    storage.delete(REFRESH_TOKEN_KEY)
  }

  async hasValidTokens(): Promise<boolean> {
    const accessToken = await this.getAccessToken()
    return !!accessToken
  }
}

export const tokenService = new TokenService()
