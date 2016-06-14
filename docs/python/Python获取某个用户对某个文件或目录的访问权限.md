# Python中获取某个用户对某个文件或目录的访问权限

在Python中我们通常可以使用os.access()函数来获取当前用户对某个文件或目录是否有某种权限，但是要获取某个用户对某个文件或目录是否有某种权限python中没有很好的方法直接获取，因此我写了个函数使用stat和pwd模块来实现这一功能。

``` python
#!/usr/bin/env python
# -*- coding: utf-8 -*-
import os
import pwd
import stat

def is_readable(path, user):
    user_info = pwd.getpwnam(user)
    uid = user_info.pw_uid
    gid = user_info.pw_gid
    s = os.stat(path)
    mode = s[stat.ST_MODE]
    return (
        ((s[stat.ST_UID] == uid) and (mode & stat.S_IRUSR > 0)) or
        ((s[stat.ST_GID] == gid) and (mode & stat.S_IRGRP > 0)) or
        (mode & stat.S_IROTH > 0)
     )

def is_writable(path, user):
    user_info = pwd.getpwnam(user)
    uid = user_info.pw_uid
    gid = user_info.pw_gid
    s = os.stat(path)
    mode = s[stat.ST_MODE]
    return (
        ((s[stat.ST_UID] == uid) and (mode & stat.S_IWUSR > 0)) or
        ((s[stat.ST_GID] == gid) and (mode & stat.S_IWGRP > 0)) or
        (mode & stat.S_IWOTH > 0)
     )

def is_executable(path, user):
    user_info = pwd.getpwnam(user)
    uid = user_info.pw_uid
    gid = user_info.pw_gid
    s = os.stat(path)
    mode = s[stat.ST_MODE]
    return (
        ((s[stat.ST_UID] == uid) and (mode & stat.S_IXUSR > 0)) or
        ((s[stat.ST_GID] == gid) and (mode & stat.S_IXGRP > 0)) or
        (mode & stat.S_IXOTH > 0)
     )
```

使用方法

``` python
print is_readable('/home', root)
print is_writable('/home', root)
print is_executable('/home', root)

print is_readable('/tmp', admin)
print is_writable('/tmp', admin)
print is_executable('/tmp', admin)
```
