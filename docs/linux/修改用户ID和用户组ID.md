# 修改用户ID和用户组ID

假定原用户和用户组id如下：
``` shell
用户 jhadmin 500
组   jhadmin 500
```

要修改成用户和组id如下：
``` shell
用户 jhadmin 1000
组   jhadmin 1000
```

## 修改用户ID
``` shell
usermod -u 1000 jhadmin
```

## 修改组ID
``` shell
groupmod -g 1000 jhadmin
```

## 修改文件权限
``` shell
find /home -group 500 -exec chgrp -h jhadmin {} \;
find / -user 500 -exec chown -h jhadmin {} \;
find / -group 500 -exec chgrp -h jhadmin {} \;
```