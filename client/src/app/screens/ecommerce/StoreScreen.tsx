import React from "react"
import { View, Text, StyleSheet, SafeAreaView } from "react-native"

export const StoreScreen: React.FC = () => {
  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.content}>
        <Text style={styles.title}>Store</Text>
        <Text style={styles.subtitle}>Browse products</Text>
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
