export interface User {
    id: string;
    studentId: string;
    email: string;
    firstName: string;
    lastName: string;
    displayName: string;
    avatar?: string;
    bio?: string;
    facultyId: string;
    facultyName: string;
    year: number;
    major?: string;
    isVerified: boolean;
    isOnline: boolean;
    lastSeen: string;
    followersCount: number;
    followingCount: number;
    postsCount: number;
    isFollowing?: boolean;
    isFollower?: boolean;
    connectionStatus: 'none' | 'pending' | 'connected';
    preferences: UserPreferences;
    createdAt: string;
    updatedAt: string;
  }
  
  export interface UserPreferences {
    theme: 'light' | 'dark' | 'auto';
    notifications: {
      push: boolean;
      email: boolean;
      likes: boolean;
      comments: boolean;
      follows: boolean;
      messages: boolean;
      facultyUpdates: boolean;
    };
    privacy: {
      profileVisibility: 'public' | 'faculty' | 'connections';
      showOnlineStatus: boolean;
      allowMessages: 'everyone' | 'connections' | 'faculty';
      showActivity: boolean;
    };
    feed: {
      autoplay: boolean;
      dataUsage: 'low' | 'medium' | 'high';
      showFacultyFirst: boolean;
    };
  }
  
  export interface Faculty {
    id: string;
    name: string;
    shortName: string;
    description: string;
    color: string;
    membersCount: number;
    postsCount: number;
  }
  
  export interface UserStats {
    postsCount: number;
    likesReceived: number;
    commentsReceived: number;
    followersCount: number;
    followingCount: number;
    profileViews: number;
    engagementRate: number;
  }