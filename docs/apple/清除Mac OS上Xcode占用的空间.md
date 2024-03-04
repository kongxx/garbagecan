# 清除Mac OS上Xcode占用的空间

1. 无效的设备: ~/Library/Developer/CoreSimulator/Devices
   
   可以使用下面命令清理
   
   ```shell
   xcrun simctl delete unavailable
   ```

2. 下载的模拟器: ~/Library/Developer/Xcode/iOS DeviceSupport，可根据情况手动删除。

3. 旧的应用程序存档: ~/Library/Developer/Xcode/Archives，可根据情况手动删除。
