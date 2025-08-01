import { authService } from '../../src/services/auth/authService';

// Mock the API client
jest.mock('../../src/services/api/client', () => ({
  apiClient: {
    post: jest.fn(),
  },
}));

// Mock the token service
jest.mock('../../src/services/auth/tokenService', () => ({
  tokenService: {
    setTokens: jest.fn(),
    clearTokens: jest.fn(),
    getRefreshToken: jest.fn(),
    setAccessToken: jest.fn(),
  },
}));

describe('authService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('login', () => {
    it('should login successfully and store tokens', async () => {
      const mockResponse = {
        data: {
          accessToken: 'mock-access-token',
          refreshToken: 'mock-refresh-token',
          user: { id: '1', email: 'test@example.com' },
        },
      };

      const { apiClient } = require('../../src/services/api/client');
      const { tokenService } = require('../../src/services/auth/tokenService');

      apiClient.post.mockResolvedValue(mockResponse);

      const credentials = {
        usernameOrEmail: 'test@example.com',
        password: 'password123',
      };

      const result = await authService.login(credentials);

      expect(apiClient.post).toHaveBeenCalledWith('/api/auth/login', credentials);
      expect(tokenService.setTokens).toHaveBeenCalledWith(
        'mock-access-token',
        'mock-refresh-token'
      );
      expect(result).toEqual(mockResponse.data);
    });
  });

  describe('logout', () => {
    it('should logout and clear tokens', async () => {
      const { apiClient } = require('../../src/services/api/client');
      const { tokenService } = require('../../src/services/auth/tokenService');

      apiClient.post.mockResolvedValue({});

      await authService.logout();

      expect(apiClient.post).toHaveBeenCalledWith('/api/auth/logout');
      expect(tokenService.clearTokens).toHaveBeenCalled();
    });
  });
});