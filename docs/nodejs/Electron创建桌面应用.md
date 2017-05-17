# Electron 创建桌面应用

[TOC]

## 添加淘宝 NPM 镜像

啥都不说了，干活前先添加淘宝 NPM 镜像，不然后面安装模块的时候会卡住。

``` shell
$ sudo npm install -g cnpm --registry=https://registry.npm.taobao.org
```

## 安装 electron

这里我是使用的全局

``` shell
$ sudo cnpm install -g electron
```

## 创建应用

一个 Electron 应用的目录结构大致如下：

```
myapp/
├── package.json
├── main.js
└── index.html
```

### package.json

``` json
{
  "name": "myapp",
  "version": "0.1.0",
  "main": "main.js",
  "scripts": {
    "start": "electron main.js"
  }
}
```

### main.js

``` javascript
const {app, BrowserWindow} = require('electron');
const path = require('path');
const url = require('url');

let win;

function createWindow () {
  win = new BrowserWindow({width: 800, height: 600});

  win.loadURL(url.format({
    pathname: path.join(__dirname, 'index.html'),
    protocol: 'file:',
    slashes: true
  }));

  win.on('closed', () => {
    win = null;
  });
}

app.on('ready', createWindow);

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit();
  }
});

app.on('activate', () => {
  if (win === null) {
    createWindow();
  }
});
```

### index.html

``` html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>Hello World!</title>
  </head>
  <body>
    <h1>Hello World!</h1>
    <ul>
      <li>Node: <script>document.write(process.versions.node)</script></li>
      <li>Chrome: <script>document.write(process.versions.chrome)</script></li>
      <li>Electron: <script>document.write(process.versions.electron)</script></li>
    </ul>
  </body>
</html>
```

## 运行应用

进入应用程序目录，然后运行

```
$ electron .
```

另外，由于我们在 package.json 文件里定义了

``` json
  ...
  "scripts": {
    "start": "electron main.js"
  }
```

所以也可以使用 npm 来运行

``` shell
$ npm install
$ npm start
```
