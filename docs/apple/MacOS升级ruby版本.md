# MacOS升级ruby版本

MacOS自带ruby版本是2.x，可以通过“ruby -v”查看版本号

``` shell
$ ruby -v
ruby 2.6.10p210 (2022-04-12 revision 67958) [universal.x86_64-darwin22]
```

homebrew安装的ruby版本号可以通过“brew info ruby”命令参看

``` shell
$ brew info ruby
==> ruby: stable 3.3.2 (bottled), HEAD [keg-only]
Powerful, clean, object-oriented scripting language
https://www.ruby-lang.org/
```

如果没有安装，可以“brew install ruby”进行安装

``` shell
$ brew install ruby
Warning: No remote 'origin' in /usr/local/Homebrew/Library/Taps/homebrew/homebrew-services, skipping update!
Warning: ruby 3.3.2 is already installed and up-to-date.
...
```

或者通过“brew install ruby”进行升级

``` shell
$ brew upgrade ruby
Warning: No remote 'origin' in /usr/local/Homebrew/Library/Taps/homebrew/homebrew-services, skipping update!
Warning: ruby 3.3.2 already installed
```

但是如果我们使用“ruby -v”查看版本，发现使用的还是MacOS自带ruby版本是2.x。这里就需要将homebrew中ruby环境加到系统PATH里，如下：

``` shell
$ echo 'export PATH="/usr/local/opt/ruby/bin:$PATH"' >> ~/.profile

$ source ~/.profile
```

再次使用“ruby -v”查看一下版本

``` shell
$ ruby -v
ruby 3.3.2 (2024-05-30 revision e5a195edf6) [x86_64-darwin22]
```
