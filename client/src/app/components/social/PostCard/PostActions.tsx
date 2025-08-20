import React from "react"
import { View, Text, TouchableOpacity } from "react-native"
import { Ionicons } from "@expo/vector-icons"

import BookmarkButton from "../BookmarkButton/BookmarkButton"
import LikeButton from "../LikeButton/LikeButton"
import ShareButton from "../ShareButton/ShareButton"

export type PostActionsProps = {
  isLiked: boolean
  likesCount: number
  commentsCount: number
  isLiking?: boolean
  postId?: string
  onLike: () => void
  onComment: () => void
  onShare: () => void
  onBookmark: () => void
}

const formatNumber = (num?: number) => {
  if (typeof num !== "number") return "0"
  if (num >= 1_000_000) return `${(num / 1_000_000).toFixed(1)}M`
  if (num >= 1_000) return `${(num / 1_000).toFixed(1)}K`
  return `${num}`
}

const PostActions: React.FC<PostActionsProps> = ({
  isLiked,
  likesCount,
  commentsCount,
  isLiking,
  postId,
  onLike,
  onComment,
  onShare,
  onBookmark,
}) => {
  return (
    <View style={{ paddingHorizontal: 12, paddingVertical: 8 }}>
      <View style={{ flexDirection: "row", alignItems: "center", gap: 16 }}>
        <LikeButton
          postId={postId}
          isLiked={isLiked}
          count={likesCount}
          onToggle={() => onLike()}
        />

        <TouchableOpacity onPress={onComment} activeOpacity={0.7}>
          <View style={{ flexDirection: "row", alignItems: "center", gap: 6 }}>
            <Ionicons name={"chatbubble-ellipses-outline"} size={22} color={"#6b7280"} />
            <Text style={{ color: "#6b7280", fontSize: 14 }}>{formatNumber(commentsCount)}</Text>
          </View>
        </TouchableOpacity>

        <ShareButton small />

        <View style={{ flex: 1 }} />

        <BookmarkButton onToggle={() => onBookmark()} />
      </View>
    </View>
  )
}

export default PostActions
