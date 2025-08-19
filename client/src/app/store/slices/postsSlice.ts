import { createSlice, PayloadAction } from "@reduxjs/toolkit"

interface MediaItem {
  id: string
  type: "image" | "video"
  url: string
  thumbnailUrl?: string
  width: number
  height: number
  duration?: number // for videos
}

interface Post {
  id: string
  author: {
    id: string
    firstName: string
    lastName: string
    profileImage?: string
    faculty: {
      id: string
      name: string
    }
    isVerified: boolean
  }
  content?: string
  media: MediaItem[]
  tags: string[]
  faculty: {
    id: string
    name: string
  }
  stats: {
    likesCount: number
    commentsCount: number
    sharesCount: number
    viewsCount: number
  }
  interactions: {
    isLiked: boolean
    isBookmarked: boolean
    isShared: boolean
  }
  createdAt: string
  updatedAt: string
  isForSale?: boolean
  price?: number
  currency?: string
  location?: string
}

interface CreatePostData {
  content?: string
  media: File[] | string[]
  tags: string[]
  isForSale?: boolean
  price?: number
  currency?: string
}

interface PostsState {
  // Feed data
  homeFeed: Post[]
  facultyFeed: Post[]
  exploreFeed: Post[]
  trendingPosts: Post[]

  // User posts
  myPosts: Post[]

  // Post detail
  currentPost: Post | null

  // Loading states
  isLoadingHomeFeed: boolean
  isLoadingFacultyFeed: boolean
  isLoadingExploreFeed: boolean
  isLoadingTrendingPosts: boolean
  isLoadingMyPosts: boolean
  isLoadingCurrentPost: boolean

  // Pagination
  homeFeedHasMore: boolean
  facultyFeedHasMore: boolean
  exploreFeedHasMore: boolean
  myPostsHasMore: boolean

  // Create post
  isCreatingPost: boolean
  createPostProgress: number

  // Interactions
  pendingLikes: Set<string>
  pendingBookmarks: Set<string>

  // Drafts
  drafts: CreatePostData[]

  // Search
  searchResults: Post[]
  searchQuery: string
  isSearching: boolean

  // Trending tags
  trendingTags: string[]

  // Filters
  activeFilters: {
    faculty?: string
    tags?: string[]
    dateRange?: {
      start: string
      end: string
    }
    hasMedia: boolean
    forSale: boolean
  }
}

const initialState: PostsState = {
  homeFeed: [],
  facultyFeed: [],
  exploreFeed: [],
  trendingPosts: [],
  myPosts: [],
  currentPost: null,

  isLoadingHomeFeed: false,
  isLoadingFacultyFeed: false,
  isLoadingExploreFeed: false,
  isLoadingTrendingPosts: false,
  isLoadingMyPosts: false,
  isLoadingCurrentPost: false,

  homeFeedHasMore: true,
  facultyFeedHasMore: true,
  exploreFeedHasMore: true,
  myPostsHasMore: true,

  isCreatingPost: false,
  createPostProgress: 0,

  pendingLikes: new Set(),
  pendingBookmarks: new Set(),

  drafts: [],

  searchResults: [],
  searchQuery: "",
  isSearching: false,

  trendingTags: [],

  activeFilters: {
    hasMedia: false,
    forSale: false,
  },
}

