# Python判断某个用户对某个文件的权限
在Python我们要判断一个文件对当前用户有没有读、写、执行权限，我们通常可以使用os.access函数来实现，比如：
``` python
# 判断读权限
os.access(<my file>, os.R_OK)
# 判断写权限
os.access(<my file>, os.W_OK)
# 判断执行权限
os.access(<my file>, os.X_OK)
# 判断读、写、执行权限
os.access(<my file>, os.R_OK | os.W_OK | os.X_OK)
```

但是如果要判断任意一个指定的用户对某个文件是否有读、写、执行权限，Python中是没有默认实现的，此时我们可以通过下面的代码断来判断
``` python
import os
import pwd
import stat

def is_readable(cls, path, user):
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

def is_writable(cls, path, user):
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

def is_executable(cls, path, user):
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
