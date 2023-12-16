# Android终端模拟器Termux上使用Ubuntu

Termux 上安装各种 Linux 系统是通过 proot-distro 工具来实现的，所以先安装一下 proot-distro 工具。

``` shell
~ $ pkg install proot-distro 
```

查看Termux支持安装那些Linux

``` shell
~ $ proot-distro list

Supported distributions:

  * Alpine Linux

    Alias: alpine
    Installed: no
    Comment: Rolling release branch (edge).

  * Arch Linux

    Alias: archlinux
    Installed: no
    Comment: Currently available only AArch64 and ARM ports.

  * Artix Linux

    Alias: artix
    Installed: no
    Comment: Currently available only for AArch64.

  * Debian

    Alias: debian
    Installed: no
    Comment: A stable release (bookworm).

  * deepin

    Alias: deepin
    Installed: no
    Comment: Supports only 64-bit CPUs.

  * Fedora

    Alias: fedora
    Installed: no
    Comment: Version 39. Supports only 64-bit CPUs.

  * Manjaro

    Alias: manjaro
    Installed: no
    Comment: Currently available only for AArch64.

  * OpenSUSE

    Alias: opensuse
    Installed: no
    Comment: Rolling release (Tumbleweed).

  * Pardus (yirmibir)

    Alias: pardus
    Installed: no

  * Ubuntu

    Alias: ubuntu
    Installed: no
    Comment: Standard release (mantic). Not available for x86 32-bit (i686) CPUs.

  * Void Linux

    Alias: void
    Installed: no

Install selected one with: proot-distro install <alias>
```

这里我们选择安装 Ubuntu 系统

``` shell
~ $ proot-distro install ubuntu
```

安装完成后，登录 Ubuntu 系统

``` shell
~ $ proot-distro login ubuntu
root@localhost:~# 
```

查看一下 Ubuntu 版本

``` shell
root@localhost:~# cat /etc/os-release 
PRETTY_NAME="Ubuntu 23.10"
NAME="Ubuntu"
VERSION_ID="23.10"
VERSION="23.10 (Mantic Minotaur)"
VERSION_CODENAME=mantic
ID=ubuntu
ID_LIKE=debian
HOME_URL="https://www.ubuntu.com/"
SUPPORT_URL="https://help.ubuntu.com/"
BUG_REPORT_URL="https://bugs.launchpad.net/ubuntu/"
PRIVACY_POLICY_URL="https://www.ubuntu.com/legal/terms-and-policies/privacy-policy"
UBUNTU_CODENAME=mantic
LOGO=ubuntu-logo
```

最后提一句，安装过程如果出现下面的错误

``` shell
CANNOT LINK EXECUTABLE "curl": library "libssl.so.1.1" not found: needed by /data/data/com.termux/files/usr/lib/libssh2.so in namespace (default)
```

可以执行一下完整的升级操作，再安装

``` shell
apt full-upgrade
```
