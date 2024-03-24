# Git钩子工具lefthook

## lefthook 是什么

lefthook是由 Go 语言开发的适用于多种编程语言项目的快速且强大的 Git 钩子管理器，它可以在项目构建过程中执行一些任务，例如检查代码格式、检查依赖、测试、覆盖率检查、静态分析、持续集成等。官方介绍它的几个特点：
快速，它是用 Go 编写的，可以并行运行命令。
强大，它可以控制执行通过命令传递的执行和文件。
简单，它是一个无依赖的二进制文件，可以在任何环境中工作。

下面看看我们怎样在 Git 仓库中使用 lefthook。

## 安装 lefthook

```shell
npm install lefthook --save-dev
```

安装完成后会在项目的根目录下创建一个 lefthook.yml 文件，如果没有产生的话也可以自己创建一个。lefthook 的配置文件可以使用下面这些文件名。

- `lefthook.yml`
- `.lefthook.yml`
- `lefthook.yaml`
- `.lefthook.yaml`
- `lefthook.toml`
- `.lefthook.toml`
- `lefthook.json`
- `.lefthook.json`

## 一个例子

看一个简单的例子，看看 lefthook 怎么使用，修改 lefthook.yml 文件，定义 pre-commit，post-commit，pre-push 三个钩子，如下所示：

```yaml
pre-commit:
  parallel: true
  scripts:
    "pre-commit.sh":
      runner: bash

post-commit:
  parallel: true
  scripts:
    "post-commit.sh":
      runner: bash

pre-push:
  parallel: true
  scripts:
    "pre-push.js":
      runner: node
```

这里定义了三个钩子，分别对应 .lefthook/pre-commit/pre-commit.sh，.lefthook/post-commit/post-commit.sh，.lefthook/pre-push/pre-push.js 三个脚本。这里脚本路径必须放在对应的钩子文件夹下，内容如下：

- .lefthook/pre-commit/pre-commit.sh

```shell
#!/bin/sh

echo 'pre-commiit ...'
```

- .lefthook/post-commit/post-commit.sh

```shell
#!/bin/sh

echo 'post-commiit ...'
```

- .lefthook/pre-push/pre-push.js

```shell
console.log('pre-push ...');
```

接下来，执行 git commit 命令，来触发 pre-commit 和 post-commit hooks。

```shell
$ git commit -m 'test' src/index.ts

╭──────────────────────────────────────╮
│ 🥊 lefthook v1.6.7  hook: pre-commit │
╰──────────────────────────────────────╯
sync hooks: ✔️ (pre-commit, pre-push, post-commit, post-push)
┃  pre-commit.sh ❯ 

pre-commiit ...


  ────────────────────────────────────
summary: (done in 0.05 seconds)       
✔️  pre-commit.sh
╭───────────────────────────────────────╮
│ 🥊 lefthook v1.6.7  hook: post-commit │
╰───────────────────────────────────────╯
┃  post-commit.sh ❯ 

post-commiit ...


  ────────────────────────────────────
summary: (done in 0.13 seconds)       
✔️  post-commit.sh
[main 98d62d5] test
 1 file changed, 1 insertion(+), 1 deletion(-)
```

执行 git push 命令，来触发 pre-push hook。

```shell
$ git push

╭────────────────────────────────────╮
│ 🥊 lefthook v1.6.7  hook: pre-push │
╰────────────────────────────────────╯
┃  pre-push.js ❯ 

pre-push ...


  ────────────────────────────────────
summary: (done in 1.98 seconds)       
✔️  pre-push.js
...
```

上面例子仅仅演示了 lefthook 怎么在 Git 命令的不同阶段触发钩子脚本，这里只定义了三个钩子并简单的打印了一些字符串，在实际应用中，你可以使用 lefthook 的不同钩子来自动执行一些任务，这些任务可以直接写在 lefthook 的配置文件中，也可以写成一个单独的脚本文件，然后通过 lefthook 调用。

## 又一个例子

下面看一个实际的应用场景，在使用 Git 命令提交代码时，使用 eslint 静态分析代码质量，使用 prettier 格式化代码。

### 配置 lefthook

修改 lefthook.yml 文件，内容如下：

``` yaml
pre-commit:
    parallel: true
    commands:
        eslint:
            glob: "*.{js,ts,jsx,tsx}"
            run: yarn eslint "{staged_files}"
        prettier:
            glob: "*{js,ts,jsx,tsx,html,md,yml,yaml,json}"
            run: yarn prettier --write "{staged_files}"
```

### 安装配置 eslint

ESLint 是一个代码静态分析工具，静态分析代码以快速发现问题。ESLint 做了两件事,一件是修复代码质量,另一件就是修复代码格式。但 ESlint 的代码格式做的不够彻底，所以后面会使用 prettier 来增强。

- 安装 eslint

``` shell
$ npm install -D eslint eslint-config-prettier
```

- 配置 eslint

``` shell
$ npm init @eslint/config (根据自己项目情况选择，命令执行后会在根目录下生成 .eslintrc.js 文件)
```

### 安装配置 prettier

- 安装 prettier

``` shell
$ npm install --save-dev --save-exact prettier
```

- 配置 prettier

在项目根目录下创建 .prettierrc 文件，这里简单配置了几项

``` json
{
    "printWidth": 120, 
    "tabWidth": 4,
    "useTabs": false,
    "endOfLine": "lf",
    "trailingComma": "es5"
}
```

在项目根目录下创建 .prettierignore 文件

``` shell
node_modules/
dist/
```

### 测试

执行 git commit 命令，可以看到 eslint 和 prettier 自动执行了，并且代码已经自动修复和格式化了。

``` shell
$ git commit -m 'test' src/index.ts 
╭──────────────────────────────────────╮
│ 🥊 lefthook v1.6.7  hook: pre-commit │
╰──────────────────────────────────────╯
│  pre-commit.sh (skip) not specified in config file
┃  prettier ❯ 

yarn run v1.22.19
$ /Users/fkong/workspace/GitHub/kongxx/my-express/node_modules/.bin/prettier --write src/index.ts
src/index.ts 296ms
✨  Done in 0.78s.

┃  eslint ❯ 

yarn run v1.22.19
$ /Users/fkong/workspace/GitHub/kongxx/my-express/node_modules/.bin/eslint src/index.ts
✨  Done in 1.86s.

                                      
  ────────────────────────────────────
summary: (done in 2.15 seconds)       
✔️  prettier
✔️  eslint
╭──────────────────────────────────────╮
│ 🥊 lefthook v1.6.7  hook: commit-msg │
╰──────────────────────────────────────╯
╭───────────────────────────────────────╮
│ 🥊 lefthook v1.6.7  hook: post-commit │
╰───────────────────────────────────────╯
[main efed81d] test
 1 file changed, 2 insertions(+), 2 deletions(-)
```
