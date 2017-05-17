# Python中动态创建类实例

## 简介

在Java中我们可以通过反射来根据类名创建类实例,那么在Python我们怎么实现类似功能呢？

其实在Python有一个builtin函数__import__，我们可以使用这个函数来在运行时动态加载一些模块。如下:

``` python
def createInstance(module_name, class_name, *args, **kwargs):
    module_meta = __import__(module_name, globals(), locals(), [class_name])
    class_meta = getattr(module_meta, class_name)
    obj = class_meta(*args, **kwargs)
    return obj
```

## 例子

首先我们建一个目录 my_modules, 其中包括三个文件
* \_\_init__.py: 模块文件
* my_module.py: 测试用的模块
* my_another_module: 另一个测试用的模块

### my_module.py

``` python
from my_modules.my_another_module import *

class MyObject(object):
    def test(self):
        print 'MyObject.test'
        MyObject1().test()
        MyObject2().test()
        MyAnotherObject().test()

class MyObject1(object):
    def test(self):
        print 'MyObject1.test'

class MyObject2(object):
    def test(self):
        print 'MyObject2.test'
```

### my_another_module.py

``` python
class MyAnotherObject(object):
    def test(self):
        print 'MyAnotherObject.test'
```

### test.py

``` python
def createInstance(module_name, class_name, *args, **kwargs):
    module_meta = __import__(module_name, globals(), locals(), [class_name])
    class_meta = getattr(module_meta, class_name)
    obj = class_meta(*args, **kwargs)
    return obj

obj = createInstance("my_modules.my_module", "MyObject")
obj.test()
```

``` shell
MyObject.test
MyObject1.test
MyObject2.test
MyAnotherObject.test
```

## pyinstaller集成

对于使用pyinstaller打包的应用程序，如果使用上面的代码，运行打包后的程序会出现下面的错误

``` python
Traceback (most recent call last):
  File "test.py", line 12, in <module>
    obj = createInstance("my_modules.my_module", "MyObject")
  File "test.py", line 7, in createInstance
    module_meta = __import__(module_name, globals(), locals(), [class_name])
ImportError: No module named my_modules.my_module
Failed to execute script test
```

这里错误的原因是 pyinstaller 在打包分析类的时候没有分析到 my_modules 下面的模块，所以运行报错。

### 解决办法一：

在 test.py 中把 my_modules 下的模块手动 import，见下面代码中的第一行。这种方法最简单，但是显然不太好。

``` python
import my_modules.my_module

def createInstance(module_name, class_name, *args, **kwargs):
    module_meta = __import__(module_name, globals(), locals(), [class_name])
    class_meta = getattr(module_meta, class_name)
    obj = class_meta(*args, **kwargs)
    return obj

obj = createInstance("my_modules.my_module", "MyObject")
obj.test()
```

### 解决办法二：

在使用 pyinstaller 打包的时候，指定 "--hidden-import"，如下

``` shell
pyinstaller -D --hidden-import my_modules.my_module test.py
```

### 解决办法三：

动态修改 python 运行时path，见下面代码中的前两行，其中path我们可以通过环境变量或者参数传递进来。显然这种方法要比前两种方法灵活的多。

``` python
import sys
sys.path.append(...)

def createInstance(module_name, class_name, *args, **kwargs):
    module_meta = __import__(module_name, globals(), locals(), [class_name])
    class_meta = getattr(module_meta, class_name)
    obj = class_meta(*args, **kwargs)
    return obj

obj = createInstance("my_modules.my_module", "MyObject")
obj.test()
```
