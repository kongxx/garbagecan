# ？？？

[TOC]

## Node.js是什么?

官网上（http://www.nodejs.org）给Node下的定义是：“一个搭建在Chrome JavaScript运行时
上的平台，用于构建高速、可伸缩的网络程序。Node.js采用的事件驱动、非阻塞I/O模型，使它
既轻量又高效，并成为构建运行在分布式设备上的数据密集型实时程序的完美选择。”

## 为什么要用Node.js

- 轻量，快速 （资源占用少，高吞吐量，高实时性）
- 容易安装，配置，学习成本低
- 第三方模块很多
- 社区活跃
- 数据密集型实时（data-intensive real-time）程序

## 什么时候不用Node.js

- CPU密集型型程序
- Node.JS本身并不利用底层系统的所有核，默认情况下它是单线程的，所以必须自己编写逻辑以利用多核处理器并使其成为多线程。

## Node.js安装

- 二进制安装包

> http://nodejs.org/ 

或者

- Linux上安装

  sudo yum install nodejs

- Node.js 包管理器 npm

  ``` shell
  npm -v
  npm search socket.io
  npm install socket.io
  npm list
  ...
  ```

- Node核心模块
>  http://nodejs.cn/api/

## 一个例子Hello World

- index.js

  ``` javascript
  var http = require('http');
  http.createServer(function(req, res) {
      res.writeHead(200);
      res.end('Hello World!');
  }).listen(3000);
  ```

- 运行 

  ``` shell
  node index.js
  ```

- 访问 http://localhost:3000

## 模块

- 模块既可以是一个文件，也可以是一个目录，如果是目录，Node通常会在目录下找index.js作为模块的默认入口。可以通过package.json来修改入口。

- 典型的模块是一个包含exports对象属性的文件

- 定义模块（导出多个对象/方法用exports）

  mymodule.js

  ``` javascript
  exports.function1 = function() {
      console.log('function1');
  }
  exports.function2 = function(arg) {
      console.log('function2');
  }
  ```

- 使用模块

  ``` javascript
  var mymodule = require('./mymodule');
  mymodule.function1();
  mymodule.function2('hello world');
  ```

- 如果只导出一个对象/方法用exports，使用module.exports

  ``` javascript
  var myobj = 'hello world';
  module.exports = myobj;
  ```

  使用

  ``` javasc
  var mymodule = require('./mymodule');
  console.log(mymodule.myobj);
  ```

- 如果模块既有exports又有module.exports，那么它会返回module.exports，而exports会被忽略。

## 同步 vs 异步

Node.js中很多模块的方法都是异步调用的，因此，我们常常需要把处理逻辑写在回掉函数里。

先看看同步版本，

``` javascript
var fs = require('fs');
data = fs.readFileSyn('./db.conf');
conf = parse(data)
data = DBUtils(conf).querySync(sql);
fs.writeFileSync('records.log', data);
```

同样的功能，使用异步版本：

```javascript
var fs = require('fs');
fs.readFile('./db.conf', function(err, data) {
	if (err) throw err;
	conf = parse(data)
	DBUtils(conf).query(sql, function(err, data) {
		if (err) throw err;
      	fs.writeFile('records.log', data, function(err) {
        	if (err) throw err;
      	});
	});
});
```

- 从代码上看，同步版本代码比较直观，异步版本代码嵌套层次太深且不容易阅读。

### 异步函数的同步调用

异步代码在嵌套不深的情况下看着还是挺简洁的，但是一旦嵌套太深，写起来就比较操蛋了。

所以我们可以使用nimble库来封装一下

异步版本：

``` javascript
setTimeout(function() {
  console.log('aaa');
  setTimeout(function() {
    console.log('bbb');
    setTimeout(function() {
      console.log('ccc');
    }, 1000);
  }, 1000);
}, 1000);
```

封装后的同步版本：

``` javascript
var flow = require('nimble');
flow.series([
  function(callback) {
    setTimeout(function() {
      console.log('aaa');
      callback();
    }, 1000);
  },
  function(callback) {
    setTimeout(function() {
      console.log('bbb');
      callback();
    }, 1000);
  },
  function(callback) {
    setTimeout(function() {
      console.log('ccc');
      callback();
    }, 1000);
  }
]);
```

###关于异步调用

Node中的大多数内置模块在使用回调时都会带两个参数：

第一个是用来放可能会发生的错误的

第二个是放结果的

  ``` javascript
var fs = require('fs');
fs.readFile('./package.json', function(err, data) {
  if (err) throw er;
  // do something with data if no error has occurred
});
  ```

