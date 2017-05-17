# Electron 使用 Webpack2 打包多入口应用程序

接前面一篇文章，前一篇文章中只有一个页面，并且只有一个js文件，所以打包的时候会把那个js打包成一个bundle.js文件。但是假如我们有多个页面，且每个页面需要使用的js文件也不同，那么我们应该怎样打包呢。

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
|   |-- home.html
|   |-- home.js
|   |-- about.html
|   |-- about.js
|   |-- contact.html
|   |-- contact.js
|   |-- user.js
|   `-- style.css
`-- webpack.config.js
```

- main.js - 程序的入口
- package.json - 是node的包说明文件
- webpack.config.js - webpack配置文件
- src/home.html和home.js - 主页面
- src/about.html和about.js- 关于页面
- src/contact.html和contact.js- 联系我们页面
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
  entry: {
      home: "./src/home.js",
      about: "./src/about.js",
      contact: "./src/contact.js"
  },
  output: {
    path: path.join(__dirname, 'build'),
    publicPath: path.join(__dirname, 'src'),
    filename: '[name].bundle.js',
  },
  module: {
    rules: [{
      test: /\.css$/,
      use: [ 'style-loader', 'css-loader' ]
    }]
  }
};

```

- 其中 target 指定为 electron-renderer
- 定义了多个entry - home, about 和 contact
- output 中 filename 定义成 '[name].bundle.js'，其中 [name] 即是上面 entry 中定义的名字。

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

  mainWindow.loadURL(`file://${__dirname}/src/home.html`);

  mainWindow.webContents.openDevTools();

  mainWindow.on('closed', () => {
    mainWindow = null;
  });
})
```

### Home Page

home.html
``` html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>Home</title>
  </head>
  <body>
    <script src="../build/home.bundle.js"></script>
    <h1>This is home!</h1>
    <ul>
      <li><a href="./contact.html">Contact us</a></li>
      <li><a href="./about.html">About</a></li>
    </ul>
  </body>
</html>
```

**注：其中引用的js变成了 home.bundle.js **

home.js
``` javascript
import './style.css';

console.log('This is home!');

var User = require('./user');

var user1 = new User('kongxx');
console.log("Hi, I am " + user1.getName());

var user2 = new User('ken');
console.log("Hi, I am " + user2.getName());

console.log('running in electron: ', require('is-electron-renderer'));
```

### About Page

about.html
``` html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>About</title>
  </head>
  <body>
    <script src="../build/about.bundle.js"></script>
    <h1>This is about!</h1>
    <a href="./home.html">Back</a>
  </body>
</html>
```

**注：其中引用的js是 about.bundle.js **

about.js
``` javascript
import './style.css';
console.log('This is about!');
```

### Contact us Page

contact.html
``` html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>Contact</title>
  </head>
  <body>
    <script src="../build/contact.bundle.js"></script>
    <h1>This is contact!</h1>
    <a href="./home.html">Back</a>
  </body>
</html>
```

**注：其中引用的js是 contact.bundle.js **

contact.js
``` javascript
import './style.css';
console.log('This is contact!');
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

``` css
body {
  background: grey;
  font-size: 20px;
}
```

## 测试

- 运行 electron 应用程序

    ``` shell
    $ npm run electron

    > myapp@1.0.0 electron /home/jhadmin/workspace/nodejs/myapp
    > webpack && electron .

    Hash: 1ebbb1014b2da9075658
    Version: webpack 2.5.1
    Time: 500ms
                Asset     Size  Chunks             Chunk Names
       home.bundle.js  19.8 kB       0  [emitted]  home
    contact.bundle.js    19 kB       1  [emitted]  contact
      about.bundle.js    19 kB       2  [emitted]  about
       [0] ./src/style.css 996 bytes {0} {1} {2} [built]
       [1] ./~/css-loader!./src/style.css 215 bytes {0} {1} {2} [built]
       [2] ./~/css-loader/lib/css-base.js 2.26 kB {0} {1} {2} [built]
       [3] ./~/style-loader/addStyles.js 9.15 kB {0} {1} {2} [built]
       [4] ./~/style-loader/fixUrls.js 3.01 kB {0} {1} {2} [built]
       [5] ./~/is-electron-renderer/index.js 304 bytes {0} [built]
       [6] ./src/user.js 127 bytes {0} [built]
       [7] ./src/about.js 55 bytes {2} [built]
       [8] ./src/contact.js 57 bytes {1} [built]
       [9] ./src/home.js 320 bytes {0} [built]
    ```

    这一步会首先运行 “webpack” 来生成 home.bundle.js，about.bundle.js 和 contact.bundle.js 文件，然后再使用 “electron .” 来运行应用程序。

    访问不同的页面观察日志输出可以发现每个页面均使用了各自的 bundle.js 文件。

- 打包 electron 应用程序

    ``` shell
    $ npm run packager
    ```

    上面程序执行后，会在当前目录下生成 myapp-linux-x64 目录，然后运行 myapp-linux-x64/myapp 即可启动打包好的可执行程序。
