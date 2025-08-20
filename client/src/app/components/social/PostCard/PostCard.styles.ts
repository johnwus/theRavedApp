import { StyleSheet } from "react-native"

export const postCardStyles = StyleSheet.create({
  container: {
    borderRadius: 16,
    elevation: 2,
    marginBottom: 12,
    overflow: "hidden",
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
  },
  content: {
    fontFamily: "Inter-Regular",
    fontSize: 16,
    lineHeight: 22,
  },
  contentContainer: {
    paddingBottom: 12,
    paddingHorizontal: 16,
  },
  engagementContainer: {
    alignItems: "center",
    flexDirection: "row",
    justifyContent: "space-between",
    paddingBottom: 16,
    paddingHorizontal: 16,
  },
  engagementText: {
    fontFamily: "Inter-Regular",
    fontSize: 14,
  },
  moreTagsText: {
    alignSelf: "center",
    fontFamily: "Inter-Regular",
    fontSize: 12,
  },
  negotiableText: {
    fontFamily: "Inter-Regular",
    fontSize: 12,
  },
  priceContainer: {
    alignItems: "center",
    flexDirection: "row",
    gap: 8,
    paddingBottom: 12,
    paddingHorizontal: 16,
  },
  priceTag: {
    alignItems: "center",
    borderRadius: 20,
    flexDirection: "row",
    gap: 4,
    paddingHorizontal: 12,
    paddingVertical: 6,
  },
  priceText: {
    color: "white",
    fontFamily: "Inter-SemiBold",
    fontSize: 14,
  },
  tag: {
    borderRadius: 16,
    paddingHorizontal: 12,
    paddingVertical: 4,
  },
  tagText: {
    fontFamily: "Inter-Medium",
    fontSize: 12,
  },
  tagsContainer: {
    flexDirection: "row",
    flexWrap: "wrap",
    gap: 8,
    paddingBottom: 12,
    paddingHorizontal: 16,
  },
  viewsText: {
    fontFamily: "Inter-Regular",
    fontSize: 12,
  },
})
