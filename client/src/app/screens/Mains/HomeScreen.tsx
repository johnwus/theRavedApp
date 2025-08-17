import React from 'react'
import { Text } from 'react-native-gesture-handler'
import { StyleSheet, View } from 'react-native';
import { colors } from 'src/app/theme/colors';
const HomeScreen = () => {
  return (
    <View style={styles.container}>
  <Text>HomeScreen</Text>
</View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    gap: 10,
    backgroundColor: colors.palette.angry100
  },
});

export default HomeScreen
