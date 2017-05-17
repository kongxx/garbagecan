# Django自定义访问日志模块

在Django默认没有访问日志模块，但是我们可以通过Django的Middleware来实现一个自己的访问日志模块。

首先在Django的工程下创建一个middleware.py文件，内容如下：
``` python
#!/usr/bin/env python
# -*- coding: utf-8 -*-
import time


class AccessMiddleware(object):

    def process_request(self, request):
        meta = request.META
        print "[%s] PATH_INFO=%s, REMOTE_ADDR=%s, HTTP_USER_AGENT=%s" \
              %(time.strftime("%Y-%m-%d %H:%M:%S", time.localtime()),
                meta['PATH_INFO'], meta['REMOTE_ADDR'], meta['HTTP_USER_AGENT'])
        return None

    def process_response(self, request, response):
        return response

```

其中 process_request() 应当返回 None 或 HttpResponse 对象。

- 如果返回 None , Django将继续处理这个 request , 执行后续的中间件， 然后调用相应的view.

- 如果返回 HttpResponse 对象, Django 将不再执行 任何 其它的中间件(不论种类)以及相应的view. Django将立即返回该 HttpResponse .

然后修改settings.py文件，在MIDDLEWARE_CLASSES部分加入上面创建的AccessMiddleware，比如：（见最后面一行）
``` python
MIDDLEWARE_CLASSES = (
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.auth.middleware.SessionAuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
    'django.middleware.security.SecurityMiddleware',
    'commons.middleware.AccessMiddleware',
)
```

重启服务，然后再访问任意一个页面就会看到有日志输出了。
