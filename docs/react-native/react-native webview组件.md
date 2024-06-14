# react-native webview组件

## 创建工程

``` shell
$ npx react-native init MyReactNativeApp
```

## 安装WebView组件

``` shell
$ cd MyReactNativeApp
$ npm install react-native-webview --save
```

``` shell
$ cd ios
$ npx pod-install ios
```

## 使用WebView

创建src/mywebview.js文件，并添加以下代码：

``` js
import React from 'react'
import { WebView } from 'react-native-webview';

export default function index() {
  return (
    <WebView
      source={{ uri: 'https://cn.bing.com' }}
      style={{ marginTop: 20, }}
    />
  )
}
```

修改 App.tsx 文件，添加 MyWebView 组件的使用：

``` js
import React from 'react';
import { SafeAreaView, View, StyleSheet } from 'react-native';
import MyWebView from './src/webview/mywebview'

function App(): React.JSX.Element {

  return (
    <SafeAreaView style={styles.container}>
      <MyWebView />
    </SafeAreaView>
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

- https://github.com/react-native-webview/react-native-webview/blob/master/docs/Getting-Started.md