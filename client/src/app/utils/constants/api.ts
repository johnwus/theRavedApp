export const API_CONFIG = {
  BASE_URL: __DEV__ ? "http://localhost:8080/api/v1" : "https://api.raved.com/v1",
  TIMEOUT: 30000,
  RETRY_ATTEMPTS: 3,
  RETRY_DELAY: 1000,
}

export const API_ENDPOINTS = {
  // Auth
  AUTH: {
    LOGIN: "/auth/login",
    REGISTER: "/auth/register",
    REFRESH: "/auth/refresh",
    LOGOUT: "/auth/logout",
    VERIFY_STUDENT: "/auth/verify-student",
    FORGOT_PASSWORD: "/auth/forgot-password",
    RESET_PASSWORD: "/auth/reset-password",
    CHANGE_PASSWORD: "/auth/change-password",
  },

  // Users
  USERS: {
    PROFILE: "/users/profile",
    UPDATE_PROFILE: "/users/profile",
    SEARCH: "/users/search",
    FOLLOWERS: "/users/followers",
    FOLLOWING: "/users/following",
    SUGGESTIONS: "/users/suggestions",
    FOLLOW: (userId: string) => `/users/${userId}/follow`,
    UNFOLLOW: (userId: string) => `/users/${userId}/unfollow`,
    BLOCK: (userId: string) => `/users/${userId}/block`,
    REPORT: (userId: string) => `/users/${userId}/report`,
  },

  // Posts
  POSTS: {
    FEED: "/posts/feed",
    TRENDING: "/posts/trending",
    POST: (postId: string) => `/posts/${postId}`,
    CREATE: "/posts",
    UPDATE: (postId: string) => `/posts/${postId}`,
    DELETE: (postId: string) => `/posts/${postId}`,
    LIKE: (postId: string) => `/posts/${postId}/like`,
    UNLIKE: (postId: string) => `/posts/${postId}/unlike`,
    SAVE: (postId: string) => `/posts/${postId}/save`,
    UNSAVE: (postId: string) => `/posts/${postId}/unsave`,
    COMMENTS: (postId: string) => `/posts/${postId}/comments`,
    SEARCH: "/posts/search",
  },

  // Chat
  CHAT: {
    ROOMS: "/chat/rooms",
    ROOM: (roomId: string) => `/chat/rooms/${roomId}`,
    MESSAGES: (roomId: string) => `/chat/rooms/${roomId}/messages`,
    SEND_MESSAGE: (roomId: string) => `/chat/rooms/${roomId}/messages`,
    TYPING: (roomId: string) => `/chat/rooms/${roomId}/typing`,
  },

  // Media
  MEDIA: {
    UPLOAD: "/media/upload",
    UPLOAD_AVATAR: "/media/upload/avatar",
  },

  // Faculties
  FACULTIES: {
    LIST: "/faculties",
    FACULTY: (facultyId: string) => `/faculties/${facultyId}`,
  },

  // Analytics
  ANALYTICS: {
    USER_STATS: "/analytics/user",
    POST_ANALYTICS: (postId: string) => `/analytics/posts/${postId}`,
    ENGAGEMENT: "/analytics/engagement",
  },

  // Store/E-commerce
  STORE: {
    PRODUCTS: "/store/products",
    PRODUCT: (productId: string) => `/store/products/${productId}`,
    CART: "/store/cart",
    ORDERS: "/store/orders",
    ORDER: (orderId: string) => `/store/orders/${orderId}`,
    PAYMENT: "/store/payment",
  },
}

export const HTTP_STATUS = {
  OK: 200,
  CREATED: 201,
  NO_CONTENT: 204,
  BAD_REQUEST: 400,
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
  NOT_FOUND: 404,
  CONFLICT: 409,
  VALIDATION_ERROR: 422,
  INTERNAL_SERVER_ERROR: 500,
  SERVICE_UNAVAILABLE: 503,
}

export const ERROR_MESSAGES = {
  NETWORK_ERROR: "Network error. Please check your connection.",
  UNAUTHORIZED: "Please log in to continue.",
  FORBIDDEN: "You do not have permission to perform this action.",
  NOT_FOUND: "The requested resource was not found.",
  VALIDATION_ERROR: "Please check your input and try again.",
  INTERNAL_ERROR: "Something went wrong. Please try again later.",
  SERVICE_UNAVAILABLE: "Service is temporarily unavailable.",
  TIMEOUT: "Request timed out. Please try again.",
  UPLOAD_ERROR: "Failed to upload file. Please try again.",
  MAX_FILE_SIZE: "File is too large. Maximum size is 10MB.",
  INVALID_FILE_TYPE: "Invalid file type. Please select an image or video.",
}
