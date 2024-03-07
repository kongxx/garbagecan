# Ubuntu visudo设置默认编辑器

现在 Ubuntu 上使用 visudo 编辑 sudoers 的时候，默认使用的编辑器不是vi，而是变成了editor，真真的是不习惯呀，今天发现可以在 visudo 里修改。

使用 “sudo visudo” 编辑 sudoers 文件，在其中添加下面内容

``` shell
Defaults editor=/usr/bin/vim, env_editor
```

保存，然后再次运行 “sudo visudo” 的时候，就会切换成 vim 编辑器了。
