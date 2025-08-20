import React from "react"
import { StyleSheet, View } from "react-native"
import { Text } from "react-native-gesture-handler"
import { colors } from "src/app/theme/colors"

const HomeScreen = () => {
  return (
    <View style={styles.container}>
      <Text>HomeScreen</Text>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    alignItems: "center",
    backgroundColor: colors.palette.angry100,
    flex: 1,
    gap: 10,
    justifyContent: "center",
  },
})

export default HomeScreen
