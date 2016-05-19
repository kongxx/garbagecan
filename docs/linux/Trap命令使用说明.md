# Trap命令使用说明

trap命令用于指定在接收到信号后将要采取的行动。trap命令的一种常见用途是在脚本程序被中断时完成清理工作。可以使用trap -l来查看具体信号列表。

trap捕捉到信号之后，可以有三种响应方式：

1. 执行一段程序来处理这一信号，比如：
``` shell
trap 'commands' signal-list
```

2. 接受信号的默认操作，比如：
``` shell
trap signal-list
```

3. 忽视这一信号，比如：
``` shell
trap " " signal-list
```

下面看一个例子，将下面代码保存为test.sh

``` shell
#!/bin/sh

function stop() {
  echo 'stop'
}
function resume() {
  echo 'resume'
}
function cleanup() {
	echo 'clean up'
}

trap stop TSTP
trap resume CONT
trap cleanup EXIT QUIT

while true
do
    date
    sleep 2
done
```

运行上面的测试脚本，然后分别运行下面的kill命令来观察脚本的输出

``` shell
kill -20 <pid>
kill -18 <pid>
kill <pid>
```
