# Windows上node.js的多版本管理工具

在Linux上我一直使用nvm来管理nodejs的不同版本，但是nvm没有windows版本，今天发现在windows上可以使用另外一个版本管理工具nvm-windows来管理。

## 下载与安装

下载地址：https://github.com/coreybutler/nvm-windows/releases

安装前，这里有一点需要注意，如果以前安装过node，需要先卸载，并且要把目录清理干净。下面是官方给的说明：

``` shell
It comes with an installer (and uninstaller), because getting it should be easy. Please note, you need to uninstall any existing versions of node.js before installing NVM for Windows. Also delete any existing nodejs installation directories (e.g., "C:\Program Files\nodejs") that might remain. NVM's generated symlink will not overwrite an existing (even empty) installation directory.

You should also delete the existing npm install location (e.g. "C:\Users<user>\AppData\Roaming\npm") so that the nvm install location will be correctly used instead. After install, reinstalling global utilities (e.g. gulp) will have to be done for each installed version of node:
```

## 使用

### 查看当前已经安装的nodejs版本

``` shell
C:\Users\kongxx> nvm list

No installations recognized.
```

因为是新安装，所以提示系统没有安装任何版本。

### 查看可以安装的nodejs版本

``` shell
C:\Users\kongxx> nvm list available

|   CURRENT    |     LTS      |  OLD STABLE  | OLD UNSTABLE |
|--------------|--------------|--------------|--------------|
|    8.8.1     |    6.11.5    |   0.12.18    |   0.11.16    |
|    8.8.0     |    6.11.4    |   0.12.17    |   0.11.15    |
|    8.7.0     |    6.11.3    |   0.12.16    |   0.11.14    |
|    8.6.0     |    6.11.2    |   0.12.15    |   0.11.13    |
|    8.5.0     |    6.11.1    |   0.12.14    |   0.11.12    |
|    8.4.0     |    6.11.0    |   0.12.13    |   0.11.11    |
|    8.3.0     |    6.10.3    |   0.12.12    |   0.11.10    |
|    8.2.1     |    6.10.2    |   0.12.11    |    0.11.9    |
|    8.2.0     |    6.10.1    |   0.12.10    |    0.11.8    |
|    8.1.4     |    6.10.0    |    0.12.9    |    0.11.7    |
|    8.1.3     |    6.9.5     |    0.12.8    |    0.11.6    |
|    8.1.2     |    6.9.4     |    0.12.7    |    0.11.5    |
|    8.1.1     |    6.9.3     |    0.12.6    |    0.11.4    |
|    8.1.0     |    6.9.2     |    0.12.5    |    0.11.3    |
|    8.0.0     |    6.9.1     |    0.12.4    |    0.11.2    |
|    7.10.1    |    6.9.0     |    0.12.3    |    0.11.1    |
|    7.10.0    |    4.8.5     |    0.12.2    |    0.11.0    |
|    7.9.0     |    4.8.4     |    0.12.1    |    0.9.12    |
|    7.8.0     |    4.8.3     |    0.12.0    |    0.9.11    |
|    7.7.4     |    4.8.2     |   0.10.48    |    0.9.10    |
```

### 安装指定版本的node

这里安装了 6.10.0 和 7.10.0 两个版本
``` shell
C:\Users\kongxx> nvm install 6.10.0 64-bit
...

C:\Users\kongxx> nvm install 7.10.0 64-bit
...

```

再次查看已安装的版本

``` shell
C:\Users\kongxx> nvm list

    7.10.0
    6.10.0
```

### 使用指定版本的node

``` shell
C:\Users\kongxx> nvm use 6.10.0
Now using node v6.10.0 (64-bit)

C:\Users\kongxx> nvm list

    7.10.0
  * 6.10.1 (Currently using 64-bit executable)

C:\Users\kongxx> node -v
v6.10.0

```

### 删除指定版本的node

``` shell
C:\Users\kongxx> nvm uninstall 7.10.0
...

```
