import { User, Faculty, UserStats } from "@utils/types/user"

import { baseApi } from "./baseApi"

export interface UpdateProfileRequest {
  firstName?: string
  lastName?: string
  displayName?: string
  bio?: string
  avatar?: string
  year?: number
  major?: string
}

export interface SearchUsersResponse {
  users: User[]
  hasMore: boolean
  totalCount: number
}

export const userApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    // Profile management
    getProfile: builder.query<User, string | void>({
      query: (userId) => (userId ? `/users/${userId}` : "/users/profile"),
      providesTags: (result, error, userId) => (userId ? [{ type: "User", id: userId }] : ["User"]),
    }),
    updateProfile: builder.mutation<User, UpdateProfileRequest>({
      query: (updates) => ({
        url: "/users/profile",
        method: "PATCH",
        body: updates,
      }),
      invalidatesTags: ["User"],
    }),
    uploadAvatar: builder.mutation<{ avatarUrl: string }, FormData>({
      query: (formData) => ({
        url: "/users/avatar",
        method: "POST",
        body: formData,
      }),
      invalidatesTags: ["User"],
    }),

    // Social connections
    getFollowers: builder.query<SearchUsersResponse, { userId?: string; page: number }>({
      query: ({ userId, page }) => ({
        url: userId ? `/users/${userId}/followers` : "/users/followers",
        params: { page },
      }),
      providesTags: ["Follow"],
    }),
    getFollowing: builder.query<SearchUsersResponse, { userId?: string; page: number }>({
      query: ({ userId, page }) => ({
        url: userId ? `/users/${userId}/following` : "/users/following",
        params: { page },
      }),
      providesTags: ["Follow"],
    }),
    getSuggestedUsers: builder.query<User[], void>({
      query: () => "/users/suggestions",
      providesTags: ["User"],
    }),
    followUser: builder.mutation<{ isFollowing: boolean; followersCount: number }, string>({
      query: (userId) => ({
        url: `/users/${userId}/follow`,
        method: "POST",
      }),
      invalidatesTags: (result, error, userId) => [{ type: "User", id: userId }, "Follow"],
      async onQueryStarted(userId, { dispatch, queryFulfilled }) {
        // Optimistic update
        const patchResult = dispatch(
          userApi.util.updateQueryData("getProfile", userId, (draft) => {
            draft.isFollowing = true
            draft.followersCount += 1
          }),
        )
        try {
          await queryFulfilled
        } catch {
          patchResult.undo()
        }
      },
    }),
    unfollowUser: builder.mutation<{ isFollowing: boolean; followersCount: number }, string>({
      query: (userId) => ({
        url: `/users/${userId}/unfollow`,
        method: "POST",
      }),
      invalidatesTags: (result, error, userId) => [{ type: "User", id: userId }, "Follow"],
    }),

    // Search and discovery
    searchUsers: builder.query<SearchUsersResponse, { query: string; filters?: any; page: number }>(
      {
        query: ({ query, filters, page }) => ({
          url: "/users/search",
          params: { q: query, ...filters, page },
        }),
        providesTags: ["User"],
      },
    ),
    getUsersByFaculty: builder.query<SearchUsersResponse, { facultyId: string; page: number }>({
      query: ({ facultyId, page }) => ({
        url: `/users/faculty/${facultyId}`,
        params: { page },
      }),
      providesTags: ["User"],
    }),

    // Faculty management
    getFaculties: builder.query<Faculty[], void>({
      query: () => "/faculties",
      providesTags: ["Faculty"],
    }),
    getFaculty: builder.query<Faculty, string>({
      query: (facultyId) => `/faculties/${facultyId}`,
      providesTags: (result, error, id) => [{ type: "Faculty", id }],
    }),

    // Analytics and stats
    getUserStats: builder.query<UserStats, string | void>({
      query: (userId) => (userId ? `/users/${userId}/stats` : "/users/stats"),
      providesTags: ["Analytics"],
    }),

    // User actions
    blockUser: builder.mutation<void, string>({
      query: (userId) => ({
        url: `/users/${userId}/block`,
        method: "POST",
      }),
      invalidatesTags: ["User"],
    }),
    unblockUser: builder.mutation<void, string>({
      query: (userId) => ({
        url: `/users/${userId}/unblock`,
        method: "POST",
      }),
      invalidatesTags: ["User"],
    }),
    reportUser: builder.mutation<void, { userId: string; reason: string; description?: string }>({
      query: ({ userId, reason, description }) => ({
        url: `/users/${userId}/report`,
        method: "POST",
        body: { reason, description },
      }),
    }),

    // Preferences
    updatePreferences: builder.mutation<User, Partial<User["preferences"]>>({
      query: (preferences) => ({
        url: "/users/preferences",
        method: "PATCH",
        body: preferences,
      }),
      invalidatesTags: ["User"],
    }),
  }),
})

export const {
  useGetProfileQuery,
  useUpdateProfileMutation,
  useUploadAvatarMutation,
  useGetFollowersQuery,
  useGetFollowingQuery,
  useGetSuggestedUsersQuery,
  useFollowUserMutation,
  useUnfollowUserMutation,
  useSearchUsersQuery,
  useGetUsersByFacultyQuery,
  useGetFacultiesQuery,
  useGetFacultyQuery,
  useGetUserStatsQuery,
  useBlockUserMutation,
  useUnblockUserMutation,
  useReportUserMutation,
  useUpdatePreferencesMutation,
} = userApi
