# 清除Mac OS上Xcode占用的空间

最近自己的Mac OS存储空间严重不足，想了一下，大概是从安装 Xcode 之后出现，在系统下通过 du 命令分析各目录大小，发现大概下面几个目录占用空间比较大，所以针对这几个名目录作了一下清理，释放了几十个G的空间。

1. 无效的设备: ~/Library/Developer/CoreSimulator/Devices
   
   可以使用下面命令清理
   
   ```shell
   xcrun simctl delete unavailable
   ```

2. 下载的模拟器: ~/Library/Developer/Xcode/iOS DeviceSupport，可根据情况手动删除其下目录。

3. 旧的应用程序存档: ~/Library/Developer/Xcode/Archives，可根据情况手动删除其下目录。
