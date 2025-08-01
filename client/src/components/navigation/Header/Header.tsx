import React from 'react';
import { View, Text } from 'react-native';

interface HeaderProps {
  // Define props here
}

const Header: React.FC<HeaderProps> = (props) => {
  return (
    <View>
      <Text>Header Component</Text>
    </View>
  );
};

export default Header;