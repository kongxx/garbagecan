# Webpack2入门

## 介绍

webpack 是一个 JavaScript 应用程序模块打包器(module bundler)。webpack 通过快速建立应用程序依赖图表并以正确的顺序打包它们来简化你的工作流。你能够针对你的代码来对 webpack 进行自定义的优化配置，比如为生产环境拆分 vendor/css/js 代码，无刷新热重载(hot-reload)等.

## Webpack可以做什么

- 处理module依赖关系
- 打包js，css和png等
- 降低页面初始加载时间
- 方便组合第三方组件库
- 可以转换不同语法成标准语法

## 安装

这里使用的是 webpack 2.4.1 版本。

``` shell
# 全局安装
npm install -g webpack

#本地安装
npm install --save-dev webpack
```

## 一个小例子

### 创建应用并安装必要的依赖库

``` shell
$ mkdir webpack-demo
$ cd webpack-demo
$ npm init -y
$ npm install --save-dev webpack
$ npm install --save-dev css-loader
$ npm install --save-dev style-loader
$ npm install --save-dev extract-text-webpack-plugin
$ npm install webpack-dev-server
```

### 入口 (index.js)

``` javascript
import './style.css';
content = require("./content.js")

var element = document.createElement('message');
element.innerHTML = content;
document.body.appendChild(element);
```

### 自定义模块 (content.js)

``` javascript
module.exports = "It works from content.js!!!";
```

### css (style.css)

``` javascript
body {
  background: grey;
}
```

### html页面 (index.html)

``` html
<html>
  <head>
    <meta charset="utf-8">
  </head>
  <body>
    <script type="text/javascript" src="bundle.js" charset="utf-8"></script>
   </body>
</html>
```

### webpack配置

``` json
module.exports = {
    entry: "./index.js",
    output: {
        path: __dirname,
        filename: "bundle.js"
    },
    module: {
        rules: [{
            test: /\.css$/,
            use: [ 'style-loader', 'css-loader' ]
        }]
    }
};
```

### 打包生成 bundle.js文件

``` shell
$ webpack
Hash: 62f1078175b4dbbd7d46
Version: webpack 2.4.1
Time: 368ms
    Asset     Size  Chunks             Chunk Names
bundle.js  19.1 kB       0  [emitted]  main
   [0] ./content.js 48 bytes {0} [built]
   [1] ./style.css 992 bytes {0} [built]
   [2] ./index.js 171 bytes {0} [built]
   [3] ./~/css-loader!./style.css 192 bytes {0} [built]
   [4] ./~/css-loader/lib/css-base.js 2.26 kB {0} [built]
   [5] ./~/style-loader/addStyles.js 9.15 kB {0} [built]
   [6] ./~/style-loader/fixUrls.js 3.01 kB {0} [built]
```

运行后可以看到生成一个 bundle.js 文件，其中包含了所有js和css。

在浏览器直接访问 index.html 文件查看结果。

### 热部署

#### 本地热部署

- 第一步：启动  webpack
  ``` shell
  $ webpack --watch
  ```
- 第二步：修改代码
- 第三部：浏览器刷新（访问file:///.../index.html）

#### 服务器热部署

- 第一步：启动 webpack-dev-server
  ``` shell
  $ webpack-dev-server
  ```
- 第二步：修改代码
- 第三步：浏览器刷新（访问http://localhost:8000）

