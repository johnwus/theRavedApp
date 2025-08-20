import React from "react"
import { View, Text, TouchableOpacity } from "react-native"

export type CommentUser = {
  id: string
  name: string
  avatarUrl?: string
}

export type CommentCardProps = {
  id: string
  user: CommentUser
  content: string
  createdAt: string | number | Date
  onPressUser?: (userId: string) => void
  onLongPress?: (commentId: string) => void
}

const formatShortTime = (dateLike: string | number | Date) => {
  const d = new Date(dateLike)
  if (Number.isNaN(d.getTime())) return ""
  const now = Date.now()
  const diffSec = Math.floor((now - d.getTime()) / 1000)
  if (diffSec < 60) return `${diffSec}s`
  const diffMin = Math.floor(diffSec / 60)
  if (diffMin < 60) return `${diffMin}m`
  const diffHr = Math.floor(diffMin / 60)
  if (diffHr < 24) return `${diffHr}h`
  const diffDay = Math.floor(diffHr / 24)
  return `${diffDay}d`
}

const CommentCard: React.FC<CommentCardProps> = ({
  id,
  user,
  content,
  createdAt,
  onPressUser,
  onLongPress,
}) => {
  return (
    <TouchableOpacity
      onLongPress={() => onLongPress?.(id)}
      activeOpacity={0.8}
      style={{ paddingHorizontal: 16, paddingVertical: 12, flexDirection: "row", gap: 12 }}
    >
      <TouchableOpacity onPress={() => onPressUser?.(user.id)} activeOpacity={0.7}>
        <View style={{ width: 32, height: 32, borderRadius: 16, backgroundColor: "#e5e7eb" }} />
      </TouchableOpacity>
      <View style={{ flex: 1 }}>
        <View style={{ flexDirection: "row", alignItems: "baseline", gap: 8 }}>
          <TouchableOpacity onPress={() => onPressUser?.(user.id)} activeOpacity={0.7}>
            <Text style={{ fontSize: 14, fontWeight: "600", color: "#111214" }}>{user.name}</Text>
          </TouchableOpacity>
          <Text style={{ fontSize: 12, color: "#6b7280" }}>{formatShortTime(createdAt)}</Text>
        </View>
        <Text style={{ marginTop: 2, fontSize: 14, color: "#374151" }}>{content}</Text>
      </View>
    </TouchableOpacity>
  )
}

export default CommentCard
