# Python使用Bottle来提供一个简单的web服务

## 介绍

今天有个不正经的需求，就是要快速做一个restful api的性能测试，要求测试在海量作业数据的情况下客户端分页获取所有作业的性能。因为只是一个小的的测试工作，所以就想到了Bottle框架作为Web服务器，这里就简单说说怎样使用Bottle框架。

## 安装

``` shell
pip install bottle
```

## 启动服务

运行下面的python脚本即可启动一个Web服务。

``` python
from bottle import route, run, request

@route('/hello')
def hello():
    return "Hello World!"

run(host='0.0.0.0', port=8080, debug=True)
```

- 测试，使用如下命令行来测试服务

``` shell
curl http://localhost:8080/hello
```

## 提供Restful API

### 服务端

- 由于我需要根据参数来返回部分结果（比如：根据偏移量和分页大小），因此可以使用Bottle的动态路来实现，当然用参数实现也是可以的。
- 这里假定我设置的请求url为：/jobs/<offset:int>/<size:int>
- 这里为了测试方便并没有返回作业信息，而是用offset和size作为json结果返回。

下面是服务器端测试代码

``` python
import json
from bottle import route, run, request


@route('/jobs/<offset:int>/<size:int>')
def get_jobs(offset, size):
    d = {"offset": offset, "size": size}
    return json.dumps(d)

run(host='0.0.0.0', port=8080, debug=True)
```

### 客户端

``` python
import httplib, json

c = httplib.HTTPConnection('localhost', 8080)
headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}
c.request('GET', '/jobs/123/321', '{}', headers)
s = c.getresponse().read().strip()
print json.loads(s)
```

