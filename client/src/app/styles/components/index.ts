export { buttonStyles } from './buttonStyles';
export { cardStyles } from './cardStyles';
export { inputStyles } from './inputStyles';

// Re-export commonly used style utilities
export const componentUtils = {
  // Common layout patterns
  flexCenter: {
    flex: 1,
    alignItems: 'center' as const,
    justifyContent: 'center' as const,
  },
  flexRow: {
    flexDirection: 'row' as const,
    alignItems: 'center' as const,
  },
  flexColumn: {
    flexDirection: 'column' as const,
  },
  
  // Common spacing patterns
  padding: {
    xs: { padding: 4 },
    sm: { padding: 8 },
    md: { padding: 16 },
    lg: { padding: 24 },
    xl: { padding: 32 },
  },
  
  margin: {
    xs: { margin: 4 },
    sm: { margin: 8 },
    md: { margin: 16 },
    lg: { margin: 24 },
    xl: { margin: 32 },
  },
  
  // Common border patterns
  borders: {
    rounded: { borderRadius: 8 },
    roundedMd: { borderRadius: 16 },
    roundedLg: { borderRadius: 24 },
    roundedFull: { borderRadius: 9999 },
  },
};
