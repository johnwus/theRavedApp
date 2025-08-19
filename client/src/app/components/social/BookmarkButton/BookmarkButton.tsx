import React, { useState, useMemo } from "react"
import { TouchableOpacity, ActivityIndicator } from "react-native"
import { Ionicons } from "@expo/vector-icons"

export type BookmarkButtonProps = {
  postId?: string
  isBookmarked?: boolean
  size?: number
  disabled?: boolean
  onToggle?: (nextBookmarked: boolean) => Promise<void> | void
}

const BookmarkButton: React.FC<BookmarkButtonProps> = ({
  postId,
  isBookmarked = false,
  size = 22,
  disabled,
  onToggle,
}) => {
  const [optimisticBookmarked, setOptimisticBookmarked] = useState<boolean>(isBookmarked)
  const [loading, setLoading] = useState(false)

  const color = useMemo(
    () => (optimisticBookmarked ? "#6C5CE7" : "#6b7280"),
    [optimisticBookmarked],
  )

  const handlePress = async () => {
    if (disabled || loading) return
    const next = !optimisticBookmarked
    setOptimisticBookmarked(next)
    setLoading(true)
    try {
      await onToggle?.(next)
      // If no onToggle is provided, we remain optimistic locally.
    } catch (e) {
      setOptimisticBookmarked(!next)
    } finally {
      setLoading(false)
    }
  }

  return (
    <TouchableOpacity onPress={handlePress} activeOpacity={0.7} disabled={disabled || loading}>
      {loading ? (
        <ActivityIndicator size="small" color={color} />
      ) : (
        <Ionicons
          name={optimisticBookmarked ? "bookmark" : "bookmark-outline"}
          size={size}
          color={color}
        />
      )}
    </TouchableOpacity>
  )
}

export default BookmarkButton
