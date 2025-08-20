export interface Post {
  id: string
  userId: string
  user: {
    id: string
    displayName: string
    avatar?: string
    isVerified: boolean
  }
  content: string
  mediaUrls: string[]
  mediaType: "image" | "video" | "mixed"
  tags: string[]
  facultyId?: string
  facultyName?: string
  location?: string
  likesCount: number
  commentsCount: number
  sharesCount: number
  viewsCount: number
  isLiked: boolean
  isBookmarked: boolean
  isFollowing: boolean
  engagement: {
    likes: number
    comments: number
    shares: number
    views: number
    saves: number
  }
  createdAt: string
  updatedAt: string

  // For store posts
  isForSale?: boolean
  price?: number
  currency?: string
  productDetails?: ProductDetails
}

export interface ProductDetails {
  condition: "new" | "like-new" | "good" | "fair"
  size?: string
  brand?: string
  category: string
  isNegotiable: boolean
  shipping: {
    available: boolean
    cost?: number
    methods: string[]
  }
}

export interface CreatePostRequest {
  content: string
  mediaUrls?: string[]
  tags?: string[]
  facultyId?: string
  location?: string
  isForSale?: boolean
  price?: number
  productDetails?: ProductDetails
}

export interface Comment {
  id: string
  postId: string
  userId: string
  user: {
    id: string
    displayName: string
    avatar?: string
    isVerified: boolean
  }
  content: string
  parentId?: string
  repliesCount: number
  likesCount: number
  isLiked: boolean
  createdAt: string
  updatedAt: string
  replies?: Comment[]
}

export interface PostInteraction {
  type: "like" | "comment" | "share" | "view" | "save"
  postId: string
  userId: string
  createdAt: string
}

export interface PostAnalytics {
  postId: string
  impressions: number
  reaches: number
  engagement: {
    likes: number
    comments: number
    shares: number
    saves: number
    clicks: number
  }
  demographics: {
    faculty: { [key: string]: number }
    year: { [key: string]: number }
    gender: { [key: string]: number }
  }
  timeline: Array<{
    timestamp: string
    views: number
    engagement: number
  }>
}
