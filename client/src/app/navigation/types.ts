import { BottomTabScreenProps } from '@react-navigation/bottom-tabs';
import { StackScreenProps } from '@react-navigation/stack';
import { NavigatorScreenParams } from '@react-navigation/native';

// Auth Navigator
export type AuthStackParamList = {
  Welcome: undefined;
  Login: undefined;
  Register: undefined;
  ForgotPassword: undefined;
  StudentVerification: { studentId: string };
};

// Main Tab Navigator
export type MainTabParamList = {
  Home: undefined;
  Explore: undefined;
  CreatePost: undefined;
  Store: undefined;
  Chat: undefined;
  Profile: undefined;
};

// Main Stack Navigator
export type MainStackParamList = {
  Tabs: NavigatorScreenParams<MainTabParamList>;
  CreatePost: undefined;
  PostDetail: { postId: string };
  Chat: { roomId: string; roomName?: string };
  UserProfile: { userId: string };
  EditProfile: undefined;
  Settings: undefined;
  Comments: { postId: string };
  Followers: { userId: string };
  Following: { userId: string };
  ProductDetail: { productId: string };
  Checkout: { items: any[] };
  OrderHistory: undefined;
};

// Root Navigator
export type RootStackParamList = {
  Auth: NavigatorScreenParams<AuthStackParamList>;
  Main: NavigatorScreenParams<MainStackParamList>;
};

// Screen Props Types
export type AuthScreenProps<T extends keyof AuthStackParamList> = StackScreenProps<AuthStackParamList, T>;
export type MainTabScreenProps<T extends keyof MainTabParamList> = BottomTabScreenProps<MainTabParamList, T>;
export type MainStackScreenProps<T extends keyof MainStackParamList> = StackScreenProps<MainStackParamList, T>;

declare global {
  namespace ReactNavigation {
    interface RootParamList extends RootStackParamList {}
  }
}