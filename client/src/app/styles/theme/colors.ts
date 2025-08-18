export type Palette = {
  // Brand and activity
  logo: string;
  active: string;
  bg: string;

  // Primary/secondary system
  primary: string;
  primaryLight: string;
  primaryDark: string;
  secondary: string;
  secondaryLight: string;
  secondaryDark: string;

  // Accents
  accent: string; // kept for backwards compat (maps to accent2)
  accent1: string;
  accent2: string;
  accent3: string;

  // Neutrals and text
  background: string;
  card: string;
  surface: string;
  text: string;
  textSecondary: string;
  textTertiary: string;
  placeholder: string;
  white: string;
  border: string;
  divider: string;

  // Feedback
  success: string;
  successLight: string;
  warning: string;
  warningLight: string;
  error: string;
  errorLight: string;
  errorRed: string;
  info: string;
  infoLight: string;

  // States
  disabled: string;
  textDisabled: string;
  inactive: string;
  hover: string;
  pressed: string;
  focus: string;

  // Overlays & misc
  overlay: string;
  overlayLight: string;
  shadow: string;
  skeleton: string;
  lightShimmer: string;

  // Nested themed values for RN components
  light: {
    background: string;
    surface: string;
    card: string;
    border: string;
    text: string;
    textSecondary: string;
    secondary: string;
    primary: string;
  };
  dark: {
    background: string;
    surface: string;
    card: string;
    border: string;
    text: string;
    textSecondary: string;
    secondary: string;
    primary: string;
    textTertiary: string;
    placeholder: string;
    divider: string;
    shadow: string;
  };
};

export const colors: Palette = {
  // Brand & activity
  logo: '#040723',
  active: '#0055B8',
  bg: '#FFFFFF',

  // Primary/secondary
  primary: '#0055B8',
  primaryLight: '#4D8FD6',
  primaryDark: '#003C80',
  secondary: '#FF7A00',
  secondaryLight: '#FFA64D',
  secondaryDark: '#CC6200',

  // Accents
  accent: '#6C63FF',
  accent1: '#00C2A8',
  accent2: '#6C63FF',
  accent3: '#FF5C85',

  // Neutrals & text
  background: '#F8F9FC',
  card: '#FFFFFF',
  surface: '#FFFFFF',
  text: '#1A1A1A',
  textSecondary: '#6B7280',
  textTertiary: '#9CA3AF',
  placeholder: '#A1A1A1',
  white: '#FFFFFF',
  border: '#E5E7EB',
  divider: '#F2F4F7',

  // Feedback
  success: '#22C55E',
  successLight: '#DCFCE7',
  warning: '#F59E0B',
  warningLight: '#FEF3C7',
  error: '#EF4444',
  errorLight: '#FEE2E2',
  errorRed: '#FF4C4C',
  info: '#3B82F6',
  infoLight: '#DBEAFE',

  // States
  disabled: '#C7C7CC',
  textDisabled: '#9CA3AF',
  inactive: '#C7C7CC',
  hover: '#F5F7FA',
  pressed: '#E5E7EB',
  focus: 'rgba(0, 102, 204, 0.2)',

  // Overlays & misc
  overlay: 'rgba(0, 0, 0, 0.5)',
  overlayLight: 'rgba(0, 0, 0, 0.2)',
  shadow: 'rgba(0,0,0,0.1)',
  skeleton: '#F3F4F6',
  lightShimmer: '#E5E7EB',

  // Nested themes
  light: {
    background: '#F8F9FC',
    surface: '#FFFFFF',
    card: '#FFFFFF',
    border: '#E5E7EB',
    text: '#1A1A1A',
    textSecondary: '#6B7280',
    secondary: '#F3F4F6',
    primary: '#0055B8',
  },
  dark: {
    background: '#181A20', // darkBackground
    surface: '#23262F', // darkCard
    card: '#23262F',
    border: '#23262F', // darkBorder
    text: '#F4F4F4', // darkText
    textSecondary: '#A3A7B0', // darkTextSecondary
    textTertiary: '#6B7280', // darkTextTertiary
    placeholder: '#444857', // darkPlaceholder
    divider: '#23262F', // darkDivider
    shadow: 'rgba(0,0,0,0.6)', // darkShadow
    secondary: '#2D2D2D',
    primary: '#7775E8',
  },
};

export type ThemeMode = 'light' | 'dark';

export const getColorsForMode = (mode: ThemeMode) => (mode === 'dark' ? colors.dark : colors.light);


