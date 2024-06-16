
# React Native 集成 ArcGIS 地图

ArcGIS官方提供了 JavaScript SDK，也提供了 ArcGIS-Runtime-SDK-iOS，但是并没有提供 React Native的版本，所以这里使用了 react-native-arcgis-mapview 库，这个库比较老，支持的 ArcGIS-Runtime-SDK-iOS 版本是100.4，但是在使用的时候发现，在使用pod install安装的时候总是会下载失败，所以后面手动将 ArcGIS-Runtime-SDK-iOS 的版本改为 100.14.1。

## 创建工程

- 初始化工程（需要科学上网）

``` shell
npx react-native init MyReactNativeApp
```

- 安装依赖

``` shell
cd MyReactNativeApp/ios
pod install
```

## 安装react-native-arcgis-mapview

- 使用 npm 安装 react-native-arcgis-mapview 库

``` shell
cd MyReactNativeApp
npm install react-native-arcgis-mapview
```

- 修改 Podfile 文件，在最后添加

``` shell
pod 'RNArcGISMapView', :path => "../node_modules/react-native-arcgis-mapview/ios"
```

- 编辑 node_modules/react-native-arcgis-mapview/ios/RNArcGISMapView.podspec，修改 dependency 版本为 100.14.1。

``` shell
s.dependency 'ArcGIS-Runtime-SDK-iOS', '100.4'
=>
s.dependency 'ArcGIS-Runtime-SDK-iOS', '100.14.1'
```

- 使用 pod install 安装 ArcGIS-Runtime-SDK-iOS 

``` shell
cd MyReactNativeApp/ios
pod install
```

## 使用 react-native-arcgis-mapview

- 在 App.tsx 中使用 react-native-arcgis-mapview 库，完整代码如下

``` JavaScript
import React, {useRef, useState}from 'react';
import {View, Text, Button, StyleSheet } from 'react-native'
import ArcGISMapView, { setLicenseKey } from 'react-native-arcgis-mapview'

function App(): React.JSX.Element {

  const key = 'AAPK9675225fa5ec47e9af9e9d09c4370e94yvQERZY3NoQGZCDh3absMuwADsEfbTwYQmqQ8FtbhcEJcxyO0a1OsMFk2nWLIE31';
  
  setLicenseKey(key);

  const mapView = useRef(null);

  const basemap = 'https://www.arcgis.com/home/item.html?id=6b6b9cea06964cb38d8a654964c347ab';

  return (
    <View style={styles.container}>
      <ArcGISMapView
          style={styles.map} 
          initialMapCenter={[{latitude: 32.788, longitude: -79.940, scale: 10000.0}]}
          basemapUrl={basemap}
          ref={mapView}
      />
      <Button title="Test" onPress={() => {
          console.log(mapView.current.props.basemapUrl)
      }} />
    </View>
  );
}

var styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  map: {
    flex: 1,
  },
})

export default App;
```

## 验证

- 运行项目，验证是否成功。
  
``` shell
npm start
```
