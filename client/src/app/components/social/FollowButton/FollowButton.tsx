import React, { useMemo, useState } from "react"
import { TouchableOpacity, Text, ActivityIndicator } from "react-native"

import { useFollowUserMutation, useUnfollowUserMutation } from "../../../store/api/userApi"

export type FollowButtonProps = {
  userId?: string
  isFollowing?: boolean
  small?: boolean
  onToggle?: (nextFollowing: boolean) => Promise<void> | void
}

const FollowButton: React.FC<FollowButtonProps> = ({
  userId,
  isFollowing = false,
  small = false,
  onToggle,
}) => {
  const [followUser, { isLoading: following }] = useFollowUserMutation()
  const [unfollowUser, { isLoading: unfollowing }] = useUnfollowUserMutation()
  const [optimisticFollowing, setOptimisticFollowing] = useState<boolean>(isFollowing)
  const loading = following || unfollowing

  const label = useMemo(() => (optimisticFollowing ? "Following" : "Follow"), [optimisticFollowing])
  const bg = optimisticFollowing ? "#e5e7eb" : "#6C5CE7"
  const color = optimisticFollowing ? "#111214" : "#ffffff"
  const paddingV = small ? 6 : 10
  const paddingH = small ? 12 : 16
  const fontSize = small ? 12 : 14
  const borderRadius = 999

  const handlePress = async () => {
    if (loading) return
    const next = !optimisticFollowing
    setOptimisticFollowing(next)
    try {
      if (onToggle) {
        await onToggle(next)
      } else if (userId) {
        if (next) await followUser(userId).unwrap()
        else await unfollowUser(userId).unwrap()
      }
    } catch (e) {
      setOptimisticFollowing(!next)
    }
  }

  return (
    <TouchableOpacity
      onPress={handlePress}
      activeOpacity={0.7}
      disabled={loading}
      style={{
        backgroundColor: bg,
        paddingVertical: paddingV,
        paddingHorizontal: paddingH,
        borderRadius,
      }}
    >
      {loading ? (
        <ActivityIndicator size="small" color={color} />
      ) : (
        <Text style={{ color, fontSize, fontWeight: "600" }}>{label}</Text>
      )}
    </TouchableOpacity>
  )
}

export default FollowButton
