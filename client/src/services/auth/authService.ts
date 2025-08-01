import { apiClient } from '../api/client';
import { tokenService } from './tokenService';

interface LoginCredentials {
  usernameOrEmail: string;
  password: string;
}

interface RegisterData {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
}

interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  user: {
    id: string;
    email: string;
    username: string;
    firstName: string;
    lastName: string;
  };
}

class AuthService {
  async login(credentials: LoginCredentials): Promise<AuthResponse> {
    const response = await apiClient.post('/api/auth/login', credentials);
    const { accessToken, refreshToken } = response.data;

    await tokenService.setTokens(accessToken, refreshToken);

    return response.data;
  }

  async register(data: RegisterData): Promise<AuthResponse> {
    const response = await apiClient.post('/api/auth/register', data);
    const { accessToken, refreshToken } = response.data;

    await tokenService.setTokens(accessToken, refreshToken);

    return response.data;
  }

  async logout(): Promise<void> {
    try {
      await apiClient.post('/api/auth/logout');
    } catch (error) {
      // Continue with logout even if API call fails
      console.warn('Logout API call failed:', error);
    } finally {
      await tokenService.clearTokens();
    }
  }

  async refreshToken(): Promise<string | null> {
    try {
      const refreshToken = await tokenService.getRefreshToken();
      if (!refreshToken) {
        return null;
      }

      const response = await apiClient.post('/api/auth/refresh', {
        refreshToken,
      });

      const { accessToken } = response.data;
      await tokenService.setAccessToken(accessToken);

      return accessToken;
    } catch (error) {
      await tokenService.clearTokens();
      return null;
    }
  }
}

export const authService = new AuthService();