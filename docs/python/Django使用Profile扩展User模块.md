# Django使用Profile扩展User模块

## 首先创建Profile应用

``` python
python manage.py startapp profiles
```

## profiles/models.py
``` python
# -*- coding: utf-8 -*-
from django.db import models
from django.contrib.auth.models import User
from django.db.models.signals import post_save


class UserProfile(models.Model):
    user = models.OneToOneField(User)
    nickname = models.CharField(max_length=16, default='', blank=True)
    sex = models.IntegerField(default=0)
    phone = models.CharField(max_length=16, default='', blank=True)

    def __str__(self):
        return self.nickname

    def __unicode__(self):
        return self.nickname


def create_user_profile(sender, instance, created, **kwargs):
    if created:
        profile = UserProfile()
        profile.user = instance
        profile.save()

post_save.connect(create_user_profile, sender=User)
```

## profiles/admin.py
``` python
# -*- coding: utf-8 -*-
from django.contrib import admin
from django.contrib.auth.models import User
from django.contrib.auth.admin import UserAdmin
from .models import UserProfile


class ProfileInline(admin.StackedInline):
    model = UserProfile
    max_num = 1
    can_delete = False


class UserProfileAdmin(UserAdmin):
    inlines = [ProfileInline, ]


admin.site.unregister(User)
admin.site.register(User, UserProfileAdmin)
```

## settings.py
添加
``` python
AUTH_PROFILE_MODULE = 'profiles.UserProfile'
```
## 测试
``` bash
$ python manage.py syncdb
$ python manage.py shell
>>> from django.contrib.auth.models import User
>>> user = User()
>>> user.username='testuser'
>>> user.save()
>>> User.objects.all()[0].userprofile
```
