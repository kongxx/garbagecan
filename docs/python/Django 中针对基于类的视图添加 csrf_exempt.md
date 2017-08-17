# Django 中针对基于类的视图添加 csrf_exempt

在Django中对于基于函数的视图我们可以 @csrf_exempt 注解来标识一个视图可以被跨域访问。那么对于基于类的视图，我们应该怎么办呢？

简单来说可以有两种访问来解决

## 方法一：在类的 dispatch 方法上使用 @csrf_exempt

``` python
from django.views.decorators.csrf import csrf_exempt

class MyView(View):

    def get(self, request):
        return HttpResponse("hi")

    def post(self, request):
        return HttpResponse("hi")

    @csrf_exempt
    def dispatch(self, *args, **kwargs):
        return super(MyView, self).dispatch(*args, **kwargs)
```

## 方法二：在 urls.py 中配置

``` python
from django.conf.urls import url
from django.views.decorators.csrf import csrf_exempt
import views

urlpatterns = [
    url(r'^myview/$', csrf_exempt(views.MyView.as_view()), name='myview'),
]

```
