import React, { useMemo, useState } from "react"
import { View, Text, TouchableOpacity, Dimensions } from "react-native"
import { Ionicons } from "@expo/vector-icons"
import { useNavigation } from "@react-navigation/native"

import PostActions from "./PostActions"
import { postCardStyles as styles } from "./PostCard.styles"
import PostHeader from "./PostHeader"
import { useAppDispatch, useAppSelector } from "../../../store"
import { toggleLike } from "../../../store/slices/postsSlice"
import MediaCarousel from "../../media/MediaCarousel/MediaCarousel"

const { width: screenWidth } = Dimensions.get("window")

type MinimalUser = {
  id: string
  name: string
  username?: string
  avatarUrl?: string
  verified?: boolean
}

type Post = {
  id: string
  user: MinimalUser
  content?: string
  createdAt: string | number | Date
  facultyName?: string
  location?: string
  mediaUrls: string[]
  mediaType?: "image" | "video"
  tags: string[]
  isForSale?: boolean
  price?: number
  currency?: string
  productDetails?: { isNegotiable?: boolean }
  isLiked?: boolean
  likesCount: number
  commentsCount: number
  viewsCount?: number
}

export type PostCardProps = {
  post: Post
  showActions?: boolean
  onPress?: () => void
}

const PostCard: React.FC<PostCardProps> = ({ post, showActions = true, onPress }) => {
  const navigation = useNavigation<any>()
  const dispatch = useAppDispatch()
  const isDarkMode = useAppSelector((state: any) => state.ui?.isDarkMode) ?? false

  const [imageHeight, setImageHeight] = useState<number>(400)

  const theme = useMemo(() => {
    return {
      surface: isDarkMode ? "#111214" : "#ffffff",
      text: isDarkMode ? "#f7f7f7" : "#111214",
      textSecondary: isDarkMode ? "#9aa0a6" : "#5f6368",
      primary: "#6C5CE7",
      success: "#22c55e",
    }
  }, [isDarkMode])

  const handleNavigateToPost = () => {
    if (onPress) return onPress()
    navigation.navigate("PostDetail", { postId: post.id })
  }

  const handleUserPress = () => {
    navigation.navigate("UserProfile", { userId: post.user.id })
  }

  const handleCommentsPress = () => {
    navigation.navigate("Comments", { postId: post.id })
  }

  const handleLike = () => {
    dispatch(
      toggleLike({
        postId: post.id,
        isLiked: !post.isLiked,
      }),
    )
  }

  const handleImageLoad = (event: any) => {
    const { width, height } = event?.nativeEvent?.source ?? { width: 1, height: 1 }
    const aspectRatio = height / width
    const newHeight = Math.min(screenWidth * aspectRatio, 500)
    setImageHeight(newHeight)
  }

  const formatNumber = (num?: number) => {
    if (typeof num !== "number") return "0"
    if (num >= 1_000_000) return `${(num / 1_000_000).toFixed(1)}M`
    if (num >= 1_000) return `${(num / 1_000).toFixed(1)}K`
    return `${num}`
  }

  return (
    <View style={[styles.container, { backgroundColor: theme.surface }]}>
      <PostHeader
        user={post.user}
        createdAt={post.createdAt}
        facultyName={post.facultyName}
        location={post.location}
        onUserPress={handleUserPress}
        onOptionsPress={() => {}}
      />

      <TouchableOpacity onPress={handleNavigateToPost} activeOpacity={0.9}>
        {!!post.content && (
          <View style={styles.contentContainer}>
            <Text style={[styles.content, { color: theme.text }]} numberOfLines={3}>
              {post.content}
            </Text>
          </View>
        )}

        {post.mediaUrls?.length > 0 && (
          <MediaCarousel
            mediaUrls={post.mediaUrls}
            mediaType={post.mediaType}
            onImageLoad={handleImageLoad}
            height={imageHeight}
          />
        )}

        {post.tags?.length > 0 && (
          <View style={styles.tagsContainer}>
            {post.tags.slice(0, 3).map((tag) => (
              <View key={tag} style={[styles.tag, { backgroundColor: "#6C5CE733" }]}>
                <Text style={[styles.tagText, { color: theme.primary }]}>#{tag}</Text>
              </View>
            ))}
            {post.tags.length > 3 && (
              <Text style={[styles.moreTagsText, { color: theme.textSecondary }]}>
                +{post.tags.length - 3} more
              </Text>
            )}
          </View>
        )}

        {post.isForSale && post.price != null && (
          <View style={styles.priceContainer}>
            <View style={[styles.priceTag, { backgroundColor: theme.success }]}>
              <Ionicons name="pricetag" size={16} color="white" />
              <Text style={styles.priceText}>
                ${post.price} {post.currency || "USD"}
              </Text>
            </View>
            {post.productDetails?.isNegotiable && (
              <Text style={[styles.negotiableText, { color: theme.textSecondary }]}>
                Negotiable
              </Text>
            )}
          </View>
        )}
      </TouchableOpacity>

      {showActions && (
        <PostActions
          postId={post.id}
          isLiked={!!post.isLiked}
          likesCount={post.likesCount}
          commentsCount={post.commentsCount}
          onLike={handleLike}
          onComment={handleCommentsPress}
          onShare={() => {}}
          onBookmark={() => {}}
        />
      )}

      <View style={styles.engagementContainer}>
        <Text style={[styles.engagementText, { color: theme.textSecondary }]}>
          {formatNumber(post.likesCount)} likes • {formatNumber(post.commentsCount)} comments
        </Text>
        {post.viewsCount ? (
          <Text style={[styles.viewsText, { color: theme.textSecondary }]}>
            {formatNumber(post.viewsCount)} views
          </Text>
        ) : null}
      </View>
    </View>
  )
}

export default PostCard
