# Linux find 命令使用小技巧一则

今天需要清理一下原来 dailybuild 的产生的一些用来存放包的目录，这些目录名字是按照一定规则存放，比如：

``` bash
/dailybuild/[module_name]/[yyyy]-[MM]-[dd]/xxx
```

这里考虑仅需要保存最近5天的dailybuild，对于早于5天的目录执行删除操作。

查看了一下find命令说明，可以
* 使用 “-type d” 参数来只查找目录，忽略文件。
* 使用 “-mtime +5” 参数来指定只查找5天前有更改的目录
  其中 “-mtime -n/+n ” 是指按文件更改时间来查找文件，-n指n天以内，+n指n天以前

所以最终脚本内容大体如下：
``` bash
#!/bin/sh
cd /dailybuild/
files=`find ./* -name "*-*-*" -type d -mtime +5 -print`
for file in $files
do
	rm -rf $file
done
```

# util /lib64/ld-lsb-x86-64.so.3: bad ELF interpreter

## 问题描述
运行lmutil命令的时候出现下面错误
``` bash
./lmutil
bash: ./lmutil: /lib64/ld-lsb-x86-64.so.3: bad ELF interpreter: No such file or directory
```

## 解决办法
安装redhat-lsb包
``` bash
sudo yum -y install redhat-lsb
```


# ----------------
sudo docker search centos-5.5
sudo docker pull gpmidi/centos-5.5