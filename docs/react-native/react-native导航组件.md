# react-native导航组件

## 创建工程

``` shell
$ npx react-native init MyReactNativeApp
```

## 安装react-native navigation和依赖库

``` shell
$ cd MyReactNativeApp

$ npm install @react-navigation/native
$ npm install @react-navigation/native-stack
$ npm install @react-navigation/stack

$ npm install react-native-gesture-handler react-native-pager-view react-native-paper react-native-reanimated react-native-safe-area-context react-native-screens react-native-tab-view

$ npm install @react-navigation/bottom-tabs @react-navigation/drawer @react-navigation/elements @react-navigation/material-bottom-tabs @react-navigation/material-top-tabs
```

``` shell
$ cd ios

$ npx pod-install ios
```

## 导航代码

创建 src/navigation.js 文件，在其中添加一个导航器组件，以及两个屏幕组件 HomeScreen 和 ProfileScreen。同时在这两个屏幕组件中添加一个按钮，用于导航到另一个屏幕组件。

``` JavaScript
import { StyleSheet, Text, View, Button } from 'react-native'
import React from 'react'
import { NavigationContainer } from '@react-navigation/native'
import { createNativeStackNavigator } from '@react-navigation/native-stack'

function HomeScreen(props) {
  return (
    <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
      <Text>Home Screen</Text>
      <Button title="Go to Profile" onPress={() => props.navigation.navigate('Profile')} />
    </View>
  )
}

function ProfileScreen(props) {
  return (
    <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
      <Text>Profile Screen</Text>
      <Button title="Go to Home" onPress={() => props.navigation.navigate('Home')} />
    </View>
  )
}

const Stack = createNativeStackNavigator()

export default function index() {
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="Home">
        <Stack.Screen name="Home" component={HomeScreen} options={{ title: 'Home' }} />
        <Stack.Screen name="Profile" component={ProfileScreen} options={{ title: 'Profile' }} />
      </Stack.Navigator>
    </NavigationContainer>
  )
}
```

修改 App.tsx 文件，添加 NavigationContainer 组件

``` JavaScript
import React from 'react';
import { SafeAreaView, View, StyleSheet, StatusBar } from 'react-native';
import MyNavigation from './src/navigation'

function App(): React.JSX.Element {

  return (
    <View style={styles.container}>
      <MyNavigation />
    </View>
  )
}

var styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#ffffff',
  }
})

export default App
```

## 运行

``` shell
$ npx react-native start
```

## 参考

- https://reactnavigation.org/docs/getting-started/
- https://reactnavigation.org/docs/hello-react-navigation
