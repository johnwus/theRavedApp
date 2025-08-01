/// <reference types="nativewind/types" />

// Global type declarations for TheRavedApp

declare module '*.svg' {
  import React from 'react';
  import { SvgProps } from 'react-native-svg';
  const content: React.FC<SvgProps>;
  export default content;
}

declare module '*.png' {
  const value: any;
  export default value;
}

declare module '*.jpg' {
  const value: any;
  export default value;
}

declare module '*.jpeg' {
  const value: any;
  export default value;
}

declare module '*.gif' {
  const value: any;
  export default value;
}

declare module '*.webp' {
  const value: any;
  export default value;
}

declare module '*.mp3' {
  const value: any;
  export default value;
}

declare module '*.mp4' {
  const value: any;
  export default value;
}

declare module '*.mov' {
  const value: any;
  export default value;
}

declare module '*.avi' {
  const value: any;
  export default value;
}

declare module '*.webm' {
  const value: any;
  export default value;
}

// Environment variables
declare namespace NodeJS {
  interface ProcessEnv {
    NODE_ENV: 'development' | 'production' | 'test';
    EXPO_PUBLIC_API_URL: string;
    EXPO_PUBLIC_WS_URL: string;
    EXPO_PUBLIC_APP_ENV: 'development' | 'staging' | 'production';
    EXPO_PUBLIC_SENTRY_DSN?: string;
    EXPO_PUBLIC_ANALYTICS_KEY?: string;
  }
}

// Expo modules augmentation
declare module 'expo-constants' {
  interface Constants {
    expoConfig: {
      extra?: {
        apiUrl?: string;
        wsUrl?: string;
        environment?: string;
      };
    };
  }
}

// React Navigation types
declare global {
  namespace ReactNavigation {
    interface RootParamList {
      // Define your navigation param list here
      Home: undefined;
      Profile: { userId: string };
      Settings: undefined;
      // Add more routes as needed
    }
  }
}

// Redux store types
declare module 'react-redux' {
  interface DefaultRootState {
    // Define your root state type here
    auth: any;
    user: any;
    // Add more slices as needed
  }
}

// Socket.io client types
declare module 'socket.io-client' {
  interface Socket {
    // Add custom socket event types here if needed
  }
}

export {};
