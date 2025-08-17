import { baseApi } from './baseApi';
import { Post, CreatePostRequest, Comment } from '@utils/types/post';

export interface PostsResponse {
  posts: Post[];
  hasMore: boolean;
  totalCount: number;
}

export interface CommentsResponse {
  comments: Comment[];
  hasMore: boolean;
  totalCount: number;
}

export const postsApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    // Feed endpoints
    getFeed: builder.query<PostsResponse, {page: number; limit?: number}>({
      query: ({ page, limit = 20 }) => `/posts/feed?page=${page}&limit=${limit}`,
      providesTags: ['Post'],
      serializeQueryArgs: ({ endpointName }) => endpointName,
      merge: (currentCache, newItems, { arg }) => {
        if (arg.page === 0) {
          return newItems;
        }
        return {
          ...newItems,
          posts: [...currentCache.posts, ...newItems.posts],
        };
      },
      forceRefetch({ currentArg, previousArg }) {
        return currentArg?.page !== previousArg?.page;
      },
    }),
    getFacultyFeed: builder.query<PostsResponse, {facultyId: string; page: number; limit?: number}>({
      query: ({ facultyId, page, limit = 20 }) => 
        `/posts/faculty/${facultyId}?page=${page}&limit=${limit}`,
      providesTags: ['Post'],
      serializeQueryArgs: ({ endpointName, queryArgs }) => `${endpointName}-${queryArgs.facultyId}`,
      merge: (currentCache, newItems, { arg }) => {
        if (arg.page === 0) {
          return newItems;
        }
        return {
          ...newItems,
          posts: [...currentCache.posts, ...newItems.posts],
        };
      },
    }),
    getTrendingPosts: builder.query<Post[], {timeframe?: 'day' | 'week' | 'month'}>({
      query: ({ timeframe = 'week' }) => `/posts/trending?timeframe=${timeframe}`,
      providesTags: ['Post'],
    }),
    getUserPosts: builder.query<PostsResponse, {userId: string; page: number; limit?: number}>({
      query: ({ userId, page, limit = 20 }) => 
        `/posts/user/${userId}?page=${page}&limit=${limit}`,
      providesTags: ['Post'],
    }),
    
    // Individual post endpoints
    getPost: builder.query<Post, string>({
      query: (postId) => `/posts/${postId}`,
      providesTags: (result, error, id) => [{ type: 'Post', id }],
    }),
    createPost: builder.mutation<Post, CreatePostRequest>({
      query: (newPost) => ({
        url: '/posts',
        method: 'POST',
        body: newPost,
      }),
      invalidatesTags: ['Post'],
    }),
    updatePost: builder.mutation<Post, {id: string; updates: Partial<CreatePostRequest>}>({
      query: ({ id, updates }) => ({
        url: `/posts/${id}`,
        method: 'PATCH',
        body: updates,
      }),
      invalidatesTags: (result, error, { id }) => [{ type: 'Post', id }],
    }),
    deletePost: builder.mutation<void, string>({
      query: (postId) => ({
        url: `/posts/${postId}`,
        method: 'DELETE',
      }),
      invalidatesTags: (result, error, id) => [{ type: 'Post', id }],
    }),
    
    // Interaction endpoints
    likePost: builder.mutation<{isLiked: boolean; likesCount: number}, string>({
      query: (postId) => ({
        url: `/posts/${postId}/like`,
        method: 'POST',
      }),
      invalidatesTags: (result, error, id) => [{ type: 'Post', id }],
      async onQueryStarted(postId, { dispatch, queryFulfilled }) {
        // Optimistic update
        const patchResult = dispatch(
          postsApi.util.updateQueryData('getPost', postId, (draft) => {
            draft.isLiked = !draft.isLiked;
            draft.likesCount += draft.isLiked ? 1 : -1;
          })
        );
        try {
          await queryFulfilled;
        } catch {
          patchResult.undo();
        }
      },
    }),
    unlikePost: builder.mutation<{isLiked: boolean; likesCount: number}, string>({
      query: (postId) => ({
        url: `/posts/${postId}/unlike`,
        method: 'POST',
      }),
      invalidatesTags: (result, error, id) => [{ type: 'Post', id }],
    }),
    
    // Comments endpoints
    getComments: builder.query<CommentsResponse, {postId: string; page: number; limit?: number}>({
      query: ({ postId, page, limit = 20 }) => 
        `/posts/${postId}/comments?page=${page}&limit=${limit}`,
      providesTags: (result, error, { postId }) => [{ type: 'Comment', id: postId }],
      serializeQueryArgs: ({ endpointName, queryArgs }) => `${endpointName}-${queryArgs.postId}`,
      merge: (currentCache, newItems, { arg }) => {
        if (arg.page === 0) {
          return newItems;
        }
        return {
          ...newItems,
          comments: [...currentCache.comments, ...newItems.comments],
        };
      },
    }),
    createComment: builder.mutation<Comment, {postId: string; content: string; parentId?: string}>({
      query: ({ postId, content, parentId }) => ({
        url: `/posts/${postId}/comments`,
        method: 'POST',
        body: { content, parentId },
      }),
      invalidatesTags: (result, error, { postId }) => [
        { type: 'Comment', id: postId },
        { type: 'Post', id: postId }
      ],
    }),
    updateComment: builder.mutation<Comment, {postId: string; commentId: string; content: string}>({
      query: ({ postId, commentId, content }) => ({
        url: `/posts/${postId}/comments/${commentId}`,
        method: 'PATCH',
        body: { content },
      }),
      invalidatesTags: (result, error, { postId }) => [{ type: 'Comment', id: postId }],
    }),
    deleteComment: builder.mutation<void, {postId: string; commentId: string}>({
      query: ({ postId, commentId }) => ({
        url: `/posts/${postId}/comments/${commentId}`,
        method: 'DELETE',
      }),
      invalidatesTags: (result, error, { postId }) => [{ type: 'Comment', id: postId }],
    }),
    
    // Search and discovery
    searchPosts: builder.query<PostsResponse, {query: string; filters?: any; page: number}>({
      query: ({ query, filters, page }) => ({
        url: '/posts/search',
        params: { q: query, ...filters, page },
      }),
      providesTags: ['Post'],
    }),
    
    // Media upload
    uploadMedia: builder.mutation<{urls: string[]}, FormData>({
      query: (formData) => ({
        url: '/media/upload',
        method: 'POST',
        body: formData,
      }),
    }),
  }),
});

export const {
  useGetFeedQuery,
  useGetFacultyFeedQuery,
  useGetTrendingPostsQuery,
  useGetUserPostsQuery,
  useGetPostQuery,
  useCreatePostMutation,
  useUpdatePostMutation,
  useDeletePostMutation,
  useLikePostMutation,
  useUnlikePostMutation,
  useGetCommentsQuery,
  useCreateCommentMutation,
  useUpdateCommentMutation,
  useDeleteCommentMutation,
  useSearchPostsQuery,
  useUploadMediaMutation,
} = postsApi;