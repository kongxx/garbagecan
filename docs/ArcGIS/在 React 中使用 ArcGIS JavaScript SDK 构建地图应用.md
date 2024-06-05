# 在 React 中使用 ArcGIS JavaScript SDK 构建地图应用

## 创建React工程

``` shell
$ npx install -g create-react-app
$ create-react-app my-react-arcgis-app
$ cd my-react-arcgis-app
$ npm start
```

## 安装ArcGIS库

``` shell
$ npm install @arcgis/core
```

## 创建ArcGIS地图组件

``` JavaScript
import React, { useEffect, useRef } from 'react';
import Map from '@arcgis/core/Map';
import MapView from '@arcgis/core/views/MapView';
import FeatureLayer from '@arcgis/core/layers/FeatureLayer';

export default function MyMapViewer({}) {

    const mapRef = useRef();

    useEffect(() => {
        const map = new Map({
            basemap: 'streets-navigation-vector'
        });

        const view = new MapView({
            container: mapRef.current,
            map: map,
            center: [-79.940, 32.788],
            zoom: 16
        });

        const featureLayer = new FeatureLayer({
            url: 'https://services.arcgis.com/P3ePLMYs2RVChkJx/ArcGIS/rest/services/Charleston_Buildings_SLR_2080/FeatureServer/85',
            popupTemplate: {
                title: "{BuildingFID}",
                outFields: ["*"],
                content: "{BuildingFID}"
            },
        });

        map.add(featureLayer);

        return () => {
            if (view) {
                view.destroy()
            }
        };
    }, []);

    const mapStyle = {
        width: '100%',
        height: '100%',
        position: 'absolute',
        margin: 0,
        padding: 0,
    };

    return (
        <div className="map-container" ref={mapRef} style={mapStyle} />
    );
};
```

## 使用ArcGIS地图组件

修改App.js，内容如下：

``` JavaScript
import MyMapViewer from './MyMapViewer';
import './App.css';

function App() {

  return (
    <div className="app">
      <h1>My ArcGIS App</h1>
      <MyMapViewer />
    </div>
  );
}

export default App;
```

修改App.css，在文件夹最后添加ArcGIS的css

``` JavaScript
@import 'https://js.arcgis.com/4.29/@arcgis/core/assets/esri/themes/light/main.css';
```
