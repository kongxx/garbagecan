# Django使用自定义认证方式

## 创建登录应用
首先创建一个新的login app，用来存放认证用到代码
``` bash
python manage.py startapp login
```

## 修改settings.py中的认证项
AUTHENTICATION_BACKENDS = (
    'login.auth.UsernamePasswordAuth',
)

## 自定义认证类
在login app下创建auth.py文件，内容如下
``` python
#coding:utf-8
from django.contrib.auth.models import User


class UsernamePasswordAuth(object):

    def authenticate(self, username=None, password=None):
        print("UsernamePasswordAuth.authenticate")
        try:
            user = User.objects.get(username__iexact=username)
            if user.check_password(password):
                return user
        except User.DoesNotExist:
            return None

    def get_user(self, user_id):
        print("UsernamePasswordAuth.get_user")
        try:
            user = User.objects.get(pk=user_id)
            return user
        except User.DoesNotExist:
            return None
```

# Django rest framework 使用自定义认证方式
上一篇文章介绍了 “Django使用自定义认证方式”，这一篇说说怎样在前一篇的基础上提供rest api。

## 修改settings.py中INSTALLED_APPS，添加 'login' app。

## 给login app增加serializers.py文件
``` python
#coding:utf-8
from django.contrib.auth.models import User
from rest_framework import serializers

class LoginSerializer(serializers.ModelSerializer):

    username = serializers.CharField(required=False, max_length=1024)
    password = serializers.CharField(required=False, max_length=1024)

    class Meta:
        model = User
        fields = ('id', 'username', 'password')

```

## 修改login app的views.py文件
``` python
#coding:utf-8
from rest_framework import generics, viewsets, mixins, status
from rest_framework.response import Response
from rest_framework.views import APIView
from .serializers import *


class LoginViewSet(APIView):
    queryset = User.objects.all()
    serializer_class = LoginSerializer

    def post(self, request):
        try:
            username = request.data.get('username')
            password = request.data.get('password')
            user = User.objects.get(username__iexact=username)
            if user.check_password(password):
                print user
                serializer = LoginSerializer({'id': user.id, 'username': user.username})
                return Response(serializer.data)
            return Response(status=status.HTTP_401_UNAUTHORIZED)
        except User.DoesNotExist:
            return Response(status=status.HTTP_401_UNAUTHORIZED)

```

## 给login app增加urls.py文件
``` python
from django.conf.urls import url, include
from rest_framework import routers
from .views import *

urlpatterns = [
    url(r'^api/login$', LoginViewSet.as_view()),
]
```

## 修改project/urls.py文件，添加 'login' app的url配置。
``` python
urlpatterns = [
...
    url(r'^login/', include('login.urls')),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
...
]
```

## 测试
运行工程，然后访问 http://localhost:8000/login/api/login
