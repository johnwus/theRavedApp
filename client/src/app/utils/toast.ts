// Toast utility that integrates with our theming system

import { useAppTheme } from '../styles/theme';

// Toast configuration
export type ToastType = 'success' | 'error' | 'warning' | 'info';

export interface ToastConfig {
  type: ToastType;
  title: string;
  message?: string;
  duration?: number;
  soundEnabled?: boolean;
  hapticEnabled?: boolean;
  theme?: any; // Optional theme override
}

// Toast manager with theming support
class ToastManager {
  private listeners: Array<(config: ToastConfig) => void> = [];
  private currentTheme: any = null;

  // Set current theme (called by ToastProvider)
  setTheme(theme: any) {
    this.currentTheme = theme;
  }

  // Subscribe to toast events
  subscribe(listener: (config: ToastConfig) => void): () => void {
    this.listeners.push(listener);
    return () => {
      const index = this.listeners.indexOf(listener);
      if (index > -1) {
        this.listeners.splice(index, 1);
      }
    };
  }

  // Show toast notification
  show(config: ToastConfig): void {
    // Merge with current theme if available
    const finalConfig = {
      ...config,
      theme: config.theme || this.currentTheme,
    };
    
    // Notify listeners
    this.listeners.forEach(listener => listener(finalConfig));
  }

  // Convenience methods with theme support
  success(title: string, message?: string, theme?: any): void {
    this.show({ type: 'success', title, message, theme });
  }

  error(title: string, message?: string, theme?: any): void {
    this.show({ type: 'error', title, message, theme });
  }

  warning(title: string, message?: string, theme?: any): void {
    this.show({ type: 'warning', title, message, theme });
  }

  info(title: string, message?: string, theme?: any): void {
    this.show({ type: 'info', title, message, theme });
  }

  // Financial transaction toasts
  paymentSuccess(amount: string, recipient?: string): void {
    const title = 'Payment Successful';
    const message = recipient 
      ? `You sent GH₵${amount} to ${recipient}`
      : `Payment of GH₵${amount} completed`;
    this.success(title, message);
  }

  paymentFailed(reason?: string): void {
    const title = 'Payment Failed';
    const message = reason || 'Please try again or contact support';
    this.error(title, message);
  }

  walletTopUpSuccess(amount: string): void {
    this.success('Wallet Topped Up', `GH₵${amount} added to your wallet`);
  }

  // Authentication toasts
  loginSuccess(): void {
    this.success('Welcome Back!', 'You have successfully logged in');
  }

  loginFailed(reason?: string): void {
    this.error('Login Failed', reason || 'Please check your credentials');
  }

  otpSent(): void {
    this.info('OTP Sent', 'Check your phone for the verification code');
  }

  otpVerified(): void {
    this.success('OTP Verified', 'Your account has been verified');
  }

  otpFailed(): void {
    this.error('OTP Failed', 'Please check the code and try again');
  }

  // KYC toasts
  kycSubmitted(): void {
    this.success('KYC Submitted', 'We will review your documents within 24-48 hours');
  }

  kycApproved(): void {
    this.success('KYC Approved', 'Your account is now fully verified');
  }

  kycRejected(reason?: string): void {
    this.error('KYC Rejected', reason || 'Please check your documents and try again');
  }

  // Security toasts
  securityEnabled(type: string): void {
    this.success('Security Enabled', `${type} security has been activated`);
  }

  securityDisabled(type: string): void {
    this.warning('Security Disabled', `${type} security has been deactivated`);
  }

  // Profile toasts
  profileUpdated(): void {
    this.success('Profile Updated', 'Your profile has been saved successfully');
  }

  profileUpdateFailed(): void {
    this.error('Update Failed', 'Failed to update profile. Please try again');
  }

  // Payment method toasts
  paymentMethodAdded(type: string): void {
    this.success('Payment Method Added', `${type} has been added successfully`);
  }

  paymentMethodRemoved(type: string): void {
    this.success('Payment Method Removed', `${type} has been removed`);
  }

  paymentMethodUpdated(type: string): void {
    this.success('Payment Method Updated', `${type} has been updated`);
  }

  // Network toasts
  networkError(): void {
    this.error('Network Error', 'Please check your internet connection');
  }

  serverError(): void {
    this.error('Server Error', 'Something went wrong. Please try again later');
  }

  // Cart toasts
  itemAddedToCart(itemName: string): void {
    this.success('Added to Cart', `${itemName} has been added to your cart`);
  }

  itemRemovedFromCart(itemName: string): void {
    this.info('Removed from Cart', `${itemName} has been removed from your cart`);
  }

