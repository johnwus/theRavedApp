import React, { useMemo } from "react"
import { View, Text, TouchableOpacity } from "react-native"

type MinimalUser = {
  id: string
  name: string
  username?: string
  avatarUrl?: string
  verified?: boolean
}

export type PostHeaderProps = {
  user: MinimalUser
  createdAt: string | number | Date
  facultyName?: string
  location?: string
  onUserPress?: () => void
  onOptionsPress?: () => void
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

const PostHeader: React.FC<PostHeaderProps> = ({
  user,
  createdAt,
  facultyName,
  location,
  onUserPress,
  onOptionsPress,
}) => {
  const time = useMemo(() => formatShortTime(createdAt), [createdAt])

  return (
    <View
      style={{
        paddingHorizontal: 16,
        paddingVertical: 12,
        flexDirection: "row",
        alignItems: "center",
        gap: 12,
      }}
    >
      <TouchableOpacity onPress={onUserPress} activeOpacity={0.7}>
        <View style={{ width: 40, height: 40, borderRadius: 20, backgroundColor: "#e5e7eb" }} />
      </TouchableOpacity>
      <View style={{ flex: 1 }}>
        <TouchableOpacity onPress={onUserPress} activeOpacity={0.7}>
          <Text style={{ fontSize: 16, fontWeight: "600", color: "#111214" }}>{user.name}</Text>
        </TouchableOpacity>
        <Text style={{ marginTop: 2, fontSize: 12, color: "#6b7280" }} numberOfLines={1}>
          {facultyName ? `${facultyName}` : ""}
          {facultyName && location ? " • " : ""}
          {location ? `${location}` : ""}
          {facultyName || location ? " • " : ""}
          {time}
        </Text>
      </View>
      <TouchableOpacity onPress={onOptionsPress} activeOpacity={0.7}>
        <Text style={{ fontSize: 20, color: "#6b7280" }}>⋯</Text>
      </TouchableOpacity>
    </View>
  )
}

export default PostHeader
