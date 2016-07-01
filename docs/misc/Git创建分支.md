# Git创建分支

## 首先获取代码的一个干净的copy，比如
``` shell
$ git clone <...>
```

## 查看当前代码的分支情况
``` shell
$ git branch
* master
```
其中的 * 号表示当前所使用的分支

## 创建一个分支
``` shell
$ git branch release_1.0
```

上面命令中的<release_1.0>是要创建的分支名。此时再查看branch信息，如下
``` shell
$ git branch
* master
  release_1.0
```

## 提交该分支到远程仓库
``` shell
$ git push origin release_1.0
  Total 0 (delta 0), reused 0 (delta 0)
  To ...
   * [new branch]      release_1.0 -> release_1.0
```

## 切换分支
``` shell
$ git checkout release_1.0
```

然后查看分支情况，此时当前工作分支就变成release_1.0了。
``` shell
$ git branch
  master
* release_1.0
```

如果要切换回来可以运行下面命令
``` shell
$ git checkout master
$ git branch
* master
  release_1.0
```

## 提交代码到分支

如果要提交代码到分支中，首先需要切换当前工作环境到需要提交代码的分支上，然后再使用通常我们提交代码的步骤来提交代码，如下：

### 提交代码到master上
``` shell
git checkout master
git add readme.md
git commit -m 'test commit to master' readme.md
git push
```

### 提交代码到release_1.0分支上
``` shell
git checkout release_1.0
git add readme.md
git commit -m 'test commit to release_1.0 branch' readme.md
git push
```
