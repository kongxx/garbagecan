# GraphQL入门之使用ApolloServer构建GraphQL服务

接上一篇文章，上一篇通过 express 作为 GraphQL Server 介绍了一下 GraphQL，今天说一下如何使用 Apollo Server 作为 GraphQL服务器。

## 创建 Node.js 的工程

``` shell
mkdir myapp
cd myapp
npm init (一路回车)
```

## 安装依赖包

``` shell
npm install @apollo/server graphql
```

## 定义 Schema

``` shell
const typeDefs = `
  type Query {
    hello: String
  }
`;
```

## 定义解析器

``` shell
const resolvers = {
  Query: {
    hello: () => 'Hello world!',
  },
};
```

## 创建ApolloServer

使用上面定义的 schema 和 resolver 创建 ApolloServer

``` shell
const server = new ApolloServer({
  typeDefs,
  resolvers,
});
```

## 启动ApolloServer

``` shell
startStandaloneServer(server).then(function(data) {
  console.log(`🚀 Server ready at ${data.url}`);
});
```

## 服务完整代码

在工程下创建 server.js，完整代码内容如下：

``` shell
const { ApolloServer } =  require('@apollo/server');
const { startStandaloneServer } = require('@apollo/server/standalone');

// The GraphQL schema
const typeDefs = `
  type Query {
    hello: String
  }
`;

// A map of functions which return data for the schema.
const resolvers = {
  Query: {
    hello: () => 'Hello world!',
  },
};

const server = new ApolloServer({
  typeDefs,
  resolvers,
});

startStandaloneServer(server).then(function(data) {
  console.log(`🚀 Server ready at ${data.url}`);
});
```

## 测试

启动服务

``` shell
node server.js
```

使用浏览器访问 http://localhost:4000/，可以看到 Apollo Server 的 IDE 的界面，输入

``` shell
query {
  hello
}
```

可以看到下面的查询结果

``` shell
{
  "data": {
    "hello": "Hello world!"
  }
}
```
