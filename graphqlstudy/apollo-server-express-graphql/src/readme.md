# GraphQL入门之使用ApolloServer和express构建GraphQL服务

接上一篇文章，由于 express 现在仍然是主流的 Node.js 服务端框架，所以今天看看 ApolloServer 怎样和 express 集成构建 GraphQL 服务。另外今天文章也顺便讲一下怎么使用 typescript 来实现。

## 初始化项目

```shell
mkdir myapp
cd myapp
npm init (一路回车)
```

## 安装依赖包

```shell
npm install @apollo/server graphql express cors body-parser nodemon
npm install --save-dev typescript @types/cors @types/express @types/body-parser ts-node
```

这里安装了包括 graphql，apollo，express 和 typescript 相关的依赖包。

## 生成 tsconfig.json 文件

```shell
npx tsc --init
```

命令运行后，会生成 tsconfig.json 文件，我们添加一下 “outDir”: “./dist”, 修改后内容如下：

```json
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

## 修改 package.json 文件

主要修改

```json
"main": "dist/server.js",
```

和

```json
"scripts": {
 "build": "npx tsc",
 "start": "node dist/server.js",
 "dev": "nodemon src/server.ts"
 },
```

修改后的package.json文件如下

```json
{
  "name": "apollo-express",
  "version": "1.0.0",
  "description": "",
  "main": "dist/server.js",
  "scripts": {
    "build": "npx tsc",
    "start": "node dist/server.js",
    "dev": "nodemon src/server.ts"
  },
  "author": "",
  "license": "ISC",
  "dependencies": {
    "@apollo/server": "^4.10.1",
    "body-parser": "^1.20.2",
    "cors": "^2.8.5",
    "express": "^4.18.3",
    "graphql": "^16.8.1",
    "nodemon": "^3.1.0"
  },
  "devDependencies": {
    "@types/body-parser": "^1.19.5",
    "@types/cors": "^2.8.17",
    "@types/express": "^4.17.21",
    "ts-node": "^10.9.2",
    "typescript": "^5.4.2"
  }
}
```

## 服务主程序

创建 src/server.ts 文件，内容如下：

```typescript
import { ApolloServer } from '@apollo/server';
import { expressMiddleware } from '@apollo/server/express4';
import { ApolloServerPluginDrainHttpServer } from '@apollo/server/plugin/drainHttpServer'
import express from 'express';
import http from 'http';
import cors from 'cors';
import bodyParser from 'body-parser';

// 定义GraphQL的schema
const typeDefs = `#graphql
  type Query {
    hello: String
  }
`;

// 定义GraphQL的解析器
const resolvers = {
  Query: {
    hello: () => 'Hello World!',
  },
};

const app = express();
const httpServer = http.createServer(app);

// 使用schema和resolver创建ApolloServer
const server = new ApolloServer({
  typeDefs,
  resolvers,
  plugins: [ApolloServerPluginDrainHttpServer({ httpServer })],
});

// 启动ApolloServer
server.start().then(() => {
  app.use(
    cors(),
    bodyParser.json(),
    expressMiddleware(server),
  );
  
  new Promise((resolve: any) => {
    httpServer.listen({ port: 4000 }, resolve)
  }).then(() => {
    console.log(`🚀 Server ready at http://localhost:4000`);
  });
});
```

## 测试

启动服务

``` shell
npm run dev
```

访问 http://localhost:4000 进行测试，输入查询

``` json
query {
  hello
}
```

执行查询结果

``` json
{
  "data": {
    "hello": "Hello World!"
  }
}
```