  cartCleared(): void {
    this.info('Cart Cleared', 'All items have been removed from your cart');
  }

  // Order toasts
  orderPlaced(orderId: string): void {
    this.success('Order Placed', `Order #${orderId} has been confirmed`);
  }

  orderDelivered(orderId: string): void {
    this.success('Order Delivered', `Order #${orderId} has been delivered`);
  }

  // Settings toasts
  settingUpdated(setting: string): void {
    this.success('Setting Updated', `${setting} has been saved`);
  }

  // Generic toasts
  genericSuccess(action: string): void {
    this.success('Success', `${action} completed successfully`);
  }

  genericError(action: string): void {
    this.error('Error', `Failed to ${action}. Please try again`);
  }

  genericWarning(message: string): void {
    this.warning('Warning', message);
  }

  genericInfo(message: string): void {
    this.info('Info', message);
  }
}

// Create the toast instance
const toastInstance = new ToastManager();

// Export function-based API
export const toast = {
  // Basic toast functions with theme support
  success: (title: string, message?: string, theme?: any) => toastInstance.success(title, message, theme),
  error: (title: string, message?: string, theme?: any) => toastInstance.error(title, message, theme),
  warning: (title: string, message?: string, theme?: any) => toastInstance.warning(title, message, theme),
  info: (title: string, message?: string, theme?: any) => toastInstance.info(title, message, theme),
  
  // Financial transaction toasts
  paymentSuccess: (amount: string, recipient?: string, theme?: any) => toastInstance.paymentSuccess(amount, recipient),
  paymentFailed: (reason?: string, theme?: any) => toastInstance.paymentFailed(reason),
  walletTopUpSuccess: (amount: string, theme?: any) => toastInstance.walletTopUpSuccess(amount),
  
  // Authentication toasts
  loginSuccess: (theme?: any) => toastInstance.loginSuccess(),
  loginFailed: (reason?: string, theme?: any) => toastInstance.loginFailed(reason),
  otpSent: (theme?: any) => toastInstance.otpSent(),
  otpVerified: (theme?: any) => toastInstance.otpVerified(),
  otpFailed: (theme?: any) => toastInstance.otpFailed(),
  
  // KYC toasts
  kycSubmitted: (theme?: any) => toastInstance.kycSubmitted(),
  kycApproved: (theme?: any) => toastInstance.kycApproved(),
  kycRejected: (reason?: string, theme?: any) => toastInstance.kycRejected(reason),
  
  // Security toasts
  securityEnabled: (type: string, theme?: any) => toastInstance.securityEnabled(type),
  securityDisabled: (type: string, theme?: any) => toastInstance.securityDisabled(type),
  
  // Profile toasts
  profileUpdated: (theme?: any) => toastInstance.profileUpdated(),
  profileUpdateFailed: (theme?: any) => toastInstance.profileUpdateFailed(),
  
  // Payment method toasts
  paymentMethodAdded: (type: string, theme?: any) => toastInstance.paymentMethodAdded(type),
  paymentMethodRemoved: (type: string, theme?: any) => toastInstance.paymentMethodRemoved(type),
  paymentMethodUpdated: (type: string, theme?: any) => toastInstance.paymentMethodUpdated(type),
  
  // Network toasts
  networkError: (theme?: any) => toastInstance.networkError(),
  serverError: (theme?: any) => toastInstance.serverError(),
  
  // Cart toasts
  itemAddedToCart: (itemName: string, theme?: any) => toastInstance.itemAddedToCart(itemName),
  itemRemovedFromCart: (itemName: string, theme?: any) => toastInstance.itemRemovedFromCart(itemName),
  cartCleared: (theme?: any) => toastInstance.cartCleared(),
  
  // Order toasts
  orderPlaced: (orderId: string, theme?: any) => toastInstance.orderPlaced(orderId),
  orderDelivered: (orderId: string, theme?: any) => toastInstance.orderDelivered(orderId),
  
  // Settings toasts
  settingUpdated: (setting: string, theme?: any) => toastInstance.settingUpdated(setting),
  
  // Generic toasts
  genericSuccess: (action: string, theme?: any) => toastInstance.genericSuccess(action),
  genericError: (action: string, theme?: any) => toastInstance.genericError(action),
  genericWarning: (message: string, theme?: any) => toastInstance.genericWarning(message),
  genericInfo: (message: string, theme?: any) => toastInstance.genericInfo(message),
  
  // Subscribe method for ToastProvider
  subscribe: (listener: (config: ToastConfig) => void) => toastInstance.subscribe(listener),
  
  // Theme management
  setTheme: (theme: any) => toastInstance.setTheme(theme),
};

