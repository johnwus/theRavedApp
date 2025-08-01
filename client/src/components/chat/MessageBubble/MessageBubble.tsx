import React from 'react';
import { View, Text } from 'react-native';

interface MessageBubbleProps {
  // Define props here
}

const MessageBubble: React.FC<MessageBubbleProps> = (props) => {
  return (
    <View>
      <Text>MessageBubble Component</Text>
    </View>
  );
};

export default MessageBubble;