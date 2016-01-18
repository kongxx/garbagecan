# Python Singleton

## 首先声明一个singleton装饰器

``` python
#!/usr/bin/env python
# -*- coding: utf-8 -*-


def singleton(cls, *args, **kw):
    instances = {}

    def _singleton():
        if cls not in instances:
            instances[cls] = cls(*args, **kw)
        return instances[cls]

    return _singleton
```

## 使用@singleton注解在需要单例的类上标注
``` python
#!/usr/bin/env python
# -*- coding: utf-8 -*-


@singleton
class Config:

    def __init__(self):
        pass

    def test(self):
        pass

```

## 测试
``` python
#!/usr/bin/env python
# -*- coding: utf-8 -*-


config = Config()
config.test()
...
```
