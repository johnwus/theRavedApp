import React from 'react';
import { View, Text } from 'react-native';

interface LoadingProps {
  // Define props here
}

const Loading: React.FC<LoadingProps> = (props) => {
  return (
    <View>
      <Text>Loading Component</Text>
    </View>
  );
};

export default Loading;