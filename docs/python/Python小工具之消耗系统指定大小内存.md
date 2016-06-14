# Python小工具之消耗系统指定大小内存

工作中需要根据某个应用程序具体吃了多少内存来决定执行某些操作，所以需要写个小工具来模拟应用程序使用内存情况，下面是我写的一个Python脚本的实现。

``` python
#!/usr/bin/python
# -*- coding: utf-8 -*-
import sys
import re
import time

def print_help():
    print 'Usage: '
    print '  python mem.py 100MB'
    print '  python mem.py 1GB'

if __name__ == "__main__":
    if len(sys.argv) == 2:
        pattern = re.compile('^(\d*)([M|G]B)$')
        match = pattern.match(sys.argv[1].upper())
        if match:
            num = int(match.group(1))
            unit = match.group(2)
            if unit == 'MB':
                s = ' ' * (num * 1024 * 1024)
            else:
                s = ' ' * (num * 1024 * 1024 * 1024)

            time.sleep(10000)
        else:
            print_help()
    else:
        print_help()
```

使用方法如下：

``` shell
python mem.py 100M
python mem.py 1G
```
