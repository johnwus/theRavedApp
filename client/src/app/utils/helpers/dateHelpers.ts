import { format, formatDistanceToNow, isToday, isYesterday } from 'date-fns';

export const formatDate = (date: Date): string => {
  if (isToday(date)) {
    return format(date, 'HH:mm');
  }

  if (isYesterday(date)) {
    return 'Yesterday';
  }

  return format(date, 'MMM d');
};

export const formatRelativeTime = (date: Date): string => {
  return formatDistanceToNow(date, { addSuffix: true });
};

export const formatFullDate = (date: Date): string => {
  return format(date, 'MMMM d, yyyy');
};

export const formatDateTime = (date: Date): string => {
  return format(date, 'MMM d, yyyy HH:mm');
};