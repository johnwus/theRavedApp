import React, { useMemo, useState } from "react"
import { TouchableOpacity, View, Text, ActivityIndicator } from "react-native"
import { Ionicons } from "@expo/vector-icons"

import { useLikePostMutation, useUnlikePostMutation } from "../../../store/api/postsApi"

export type LikeButtonProps = {
  postId?: string
  isLiked?: boolean
  count?: number
  size?: number
  disabled?: boolean
  onToggle?: (nextLiked: boolean) => Promise<void> | void
}

const formatNumber = (num?: number) => {
  if (typeof num !== "number") return "0"
  if (num >= 1_000_000) return `${(num / 1_000_000).toFixed(1)}M`
  if (num >= 1_000) return `${(num / 1_000).toFixed(1)}K`
  return `${num}`
}

const LikeButton: React.FC<LikeButtonProps> = ({
  postId,
  isLiked = false,
  count = 0,
  size = 22,
  disabled,
  onToggle,
}) => {
  const [likePost, { isLoading: liking }] = useLikePostMutation()
  const [unlikePost, { isLoading: unliking }] = useUnlikePostMutation()
  const [optimisticLiked, setOptimisticLiked] = useState<boolean>(isLiked)
  const [optimisticCount, setOptimisticCount] = useState<number>(count)

  const loading = liking || unliking

  const color = useMemo(() => (optimisticLiked ? "#ef4444" : "#6b7280"), [optimisticLiked])

  const handlePress = async () => {
    if (disabled || loading) return
    const next = !optimisticLiked
    // optimistic UI
    setOptimisticLiked(next)
    setOptimisticCount((c) => c + (next ? 1 : -1))

    try {
      if (onToggle) {
        await onToggle(next)
      } else if (postId) {
        if (next) {
          await likePost(postId).unwrap()
        } else {
          await unlikePost(postId).unwrap()
        }
      }
    } catch (e) {
      // revert on error
      setOptimisticLiked(!next)
      setOptimisticCount((c) => c + (next ? -1 : 1))
    }
  }

  return (
    <TouchableOpacity onPress={handlePress} activeOpacity={0.7} disabled={disabled || loading}>
      <View style={{ flexDirection: "row", alignItems: "center", gap: 6 }}>
        {loading ? (
          <ActivityIndicator size="small" color={color} />
        ) : (
          <Ionicons name={optimisticLiked ? "heart" : "heart-outline"} size={size} color={color} />
        )}
        <Text style={{ color: "#6b7280", fontSize: 14 }}>{formatNumber(optimisticCount)}</Text>
      </View>
    </TouchableOpacity>
  )
}

export default LikeButton
