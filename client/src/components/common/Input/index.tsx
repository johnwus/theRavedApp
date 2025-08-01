import React from 'react';
import { View, Text } from 'react-native';

interface indexProps {
  // Define props here
}

const index: React.FC<indexProps> = (props) => {
  return (
    <View>
      <Text>index Component</Text>
    </View>
  );
};

export default index;