import React from 'react';
import { View, Text } from 'react-native';

interface AvatarProps {
  // Define props here
}

const Avatar: React.FC<AvatarProps> = (_props) => {
  return (
    <View>
      <Text>Avatar Component</Text>
    </View>
  );
};

export default Avatar;