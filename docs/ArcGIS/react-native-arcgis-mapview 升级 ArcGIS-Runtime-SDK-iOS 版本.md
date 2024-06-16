# react-native-arcgis-mapview 升级 ArcGIS-Runtime-SDK-iOS 版本

## 查看 ArcGIS-Runtime-SDK-iOS pod 版本信息

``` shell
$ pod spec cat ArcGIS-Runtime-SDK-iOS

{
  "name": "ArcGIS-Runtime-SDK-iOS",
  "platforms": {
    "ios": "14.0"
  },
  "version": "100.15.5",
  "summary": "ArcGIS Runtime SDK for iOS allows you to add 2D/3D mapping capabilities and spatial analysis from Esri's ArcGIS platform to your iOS apps.",
  "homepage": "https://developers.arcgis.com/en/ios/",
  "authors": {
    "Esri": "iOSDevelopmentTeam@esri.com"
  },
  "source": {
    "http": "https://gisupdates.esri.com/ArcGIS_RuntimeSDK/100.15.5/v100.15.5-Runtime-iOS.zip"
  },
  "vendored_frameworks": [
    "ArcGIS.xcframework",
    "Runtimecore.xcframework"
  ],
  "license": {
    "type": "Commercial"
  },
  "requires_arc": true,
  "description": "With ArcGIS you can : Create thematic interactive 2D maps & 3D scenes that allow your users to explore and understand their geographic data.\nSearch for places and addresses and display them on your map.\nGenerate directions, optimal routes and calculate drive time areas.\nChoose from a collection of ready-to-use basemaps, demographic maps, and imagery and make interactive maps with your data.\nEnrich your existing data with demographic variables for a given study area.\nAnalyze your data spatially to detect patterns, assess trends, and make decisions.\nAccess ArcGIS Online image services (basemap, multispectral, event and temporal) to visualize and analyze change.\nCreate custom REST endpoints to store and visualize your content.\nTake your maps and data offline to view, edit, search and find routes. And much more..."
}
```

可以看到最低 ios 版本要求是 14.0。

## 修改 Podfile

- 文件最后添加

``` shell
pod 'RNArcGISMapView', :path => "../node_modules/react-native-arcgis-mapview/ios"
```

- 修改最小ios版本支持

``` shell
platform :ios, min_ios_version_supported
=>
platform :ios, 14.0
```

## 修改 RNArcGISMapView podspec

- 编辑/node_modules/react-native-arcgis-mapview/ios/RNArcGISMapView.podspec，修改 platforms 和 dependency 版本

``` shell
  s.platforms    = { :ios => "14.0" }
  s.dependency 'ArcGIS-Runtime-SDK-iOS', '100.15.5'
```

## 升级pod库

``` shell
$ pod update
```
