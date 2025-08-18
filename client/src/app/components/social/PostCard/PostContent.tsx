import React from 'react';
import { View, Text } from 'react-native';

type PostContentProps = {
  text?: string;
  color?: string;
};

const PostContent: React.FC<PostContentProps> = ({ text, color = '#111214' }) => {
  if (!text) return null;
  return (
    <View style={{ paddingHorizontal: 16, paddingBottom: 12 }}>
      <Text style={{ fontSize: 16, lineHeight: 22, color }} numberOfLines={3}>
        {text}
      </Text>
    </View>
  );
};

export default PostContent;


