import {
  isValidEmail,
  isValidUsername,
  isValidPassword,
  isStrongPassword,
  isValidPhone
} from '../../src/utils/helpers/validationHelpers';

describe('validationHelpers', () => {
  describe('isValidEmail', () => {
    it('should validate correct email addresses', () => {
      expect(isValidEmail('test@example.com')).toBe(true);
      expect(isValidEmail('user.name@domain.co.uk')).toBe(true);
    });

    it('should reject invalid email addresses', () => {
      expect(isValidEmail('invalid-email')).toBe(false);
      expect(isValidEmail('@domain.com')).toBe(false);
      expect(isValidEmail('test@')).toBe(false);
    });
  });

  describe('isValidUsername', () => {
    it('should validate correct usernames', () => {
      expect(isValidUsername('user123')).toBe(true);
      expect(isValidUsername('test_user')).toBe(true);
    });

    it('should reject invalid usernames', () => {
      expect(isValidUsername('ab')).toBe(false); // too short
      expect(isValidUsername('user-name')).toBe(false); // contains hyphen
    });
  });

  describe('isValidPassword', () => {
    it('should validate passwords with minimum length', () => {
      expect(isValidPassword('password123')).toBe(true);
    });

    it('should reject short passwords', () => {
      expect(isValidPassword('short')).toBe(false);
    });
  });

  describe('isStrongPassword', () => {
    it('should validate strong passwords', () => {
      expect(isStrongPassword('Password123!')).toBe(true);
    });

    it('should reject weak passwords', () => {
      expect(isStrongPassword('password')).toBe(false); // no uppercase, digit, special
      expect(isStrongPassword('PASSWORD')).toBe(false); // no lowercase, digit, special
    });
  });

  describe('isValidPhone', () => {
    it('should validate correct phone numbers', () => {
      expect(isValidPhone('+1234567890')).toBe(true);
      expect(isValidPhone('1234567890')).toBe(true);
    });

    it('should reject invalid phone numbers', () => {
      expect(isValidPhone('123')).toBe(false); // too short
      expect(isValidPhone('abc123')).toBe(false); // contains letters
    });
  });
});