const postsSlice = createSlice({
  name: "posts",
  initialState,
  reducers: {
    // Feed actions
    setHomeFeed: (state, action: PayloadAction<{ posts: Post[]; hasMore: boolean }>) => {
      state.homeFeed = action.payload.posts
      state.homeFeedHasMore = action.payload.hasMore
      state.isLoadingHomeFeed = false
    },

    appendHomeFeed: (state, action: PayloadAction<{ posts: Post[]; hasMore: boolean }>) => {
      state.homeFeed.push(...action.payload.posts)
      state.homeFeedHasMore = action.payload.hasMore
      state.isLoadingHomeFeed = false
    },

    setFacultyFeed: (state, action: PayloadAction<{ posts: Post[]; hasMore: boolean }>) => {
      state.facultyFeed = action.payload.posts
      state.facultyFeedHasMore = action.payload.hasMore
      state.isLoadingFacultyFeed = false
    },

    appendFacultyFeed: (state, action: PayloadAction<{ posts: Post[]; hasMore: boolean }>) => {
      state.facultyFeed.push(...action.payload.posts)
      state.facultyFeedHasMore = action.payload.hasMore
      state.isLoadingFacultyFeed = false
    },

    setExploreFeed: (state, action: PayloadAction<{ posts: Post[]; hasMore: boolean }>) => {
      state.exploreFeed = action.payload.posts
      state.exploreFeedHasMore = action.payload.hasMore
      state.isLoadingExploreFeed = false
    },

    appendExploreFeed: (state, action: PayloadAction<{ posts: Post[]; hasMore: boolean }>) => {
      state.exploreFeed.push(...action.payload.posts)
      state.exploreFeedHasMore = action.payload.hasMore
      state.isLoadingExploreFeed = false
    },

    setTrendingPosts: (state, action: PayloadAction<Post[]>) => {
      state.trendingPosts = action.payload
      state.isLoadingTrendingPosts = false
    },

    // User posts
    setMyPosts: (state, action: PayloadAction<{ posts: Post[]; hasMore: boolean }>) => {
      state.myPosts = action.payload.posts
      state.myPostsHasMore = action.payload.hasMore
      state.isLoadingMyPosts = false
    },

    appendMyPosts: (state, action: PayloadAction<{ posts: Post[]; hasMore: boolean }>) => {
      state.myPosts.push(...action.payload.posts)
      state.myPostsHasMore = action.payload.hasMore
      state.isLoadingMyPosts = false
    },

    // Current post
    setCurrentPost: (state, action: PayloadAction<Post>) => {
      state.currentPost = action.payload
      state.isLoadingCurrentPost = false
    },

    // Post interactions
    toggleLike: (state, action: PayloadAction<string>) => {
      const postId = action.payload
      state.pendingLikes.add(postId)

      const updatePost = (post: Post) => {
        if (post.id === postId) {
          post.interactions.isLiked = !post.interactions.isLiked
          post.stats.likesCount += post.interactions.isLiked ? 1 : -1
        }
      }

      state.homeFeed.forEach(updatePost)
      state.facultyFeed.forEach(updatePost)
      state.exploreFeed.forEach(updatePost)
      state.trendingPosts.forEach(updatePost)
      state.myPosts.forEach(updatePost)
      if (state.currentPost?.id === postId) {
        updatePost(state.currentPost)
      }
    },

    toggleBookmark: (state, action: PayloadAction<string>) => {
      const postId = action.payload
      state.pendingBookmarks.add(postId)

      const updatePost = (post: Post) => {
        if (post.id === postId) {
          post.interactions.isBookmarked = !post.interactions.isBookmarked
        }
      }

      state.homeFeed.forEach(updatePost)
      state.facultyFeed.forEach(updatePost)
      state.exploreFeed.forEach(updatePost)
      state.trendingPosts.forEach(updatePost)
      state.myPosts.forEach(updatePost)
      if (state.currentPost?.id === postId) {
        updatePost(state.currentPost)
      }
    },

    updatePostStats: (
      state,
      action: PayloadAction<{ postId: string; stats: Partial<Post["stats"]> }>,
    ) => {
      const { postId, stats } = action.payload

      const updatePost = (post: Post) => {
        if (post.id === postId) {
          post.stats = { ...post.stats, ...stats }
        }
      }

      state.homeFeed.forEach(updatePost)
      state.facultyFeed.forEach(updatePost)
      state.exploreFeed.forEach(updatePost)
      state.trendingPosts.forEach(updatePost)
      state.myPosts.forEach(updatePost)
      if (state.currentPost?.id === postId) {
        updatePost(state.currentPost)
      }
    },

    // Create post
    setCreatePostProgress: (state, action: PayloadAction<number>) => {
      state.createPostProgress = action.payload
    },

    setIsCreatingPost: (state, action: PayloadAction<boolean>) => {
      state.isCreatingPost = action.payload
      if (!action.payload) {
        state.createPostProgress = 0
      }
    },

    addNewPost: (state, action: PayloadAction<Post>) => {
      state.homeFeed.unshift(action.payload)
      state.facultyFeed.unshift(action.payload)
      state.myPosts.unshift(action.payload)
      state.isCreatingPost = false
      state.createPostProgress = 0
    },

    // Drafts
    saveDraft: (state, action: PayloadAction<CreatePostData>) => {
      state.drafts.push(action.payload)
    },

    removeDraft: (state, action: PayloadAction<number>) => {
      state.drafts.splice(action.payload, 1)
    },

    clearDrafts: (state) => {
      state.drafts = []
    },

    // Search
    setSearchResults: (state, action: PayloadAction<Post[]>) => {
      state.searchResults = action.payload
      state.isSearching = false
    },

    setSearchQuery: (state, action: PayloadAction<string>) => {
      state.searchQuery = action.payload
    },

    setIsSearching: (state, action: PayloadAction<boolean>) => {
      state.isSearching = action.payload
    },

    clearSearchResults: (state) => {
      state.searchResults = []
      state.searchQuery = ""
      state.isSearching = false
    },

    // Trending tags
    setTrendingTags: (state, action: PayloadAction<string[]>) => {
      state.trendingTags = action.payload
    },

    // Filters
    setActiveFilters: (state, action: PayloadAction<Partial<PostsState["activeFilters"]>>) => {
      state.activeFilters = { ...state.activeFilters, ...action.payload }
    },

    clearFilters: (state) => {
      state.activeFilters = {
        hasMedia: false,
        forSale: false,
      }
    },

    // Loading states
    setHomeFeedLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoadingHomeFeed = action.payload
    },

    setFacultyFeedLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoadingFacultyFeed = action.payload
    },

    setExploreFeedLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoadingExploreFeed = action.payload
    },

    setTrendingPostsLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoadingTrendingPosts = action.payload
    },

    setMyPostsLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoadingMyPosts = action.payload
    },

    setCurrentPostLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoadingCurrentPost = action.payload
    },

    // Clear pending interactions
    clearPendingLike: (state, action: PayloadAction<string>) => {
      state.pendingLikes.delete(action.payload)
    },

    clearPendingBookmark: (state, action: PayloadAction<string>) => {
      state.pendingBookmarks.delete(action.payload)
    },

    // Delete post
    deletePost: (state, action: PayloadAction<string>) => {
      const postId = action.payload
      state.homeFeed = state.homeFeed.filter((p) => p.id !== postId)
      state.facultyFeed = state.facultyFeed.filter((p) => p.id !== postId)
      state.exploreFeed = state.exploreFeed.filter((p) => p.id !== postId)
      state.trendingPosts = state.trendingPosts.filter((p) => p.id !== postId)
      state.myPosts = state.myPosts.filter((p) => p.id !== postId)
      state.searchResults = state.searchResults.filter((p) => p.id !== postId)

      if (state.currentPost?.id === postId) {
        state.currentPost = null
      }
    },

    // Update post
    updatePost: (state, action: PayloadAction<Post>) => {
      const updatedPost = action.payload

      const updatePostInArray = (posts: Post[]) => {
        const index = posts.findIndex((p) => p.id === updatedPost.id)
        if (index !== -1) {
          posts[index] = updatedPost
        }
      }

      updatePostInArray(state.homeFeed)
      updatePostInArray(state.facultyFeed)
      updatePostInArray(state.exploreFeed)
      updatePostInArray(state.trendingPosts)
      updatePostInArray(state.myPosts)
      updatePostInArray(state.searchResults)

      if (state.currentPost?.id === updatedPost.id) {
        state.currentPost = updatedPost
      }
    },
  },
})

export const {
  setHomeFeed,
  appendHomeFeed,
  setFacultyFeed,
  appendFacultyFeed,
  setExploreFeed,
  appendExploreFeed,
  setTrendingPosts,
  setMyPosts,
  appendMyPosts,
  setCurrentPost,
  toggleLike,
  toggleBookmark,
  updatePostStats,
  setCreatePostProgress,
  setIsCreatingPost,
  addNewPost,
  saveDraft,
  removeDraft,
  clearDrafts,
  setSearchResults,
  setSearchQuery,
  setIsSearching,
  clearSearchResults,
  setTrendingTags,
  setActiveFilters,
  clearFilters,
  setHomeFeedLoading,
  setFacultyFeedLoading,
  setExploreFeedLoading,
  setTrendingPostsLoading,
  setMyPostsLoading,
  setCurrentPostLoading,
  clearPendingLike,
  clearPendingBookmark,
  deletePost,
  updatePost,
} = postsSlice.actions

export default postsSlice.reducer
