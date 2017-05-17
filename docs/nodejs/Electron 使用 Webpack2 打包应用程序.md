# Electron 使用 Webpack2 打包应用程序 V2

前两天看了一下使用 Electron 来开发应用程序，今天说说所怎样集成 Electron 和 Webpack2 来打包应用程序。

## 安装依赖库

这里准备做个小的演示程序，首先安装一些这个演示程序需要看的第三方库

``` shell
$ npm install --save-dev electron 
$ npm install --save-dev electron-packager
$ npm install --save-dev webpack 
$ npm install --save-dev webpack-dev-server

$ npm install --save-dev webpack-target-electron-renderer
$ npm install --save-dev is-electron-renderer
$ npm install --save-dev css-loader
$ npm install --save-dev style-loader
$ npm install --save-dev extract-text-webpack-plugin
```

** 注：这里使用的webpack是2.5.1版本 **

## 工程结构

这个演示程序包含的文件结构如下所示：

``` shell
myapp
|-- main.js
|-- package.json
|-- src
|   |-- index.html
|   |-- index.js
|   |-- user.js
|   `-- style.css
`-- webpack.config.js
```

- main.js - 程序的入口
- package.json - 是node的包说明文件
- webpack.config.js - webpack配置文件
- src/index.html - 演示页面html文件
- src/index.js - 演示页面js文件
- src/user.js - 一个自定义的js模块
- src/style.css - 演示用的css文件

## 文件说明

### package.json
``` json
{
  "name": "myapp",
  "version": "1.0.0",
  "description": "",
  "main": "main.js",
  "scripts": {
    "electron": "webpack && electron .",
    "packager": "webpack && electron-packager . --platform=linux --electron-version=1.6.6  --overwrite"
  },
  "author": "",
  "license": "ISC",
  "devDependencies": {
    "css-loader": "^0.28.1",
    "electron": "^1.6.7",
    "electron-packager": "^8.7.0",
    "extract-text-webpack-plugin": "^2.1.0",
    "is-electron-renderer": "^2.0.1",
    "style-loader": "^0.17.0",
    "webpack": "^2.5.1",
    "webpack-dev-server": "^2.4.5",
    "webpack-target-electron-renderer": "^0.4.0"
  }
}
```

- 其中 main 定义了 app 的入口
- scripts 中的 electron 定义了一个命令用来使用 webpack 打包并使用 electron 来运行应用程序
- scripts 中的 packager 定义了打包程序为一个可执行程序。

### webpack.config.js
``` javascript
/* eslint strict: 0 */
'use strict';

const path = require('path');
const webpack = require('webpack');

module.exports ={
  target: 'electron-renderer',
  entry: [
    './src/index',
  ],
  output: {
    path: path.join(__dirname, 'build'),
    publicPath: path.join(__dirname, 'src'),
    filename: 'bundle.js',
  },
  module: {
    rules: [{
      test: /\.css$/,
      use: [ 'style-loader', 'css-loader' ]
    }]
  },
};
```

- 其中 target 指定为 electron-renderer

### main.js

Electron App 的入口 js 文件，启动一个窗口并加载 src/index.html 文件。

``` javascript
/* eslint strict: 0 */
'use strict';

const electron = require('electron');
const app = electron.app;
const BrowserWindow = electron.BrowserWindow;
let mainWindow = null;

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') app.quit();
});

app.on('ready', () => {
  mainWindow = new BrowserWindow({ width: 1024, height: 768 });

  mainWindow.loadURL(`file://${__dirname}/src/index.html`);

  mainWindow.webContents.openDevTools();

  mainWindow.on('closed', () => {
    mainWindow = null;
  });
})
```

### src/index.js

webpack 入口文件，这里仅仅打印当前应用是否是运行在 electron 中。

``` javascript
import './style.css';

var User = require('./user');

var user1 = new User('kongxx');
console.log("Hi, I am " + user1.getName());

var user2 = new User('ken');
console.log("Hi, I am " + user2.getName());

console.log('running in electron: ', require('is-electron-renderer'));
```

### src/index.html

测试的 html 文件，其中使用了 webpack 预编译好的 bundle.js 文件。

``` html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>Hello World</title>
  </head>
  <body>
    <script src="../build/bundle.js"></script>
    <ul>
      <li>Node: <script>document.write(process.versions.node)</script></li>
      <li>Chrome: <script>document.write(process.versions.chrome)</script></li>
      <li>Electron: <script>document.write(process.versions.electron)</script></li>
    </ul>
  </body>
</html>
```

### src/user.js

一个自定义的模块文件

``` javascript
module.exports = User;

function User(name) {
  this.name = name;
}

User.prototype.getName = function() {
  return this.name;
}
```

### src/style.css 

一个自定义的 css 文件

``` javascript
body {
  background: grey;
  font-size: 20px;
}
```

## 测试

- 直接运行 electron 应用程序

    ``` shell
    $ npm run electron
    ```
    
    这一步会首先运行 “webpack” 来生成 bundle.js 文件，然后再使用 “electron .” 来运行应用程序。

- 打包 electron 应用程序

    ``` shell
    $ npm run packager
    ```
    
    上面程序执行后，会在当前目录下生成 myapp-linux-x64 目录，然后运行 myapp-linux-x64/myapp 即可启动打包好的可执行程序。