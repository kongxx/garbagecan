# express 项目支持 typescript 实战

## 首先创建一个express项目

* 初始化项目

``` shell
mkdir myapp
cd myapp
npm init (都用默认值)
```

* 添加依赖包

``` shell
npm install express nodemon
```

* 修改 package.json

``` shell
{
  "name": "myapp",
  "version": "1.0.0",
  "description": "",
  "main": "src/index.js",
  "scripts": {
    "start": "node src/index.js",
    "dev": "nodemon src/index.js"
  },
  "author": "",
  "license": "ISC",
  "dependencies": {
    "express": "^4.18.2",
    "nodemon": "^3.0.3"
  }
}
```

* 修改工程脚本 src/index.js

``` shell
const express = require('express');

const app = express();

const port = process.env.PORT || 3000;

app.get("/", (req, res, next) => {
  res.send("Express Server");
});

app.listen(port, () => {
  console.log(`[server]: Server is running at http://localhost:${port}`);
});
```

基于javascript的express创建好了，可以启动“npm run dev”验证一下。

## express + typescript 改造

* 添加typescript使用的包

``` shell
npm install -D typescript @types/express @types/node

npm install -D ts-node
```

* 生成 tsconfig.json

``` shell
npx tsc --init
```

命令运行后，会生成 tsconfig.json 文件，我们添加一下 "outDir": "./dist", 修改后内容如下：

``` shell
{
  "compilerOptions": {
    "target": "es2016",
    "module": "commonjs",
    "outDir": "./dist",
    "esModuleInterop": true,
    "forceConsistentCasingInFileNames": true,
    "strict": true,
    "skipLibCheck": true
  }
}
```

* 将 src/index.js 改名成 src/index.ts，内容改成typescript脚本

``` shell
import express, { Express, Request, Response } from "express";

const app: Express = express();
const port = process.env.PORT || 3000;

app.get("/", (req: Request, res: Response) => {
  res.send("Express + TypeScript Server");
});

app.listen(port, () => {
  console.log(`[server]: Server is running at http://localhost:${port}`);
});
```

* 修改 package.json 的 scripts，内容如下

``` shell
{
  "name": "myapp",
  "version": "1.0.0",
  "description": "",
  "main": "dist/index.js",
  "scripts": {
    "build": "npx tsc",
    "start": "node dist/index.js",
    "dev": "nodemon src/index.ts"
  },
  "author": "",
  "license": "ISC",
  "dependencies": {
    "express": "^4.18.2",
    "nodemon": "^3.0.3"
  },
  "devDependencies": {
    "@types/express": "^4.17.21",
    "@types/node": "^20.11.5",
    "ts-node": "^10.9.2",
    "typescript": "^5.3.3"
  }
}
```

到这里，express + typescript 的工程改造就完成了，可以使用 “npm run dev” 命令启动工程进行测试。
