# Ubuntu Server 14.04 上安装 OpenSSL 1.0.2
在 Ubuntu Server 14.04上默认的 OpenSSL 的版本还是 OpenSSL 1.0.1f，使用 apt-get 还无法升级，因此只能通过编译来安装了。

## 查看现有版本

``` shell
/usr/bin/openssl version
OpenSSL 1.0.1f 6 Jan 2014
```

## 下载并解压

``` shell
wget https://www.openssl.org/source/openssl-1.0.2l.tar.gz
tar -xzvf openssl-1.0.2l.tar.gz
```

## 编译安装

``` shell
cd openssl-1.0.2l
sudo ./config
sudo make install
```

## 建立新版连接

``` shell
sudo ln -sf /usr/local/ssl/bin/openssl `which openssl`
```

## 查看新版本

``` shell
/usr/bin/openssl version
OpenSSL 1.0.2l  25 May 2017
```
