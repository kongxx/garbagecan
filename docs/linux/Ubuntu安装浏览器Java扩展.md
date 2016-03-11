# Ubuntu安装浏览器Java扩展
[TOC]

---

## Firefox
```
cd /usr/lib/firefox-addons/plugins
ln -s /opt/jdk8/jre/lib/amd64/libnpjp2.so
```
其中java安装路径根据具体环境修改，修改完重新启动浏览器。

## Chromium
```
cd /usr/lib/chromium-browser/plugins
ln -s /opt/jdk8/jre/lib/amd64/libnpjp2.so
```
其中java安装路径根据具体环境修改，修改完重新启动浏览器。

