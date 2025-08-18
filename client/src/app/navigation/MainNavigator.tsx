import React from 'react';
import { createStackNavigator } from '@react-navigation/stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { Ionicons } from '@expo/vector-icons';
import { View, TouchableOpacity, StyleSheet } from 'react-native';
import { useAppSelector, useAppDispatch } from '@store/middleware';
import { setCreatePostModalVisible } from '@store/slices/uiSlice';

// Screens
import HomeScreen from '@screens/main/HomeScreen';
import ExploreScreen from '@screens/main/ExploreScreen';
import StoreScreen from '@screens/ecommerce/StoreScreen';
import ChatListScreen from '@screens/chat/ChatListScreen';
import ProfileScreen from '@screens/profile/ProfileScreen';
import CreatePostScreen from '@screens/social/CreatePostScreen';
import PostDetailScreen from '@screens/social/PostDetailScreen';
import ChatScreen from '@screens/chat/ChatScreen';
import UserProfileScreen from '@screens/profile/UserProfileScreen';
import EditProfileScreen from '@screens/profile/EditProfileScreen';
import SettingsScreen from '@screens/profile/SettingsScreen';

import { colors } from '@utils/constants/colors';
import { MainTabParamList, MainStackParamList } from './types';

const Stack = createStackNavigator<MainStackParamList>();
const Tab = createBottomTabNavigator<MainTabParamList>();

const CreatePostButton: React.FC<{ onPress: () => void }> = ({ onPress }) => {
  const { isDarkMode } = useAppSelector(state => state.ui);
  
  return (
    <TouchableOpacity 
      style={[styles.createButton, { backgroundColor: colors.primary }]} 
      onPress={onPress}
      activeOpacity={0.8}
    >
      <Ionicons name="add" size={24} color="white" />
    </TouchableOpacity>
  );
};

const TabNavigator: React.FC = () => {
  const dispatch = useAppDispatch();
  const { isDarkMode, tabBarVisible } = useAppSelector(state => state.ui);
  const { rooms } = useAppSelector(state => state.chat);
  
  const totalUnreadMessages = rooms.reduce((total, room) => total + (room.unreadCount || 0), 0);

  return (
    <Tab.Navigator
      screenOptions={({ route }) => ({
        headerShown: false,
        tabBarStyle: [
          styles.tabBar,
          {
            backgroundColor: isDarkMode ? colors.dark.surface : colors.light.surface,
            borderTopColor: isDarkMode ? colors.dark.border : colors.light.border,
            display: tabBarVisible ? 'flex' : 'none',
          }
        ],
        tabBarActiveTintColor: colors.primary,
        tabBarInactiveTintColor: isDarkMode ? colors.dark.textSecondary : colors.light.textSecondary,
        tabBarIcon: ({ focused, color, size }) => {
          let iconName: keyof typeof Ionicons.glyphMap;

          switch (route.name) {
            case 'Home':
              iconName = focused ? 'home' : 'home-outline';
              break;
            case 'Explore':
              iconName = focused ? 'search' : 'search-outline';
              break;
            case 'CreatePost':
              return (
                <CreatePostButton 
                  onPress={() => dispatch(setCreatePostModalVisible(true))} 
                />
              );
            case 'Store':
              iconName = focused ? 'storefront' : 'storefront-outline';
              break;
            case 'Chat':
              iconName = focused ? 'chatbubbles' : 'chatbubbles-outline';
              break;
            case 'Profile':
              iconName = focused ? 'person' : 'person-outline';
              break;
            default:
              iconName = 'home-outline';
          }

          return (
            <View style={styles.iconContainer}>
              <Ionicons name={iconName} size={size} color={color} />
              {route.name === 'Chat' && totalUnreadMessages > 0 && (
                <View style={styles.badge}>
                  <Text style={styles.badgeText}>
                    {totalUnreadMessages > 99 ? '99+' : totalUnreadMessages}
                  </Text>
                </View>
              )}
            </View>
          );
        },
      })}
    >
      <Tab.Screen name="Home" component={HomeScreen} />
      <Tab.Screen name="Explore" component={ExploreScreen} />
      <Tab.Screen 
        name="CreatePost" 
        component={View} 
        options={{ tabBarButton: () => null }}
      />
      <Tab.Screen name="Store" component={StoreScreen} />
      <Tab.Screen name="Chat" component={ChatListScreen} />
      <Tab.Screen name="Profile" component={ProfileScreen} />
    </Tab.Navigator>
  );
};

const MainNavigator: React.FC = () => {
  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Tabs" component={TabNavigator} />
      <Stack.Screen 
        name="CreatePost" 
        component={CreatePostScreen}
        options={{
          presentation: 'modal',
          gestureEnabled: true,
          gestureDirection: 'vertical',
        }}
      />
      <Stack.Screen name="PostDetail" component={PostDetailScreen} />
      <Stack.Screen name="Chat" component={ChatScreen} />
      <Stack.Screen name="UserProfile" component={UserProfileScreen} />
      <Stack.Screen name="EditProfile" component={EditProfileScreen} />
      <Stack.Screen name="Settings" component={SettingsScreen} />
    </Stack.Navigator>
  );
};

const styles = StyleSheet.create({
  tabBar: {
    height: 80,
    paddingBottom: 20,
    paddingTop: 10,
    borderTopWidth: 1,
    elevation: 8,
    shadowOffset: { width: 0, height: -2 },
    shadowOpacity: 0.1,
    shadowRadius: 8,
  },
  createButton: {
    width: 50,
    height: 50,
    borderRadius: 25,
    justifyContent: 'center',
    alignItems: 'center',
    elevation: 4,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.2,
    shadowRadius: 4,
  },
  iconContainer: {
    position: 'relative',
  },
  badge: {
    position: 'absolute',
    right: -8,
    top: -8,
    backgroundColor: colors.error,
    borderRadius: 10,
    minWidth: 20,
    height: 20,
    justifyContent: 'center',
    alignItems: 'center',
  },
  badgeText: {
    color: 'white',
    fontSize: 12,
    fontWeight: 'bold',
  },
});

export default MainNavigator;