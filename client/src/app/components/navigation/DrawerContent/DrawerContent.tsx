import React from 'react';
import { View, Text } from 'react-native';

interface DrawerContentProps {
  // Define props here
}

const DrawerContent: React.FC<DrawerContentProps> = (props) => {
  return (
    <View>
      <Text>DrawerContent Component</Text>
    </View>
  );
};

export default DrawerContent;