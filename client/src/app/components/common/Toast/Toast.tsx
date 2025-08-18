import React, { useEffect, useRef, useCallback } from 'react';
import {
  View,
  Text,
  StyleSheet,
  Animated,
  TouchableOpacity,
  Dimensions,
  Platform,
  Modal,
} from 'react-native';
import * as Haptics from 'expo-haptics';

import { useAppTheme } from '@styles/theme';
import { Icons } from '@/utils/icons';
const { width } = Dimensions.get('window');

export type ToastType = 'success' | 'error' | 'warning' | 'info';

export interface ToastProps {
  visible: boolean;
  type: ToastType;
  title: string;
  message?: string;
  duration?: number;
  onClose: () => void;
  soundEnabled?: boolean;
  hapticEnabled?: boolean;
}

const Toast: React.FC<ToastProps> = ({
  visible,
  type,
  title,
  message,
  duration = 4000,
  onClose,
  soundEnabled = true,
  hapticEnabled = true,
}) => {
  const { theme } = useAppTheme();
  const translateY = useRef(new Animated.Value(-100)).current;
  const opacity = useRef(new Animated.Value(0)).current;

  const getToastConfig = () => {
    switch (type) {
      case 'success':
        return {
          icon: Icons.CheckCircle,
          backgroundColor: theme.colors.success,
          iconColor: 'white',
          hapticType: Haptics.NotificationFeedbackType.Success,
        };
      case 'error':
        return {
          icon: Icons.XCircle,
          backgroundColor: theme.colors.error,
          iconColor: 'white',
          hapticType: Haptics.NotificationFeedbackType.Error,
        };
      case 'warning':
        return {
          icon: Icons.AlertCircle,
          backgroundColor: theme.colors.warning,
          iconColor: 'white',
          hapticType: Haptics.NotificationFeedbackType.Warning,
        };
      case 'info':
        return {
          icon: Icons.Info,
          backgroundColor: theme.colors.primary,
          iconColor: 'white',
          hapticType: Haptics.NotificationFeedbackType.Success,
        };
      default:
        return {
          icon: Icons.Info,
          backgroundColor: theme.colors.primary,
          iconColor: 'white',
          hapticType: Haptics.NotificationFeedbackType.Success,
        };
    }
  };

  const triggerHaptic = useCallback(() => {
    if (!hapticEnabled) return;

    // Use requestAnimationFrame to avoid blocking the main thread
    requestAnimationFrame(() => {
      try {
        const config = getToastConfig();
        Haptics.notificationAsync(config.hapticType);
      } catch (error) {
        console.log('Error triggering haptic:', error);
      }
    });
  }, [hapticEnabled, getToastConfig]);

  const showToast = useCallback(() => {
    // Optimized animation with reduced duration and spring physics
    Animated.parallel([
      Animated.spring(translateY, {
        toValue: 0,
        tension: 100,
        friction: 8,
        useNativeDriver: true,
      }),
      Animated.timing(opacity, {
        toValue: 1,
        duration: 200, // Reduced from 300ms
        useNativeDriver: true,
      }),
    ]).start();

    // Trigger haptic feedback asynchronously
    triggerHaptic();

    // Auto-hide after duration
    const hideTimer = setTimeout(() => {
      hideToast();
    }, duration);

    return () => clearTimeout(hideTimer);
  }, [translateY, opacity, triggerHaptic, duration]);

  const hideToast = useCallback(() => {
    // Faster hide animation with spring physics
    Animated.parallel([
      Animated.spring(translateY, {
        toValue: -100,
        tension: 120,
        friction: 8,
        useNativeDriver: true,
      }),
      Animated.timing(opacity, {
        toValue: 0,
        duration: 150, // Reduced from 300ms
        useNativeDriver: true,
      }),
    ]).start(() => {
      onClose();
    });
  }, [translateY, opacity, onClose]);

  useEffect(() => {
    if (visible) {
      const cleanup = showToast();
      return cleanup;
    }
  }, [visible, showToast]);

  if (!visible) return null;

  const config = getToastConfig();
  const IconComponent = config.icon;

  return (
    <Animated.View
      style={[
        styles.container,
        {
          backgroundColor: config.backgroundColor,
          transform: [{ translateY }],
          opacity,
        },
      ]}
    >
      <View style={styles.content}>
        <View style={styles.iconContainer}>
          <IconComponent size={24} color={config.iconColor} />
        </View>
        
        <View style={styles.textContainer}>
          <Text style={styles.title}>{title}</Text>
          {message && <Text style={styles.message}>{message}</Text>}
        </View>

        <TouchableOpacity style={styles.closeButton} onPress={hideToast}>
          <Icons.X size={20} color="white" />
        </TouchableOpacity>
      </View>
    </Animated.View>
  );
};

const styles = StyleSheet.create({
  container: {
    position: 'absolute',
    top: Platform.OS === 'ios' ? 60 : 40,
    left: 16,
    right: 16,
    borderRadius: 12,
    padding: 16,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 4,
    },
    shadowOpacity: 0.15,
    shadowRadius: 8,
    elevation: 9999,
    zIndex: 999999,
  },
  content: {
    flexDirection: 'row',
    alignItems: 'flex-start',
  },
  iconContainer: {
    marginRight: 12,
    marginTop: 2,
  },
  textContainer: {
    flex: 1,
    marginRight: 8,
  },
  title: {
    fontSize: 16,
    fontWeight: '600',
    color: 'white',
    marginBottom: 2,
  },
  message: {
    fontSize: 14,
    color: 'rgba(255, 255, 255, 0.9)',
    lineHeight: 20,
  },
  closeButton: {
    padding: 4,
    marginTop: 2,
  },
});

export default Toast; 