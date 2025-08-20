import React from "react"
import { createNativeStackNavigator } from "@react-navigation/native-stack"

import { ForgotPasswordScreen } from "../screens/auth/ForgotPasswordScreen"
import { LoginScreen } from "../screens/auth/LoginScreen"
import { RegisterScreen } from "../screens/auth/RegisterScreen"
import { StudentVerificationScreen } from "../screens/auth/StudentVerificationScreen"

export type AuthStackParamList = {
  Login: undefined
  Register: undefined
  ForgotPassword: undefined
  StudentVerification: { email: string }
}

const Stack = createNativeStackNavigator<AuthStackParamList>()

export const AuthNavigator: React.FC = () => {
  return (
    <Stack.Navigator
      initialRouteName="Login"
      screenOptions={{
        headerShown: false,
        gestureEnabled: true,
      }}
    >
      <Stack.Screen name="Login" component={LoginScreen} />
      <Stack.Screen name="Register" component={RegisterScreen} />
      <Stack.Screen name="ForgotPassword" component={ForgotPasswordScreen} />
      <Stack.Screen name="StudentVerification" component={StudentVerificationScreen} />
    </Stack.Navigator>
  )
}
