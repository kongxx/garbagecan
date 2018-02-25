# 使用django-crontab实现定时任务

今天打算在自己的 Django 应用中添加一个定时任务来定时执行一些定期检查的功能，因此想到了使用 django-crontab 插件可以满足我的需求，下面就是这个插件的使用方法。

首先使用pip安装 django-crontab 插件
``` shell
pip install django-crontab
```

创建要定期执行的脚本和方法，这里假设脚本名称叫cron.py，内容如下：

``` python
#!/usr/bin/env python
# -*- coding: utf-8 -*-


def check():
    print "hello django-crontab"

```

然后在自己应用的 settings.py 文件中添加这个app
``` python
INSTALLED_APPS = (
    ...
    'django_crontab',
)
```

同时在 settings.py 文件中添加 CRONJOBS 配置，内容如下：
``` python
CRONJOBS = [
    ('*/1 * * * *', 'cron.check','>>/tmp/test.log')
]
```

其中：
- 第一个参数是 cron 表达式，定义定时任务的执行时间。
- 第二个参数是要执行的模块和函数。
- 第三个参数是执行定时脚本时日志文件的路径。

定义了定时任务和脚本，下面看看怎样使其生效。

首先查看一下系统中已有的 cron job

``` shell
python manage.py crontab show 
```

添加和修改 cron job

``` shell
python manage.py crontab add 
```

删除 cron job

``` shell
python manage.py crontab remove 
```
