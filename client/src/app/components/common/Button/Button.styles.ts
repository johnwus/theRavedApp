import { StyleSheet } from 'react-native';

export const buttonStyles = StyleSheet.create({
  button: {
    borderRadius: 12,
    justifyContent: 'center',
    alignItems: 'center',
    flexDirection: 'row',
  },
  button_small: {
    paddingHorizontal: 16,
    paddingVertical: 8,
    minHeight: 36,
  },
  button_medium: {
    paddingHorizontal: 20,
    paddingVertical: 12,
    minHeight: 44,
  },
  button_large: {
    paddingHorizontal: 24,
    paddingVertical: 16,
    minHeight: 52,
  },
  buttonFullWidth: {
    width: '100%',
  },
  buttonText: {
    fontFamily: 'Inter-Medium',
    textAlign: 'center',
  },
  buttonText_small: {
    fontSize: 14,
  },
  buttonText_medium: {
    fontSize: 16,
  },
  buttonText_large: {
    fontSize: 18,
  },
  buttonContent: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
});
