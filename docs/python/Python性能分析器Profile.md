# Python性能分析器Profile

在使用Python进行开发的过程中，有时候我们需要对Python程序的执行性能进行分析，此时我们就需要用到Python的性能分析工具，这里我就简单说说Python中的profile和pstats库。

下面是一个测试程序

``` python
import os

def a():
    sum = 0
    for i in range(1, 10001):
        sum += i
    return sum

def b():
    sum = 0
    for i in range(1, 100):
        sum += a()
    return sum

print b()
```

此时我们可以使用下面的命令来查看分析结果

``` shell
python -m cProfile test.py
```

运行结果如下：

``` shell
$ python -m cProfile test.py
4950495000
         202 function calls in 0.040 seconds

   Ordered by: standard name

   ncalls  tottime  percall  cumtime  percall filename:lineno(function)
        1    0.000    0.000    0.040    0.040 test.py:1(<module>)
       99    0.035    0.000    0.040    0.000 test.py:3(a)
        1    0.000    0.000    0.040    0.040 test.py:9(b)
        1    0.000    0.000    0.000    0.000 {method 'disable' of '_lsprof.Profiler' objects}
      100    0.005    0.000    0.005    0.000 {range}
```

其中：
- tottime 是函数本身不包括它调用其它函数使用的时间
- cumtime 是函数本身包括它调用其它函数使用的时间

也可以使用下面的命令把结果保存在一个文件里

``` shell
python -m cProfile -o result test.py
```

然后使用pstats来格式化显示结果

``` shell
python -c "import pstats; pstats.Stats('result').sort_stats(-1).print_stats()"
```

``` shell
Thu Aug 11 13:20:20 2016    result

         202 function calls in 0.037 seconds

   Ordered by: standard name

   ncalls  tottime  percall  cumtime  percall filename:lineno(function)
        1    0.000    0.000    0.037    0.037 test.py:1(<module>)
       99    0.032    0.000    0.036    0.000 test.py:3(a)
        1    0.000    0.000    0.037    0.037 test.py:9(b)
        1    0.000    0.000    0.000    0.000 {method 'disable' of '_lsprof.Profiler' objects}
      100    0.004    0.000    0.004    0.000 {range}
```

另外，我们也可以在代码里直接嵌入代码来使用cProfile和pstats模块，这样在程序退出时就会自动生成分析结果并打印，如下：

``` python
import os

def a():
    sum = 0
    for i in range(1, 10001):
        sum += i
    return sum

def b():
    sum = 0
    for i in range(1, 100):
        sum += a()
    return sum

print b()

import cProfile
#cProfile.run("b()")
cProfile.run("b()", "result")

import pstats
pstats.Stats('result').sort_stats(-1).print_stats()
```

此时运行上面的程序就只要使用下面的命令

``` shell
python test.py
```
