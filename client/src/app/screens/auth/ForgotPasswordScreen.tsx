import React from "react"
import { View, Text, StyleSheet, SafeAreaView } from "react-native"

export const ForgotPasswordScreen: React.FC = () => {
  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.content}>
        <Text style={styles.title}>Forgot Password</Text>
        <Text style={styles.subtitle}>Reset your password</Text>
      </View>
    </SafeAreaView>
  )
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: "#fff",
    flex: 1,
  },
  content: {
    alignItems: "center",
    flex: 1,
    justifyContent: "center",
    padding: 20,
  },
  subtitle: {
    color: "#666",
    fontSize: 16,
  },
  title: {
    color: "#333",
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 10,
  },
})
