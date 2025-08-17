import { StyleSheet } from 'react-native';

export const postCardStyles = StyleSheet.create({
  container: {
    marginBottom: 12,
    borderRadius: 16,
    overflow: 'hidden',
    elevation: 2,
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
  },
  contentContainer: {
    paddingHorizontal: 16,
    paddingBottom: 12,
  },
  content: {
    fontSize: 16,
    lineHeight: 22,
    fontFamily: 'Inter-Regular',
  },
  tagsContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    paddingHorizontal: 16,
    paddingBottom: 12,
    gap: 8,
  },
  tag: {
    paddingHorizontal: 12,
    paddingVertical: 4,
    borderRadius: 16,
  },
  tagText: {
    fontSize: 12,
    fontFamily: 'Inter-Medium',
  },
  moreTagsText: {
    fontSize: 12,
    fontFamily: 'Inter-Regular',
    alignSelf: 'center',
  },
  priceContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 16,
    paddingBottom: 12,
    gap: 8,
  },
  priceTag: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 20,
    gap: 4,
  },
  priceText: {
    color: 'white',
    fontSize: 14,
    fontFamily: 'Inter-SemiBold',
  },
  negotiableText: {
    fontSize: 12,
    fontFamily: 'Inter-Regular',
  },
  engagementContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 16,
    paddingBottom: 16,
  },
  engagementText: {
    fontSize: 14,
    fontFamily: 'Inter-Regular',
  },
  viewsText: {
    fontSize: 12,
    fontFamily: 'Inter-Regular',
  },
});