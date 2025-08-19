// import { ComponentProps } from "react"
// import { NavigationContainer, NavigatorScreenParams } from "@react-navigation/native"
// import { createNativeStackNavigator, NativeStackScreenProps } from "@react-navigation/native-stack"

// import Config from "@config/index"
// import { useAppTheme } from "src/app/theme/context"
// import { ErrorBoundary } from "@screens/error/ErrorBoundary"
// import { navigationRef, useBackButtonHandler } from "./navigationUtilities"
// import {MainNavigator, MainTabParamList} from "./MainNavigator"

// export type AppStackParamList = {
//   Main: NavigatorScreenParams<MainTabParamList>
//   Auth:undefined
// }

// const exitRoutes = Config.exitRoutes

// export type AppStackScreenProps<T extends keyof AppStackParamList> = NativeStackScreenProps<
//   AppStackParamList,
//   T
// >
// const Stack = createNativeStackNavigator<AppStackParamList>()

// const AppStack = () => {
//   const  isAuthenticated  = true
//   const {
//     theme: { colors },
//   } = useAppTheme()

//   return (
//     <Stack.Navigator
//       screenOptions={{
//         headerShown: false,
//         navigationBarColor: colors.background,
//         contentStyle: {
//           backgroundColor: colors.background,
//         },
//       }}
//       initialRouteName={isAuthenticated ? "Main" : "Auth"}
//     >
//       <Stack.Screen name="Main" component={MainNavigator} />
//     </Stack.Navigator>
//   )
// }

// export interface NavigationProps
//   extends Partial<ComponentProps<typeof NavigationContainer<AppStackParamList>>> { }

// export const AppNavigator = (props: NavigationProps) => {
//   const { navigationTheme } = useAppTheme()

//   useBackButtonHandler((routeName) => exitRoutes.includes(routeName))

//   return (
//     <NavigationContainer ref={navigationRef} theme={navigationTheme} {...props}>
//       <ErrorBoundary catchErrors={Config.catchErrors}>
//         <AppStack />
//       </ErrorBoundary>
//     </NavigationContainer>
//   )
// }

import React, { useEffect } from "react"
import { useColorScheme } from "react-native"
import { StatusBar } from "expo-status-bar"
import Loading from "@components/common/Loading/Loading"
import { NavigationContainer } from "@react-navigation/native"
import { createStackNavigator } from "@react-navigation/stack"

import { lightTheme, darkTheme } from "@styles/theme"

import { useAppSelector, useAppDispatch } from "@store/middleware"
import { AuthNavigator } from "./AuthNavigator"
import MainNavigator from "./MainNavigator"
import { setDarkMode } from "@store/slices/uiSlice"

const Stack = createStackNavigator()

const AppNavigator: React.FC = () => {
  const dispatch = useAppDispatch()
  const { isAuthenticated, isLoading } = useAppSelector((state) => state.auth)
  const { theme, isDarkMode } = useAppSelector((state) => state.ui)
  const systemColorScheme = useColorScheme()

  useEffect(() => {
    if (theme === "auto") {
      dispatch(setDarkMode(systemColorScheme === "dark"))
    } else {
      dispatch(setDarkMode(theme === "dark"))
    }
  }, [theme, systemColorScheme, dispatch])

  if (isLoading) {
    return <Loading />
  }

  return (
    <NavigationContainer theme={isDarkMode ? darkTheme : lightTheme}>
      <StatusBar style={isDarkMode ? "light" : "dark"} />
      <Stack.Navigator screenOptions={{ headerShown: false }}>
        {isAuthenticated ? (
          <Stack.Screen name="Main" component={MainNavigator} />
        ) : (
          <Stack.Screen name="Auth" component={AuthNavigator} />
        )}
      </Stack.Navigator>
    </NavigationContainer>
  )
}

export default AppNavigator
