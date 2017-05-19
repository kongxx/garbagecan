# Electron 使用 Webpack2 预编译 Electron 和 Browser targets

前一篇文章说了说怎样使用 Webpack2 预编译 Electron 应用，但是有时候我们希望使用 Webpack2 的热部署功能来提高我们的开发效率，使我们在代码修改后能自动立即看到修改后的结果。那么今天就看看怎样来实现这个功能。

## 安装依赖库

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
|-- webpack.config.js
`-- src
    |-- index.html
    |-- index.js
    |-- user.js
    `-- style.css

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
    "electron": "webpack --target electron-renderer && electron .",
    "web": "webpack --target web && webpack-dev-server --target web --hot --inline",
    "packager": "webpack --target electron-renderer && electron-packager . --platform=linux --electron-version=1.6.6  --overwrite"
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

- 其中 main 定义了 app 的入口，这里是使用的main.js作为入口。
- scripts 中的 electron 使用 "webpack  --target electron-renderer" 来打包js和css，然后仍然使用 electron 来运行应用程序。
- scripts 中的 web 使用 "webpack --target web" 打包js和css，同样，后面在使用 webpack-dev-server 时也需要使用 "--target web"选项。
- scripts 中的 packager 定义了打包程序为一个可执行程序。

### webpack.config.js
``` javascript
/* eslint strict: 0 */
'use strict';

const path = require('path');
const webpack = require('webpack');

module.exports ={
  entry: [
    './src/index.js'
  ],
  output: {
    path: path.join(__dirname, 'src'),
    publicPath: '/src/',
    filename: 'bundle.js',
  },
  module: {
    rules: [{
      test: /\.css$/,
      use: [ 'style-loader', 'css-loader' ]
    }]
  }
};
```

** 注：webpack.config.js 中我去掉了target 配置项 **

### main.js

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
});
```

### src/index.js

webpack 入口文件，这里仅仅打印当前应用是否是运行在 electron 中。

``` javascript
import './style.css';

console.log('This is index!');

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
    <script type="text/javascript" src="./bundle.js" charset="utf-8"></script>
    <h1>Hello World</h1>
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

- 作为 electron 应用程序运行

    ``` shell
    $ npm run electron
    ```

- 作为 web 应用程序运行

    ``` shell
    $ npm run web
    ```

    运行起来后，在浏览器访问

    http://localhost:8080/webpack-dev-server/src/

    或

    http://localhost:8080/src/

    然后，随意修改一下 index.js、user.js 或 style.css 并保存，此时即可看到页面立刻自动刷新了，这就说明webpack 的热部署功能已经工作了。

- 打包 electron 应用程序

    ``` shell
    $ npm run packager
    ```

    上面程序执行后，会在当前目录下生成 myapp-linux-x64 目录，然后运行 myapp-linux-x64/myapp 即可启动打包好的可执行程序。