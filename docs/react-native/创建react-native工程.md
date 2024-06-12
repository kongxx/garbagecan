# 创建react-native工程

注意：在运行下面命令之前不要设置代理 http_proxy 和 https_proxy 代理

``` shell
$ npx react-native init MyReactNativeApp
```

出现下面提示的时候可以先选择no（选中yes的时候安装CocoaPods总是卡住）

``` shell
。。。
Do you want to install CocoaPods now? Only needed if you run your project in Xcode directly … no
。。。
```

命令执行完成后，会有类似如下输出

``` shell
✔ Downloading template
✔ Copying template
error Error: connect ECONNREFUSED 0.0.0.0:443
    at TCPConnectWrap.afterConnect [as oncomplete] (node:net:1606:16)
info Visit https://yarnpkg.com/en/docs/cli/policies for documentation about this command.
✔ Processing template
✔ Installing dependencies
✔ Do you want to install CocoaPods now? Only needed if you run your project in Xcode directly … no


info 💡 To enable automatic CocoaPods installation when building for iOS you can create react-native.config.js with automaticPodsInstallation field. 
For more details, see https://github.com/react-native-community/cli/blob/main/docs/projects.md#projectiosautomaticpodsinstallation
            
✔ Initializing Git repository

  
  Run instructions for Android:
    • Have an Android emulator running (quickest way to get started), or a device connected.
    • cd "/Users/kongxx/test/MyReactNativeApp" && npx react-native run-android
  
  Run instructions for iOS:
    • cd "/Users/kongxx/test/MyReactNativeApp/ios"
    
    • Install Cocoapods
      • bundle install # you need to run this only once in your project.
      • bundle exec pod install
      • cd ..
    
    • npx react-native run-ios
    - or -
    • Open MyReactNativeApp/ios/MyReactNativeApp.xcodeproj in Xcode or run "xed -b ios"
    • Hit the Run button
    
  Run instructions for macOS:
    • See https://aka.ms/ReactNativeGuideMacOS for the latest up-to-date instructions.
```

其中 “error Error: connect ECONNREFUSED 0.0.0.0:443” 可以先忽略。

按照提示针对iOS，需要执行以下操作

``` shell
  Run instructions for iOS:
    • cd "/Users/kongxx/test/MyReactNativeApp/ios"
    
    • Install Cocoapods
      • bundle install # you need to run this only once in your project.
      • bundle exec pod install
      • cd ..
    
    • npx react-native run-ios
    - or -
    • Open MyReactNativeApp/ios/MyReactNativeApp.xcodeproj in Xcode or run "xed -b ios"
    • Hit the Run button
```

## 执行 bundler install

默认执行 “bundler install” 命令时，会出现挂死或超时问题，原因懂的都懂。

解决办法：

先查看一下 gem 源

```shell
$ gem sources -l
*** CURRENT SOURCES ***

https://rubygems.org/
```

修改 gem 源

```shell
gem sources --remove https://rubygems.org/ 

gem sources --add https://gems.ruby-china.com/
gem sources --add https://mirrors.tuna.tsinghua.edu.cn/rubygems/
```

查看一下修改

```shell
gem sources -l
*** CURRENT SOURCES ***

https://gems.ruby-china.com/
https://mirrors.tuna.tsinghua.edu.cn/rubygems/
```

修改 bundle 配置（这一步是不是必须待验证）

```shell
bundle config mirror.https://rubygems.org https://gems.ruby-china.com
或
bundle config mirror.https://rubygems.org https://mirrors.tuna.tsinghua.edu.cn/rubygems
```

参考：
- https://bundler.io/v2.2/man/bundle-config.1.html#MIRRORS-OF-GEM-SOURCES


## 执行 “bundle exec pod install”

在执行 “bundle exec pod install” 之前，需要设置代理

``` shell
export http_proxy=socks5://localhost:9090/
export https_proxy=socks5://localhost:9090/
```

设置 Git 代理 （这一步是否必须待验证）

``` shell
# 设置代理
git config --global http.https://github.com.proxy socks5://localhost:9090

# 取消代理
git config --global --unset http.https://github.com.proxy
```

设置代理后，可以在 ~/.gitconfig 看到下面配置

``` shell
[http "https://github.com"]
        proxy = socks5://localhost:9090
```

## 运行工程

使用 xcode 打开 ios 目录下 MyReactNativeApp.xcworkspace 文件，然后编译运行。

## 备注

上面步骤都正常后，再运行 “npx react-native init MyReactNativeApp” 创建工程的时候，就比较顺利了。但是注意这个时候不要设置 http_proxy 和 https_proxy 代理。
