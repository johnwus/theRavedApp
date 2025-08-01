import { formatDate, formatRelativeTime } from '../../src/utils/helpers/dateHelpers';

describe('dateHelpers', () => {
  describe('formatDate', () => {
    it('should format today\'s date as time only', () => {
      const today = new Date();
      const result = formatDate(today);
      expect(result).toMatch(/^\d{2}:\d{2}$/);
    });

    it('should format yesterday as "Yesterday"', () => {
      const yesterday = new Date();
      yesterday.setDate(yesterday.getDate() - 1);
      const result = formatDate(yesterday);
      expect(result).toBe('Yesterday');
    });

    it('should format older dates as "MMM d"', () => {
      const oldDate = new Date('2023-01-15');
      const result = formatDate(oldDate);
      expect(result).toBe('Jan 15');
    });
  });

  describe('formatRelativeTime', () => {
    it('should format relative time correctly', () => {
      const pastDate = new Date();
      pastDate.setHours(pastDate.getHours() - 2);
      const result = formatRelativeTime(pastDate);
      expect(result).toContain('ago');
    });
  });
});