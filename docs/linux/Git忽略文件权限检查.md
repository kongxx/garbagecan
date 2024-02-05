# Git忽略文件权限检查

自己有个Git项目的工程保存在共享存储上，然后在多个系统上操作文件的时候，总是出现虽然文件没有被修改，但是在其中某些系统上出现文件标记为变化了。并且如果使用 git status 检查文件的时候，出现类似下面的输出

``` shell
$ git diff test.js
diff --git a/test.js b/test.js
old mode 100644
new mode 100755
```

这个问题的原因是文件在两个系统上权限不一致，因此可以使用下面命令让其忽略文件权限检查

``` shell
$ git config core.fileMode false
```

也可以在git的全局范围内生效

``` shell
git config --global core.filemode false
```

也可以通过修改 “~/.gitconfig” 文件，在其中添加下面内容 

``` shell
[core]
        filemode = false
```
