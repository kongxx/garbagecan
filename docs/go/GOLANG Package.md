# GOLANG打包

## 工程目录结构
假设有个Go语言的工程myproject，目录结构如下
``` bash
myproject
+-- src
    |-- myapp
    |   +-- myapp.go
    +-- mylib
        +-- logger.go
```
其中mylib目录是一个lib库，myapp是一个应用，其中引用了mylib库。

## 工程样例程序
下面是logger.go和myapp.go程序代码

logger.go
``` Go
package mylib

import "fmt"

func Print(msg string) {
        fmt.Print(msg)
}
```

myapp.go
``` Go
package main

import (
        "mylib"
)

func main() {
        mylib.Print("Hello World!\n")
}
```

## 编译打包工程
### 设置环境变量
export GOPATH=/home/kongxx/test/golang/myproject

### 编译打包
cd /home/kongxx/test/golang/myproject
go install mylib
go install myapp

### 生成目录结构
下面是运行编译打包之后的目录结构，其中
* pkg 目录是生成的库目录
* bin 目录是生成的可运行程序目录
``` bash
myproject
|-- bin
|   +-- myapp
|-- pkg
|   +-- linux_amd64
|       +-- mylib.a
+-- src
    |-- myapp
    |   +-- myapp.go
    +-- mylib
        +-- logger.go
```

### 测试
运行bin/myapp即可。
