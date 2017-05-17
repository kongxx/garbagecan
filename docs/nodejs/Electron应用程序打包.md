# Electron 应用程序打包

[TOC]

接前一篇博客

## 安装electron-packager

``` shell
$ sudo cnpm install --save-dev electron-packager
```

## 修改 package.json 文件

``` json
{
  "name": "myapp",
  "version": "0.1.0",
  "main": "main.js",
  "scripts": {
    "start": "electron main.js",
    "packager": "electron-packager . --platform=linux --electron-version=1.6.6  --overwrite"

  },
  "devDependencies": {
    "electron-packager": "^8.7.0"
  }
}
```

> **注意：这里只指定了linux平台，可以 修改打包全平台的包。**

## 打包

``` shell
$ ELECTRON_MIRROR=http://npm.taobao.org/mirrors/electron/ npm run-script packager
```

> **这里说明一下，上面的命令前设置了 ELECTRON_MIRROR 使用淘宝镜像，如果不使用淘宝镜像，下载包的时候会出错。**

打包后，可以看到在myapp下生成一个myapp-linux-x64的目录，运行myapp-linux-x64/myapp即可启动应用。
