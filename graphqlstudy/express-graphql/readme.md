# GraphQL入门

## GraphQL 是什么

GraphQL 是一个用于 API 的查询语言，是一个使用基于类型系统来执行查询的服务端运行时。GraphQL 并没有和任何特定数据库或者存储引擎绑定，而是依靠你现有的代码和数据支撑。

## GraphQL Hello World

这里我门以 Node.js 的 express 框架为例，看一下 Hello World 的 GraphQL 版本。

### 首先创建一个 Node.js 的工程

``` shell
mkdir myapp
cd myapp
npm init (一路回车)
```

### 安装依赖包

``` shell
npm install express express-graphql graphql
```

### 应用启动文件

创建一个应用的启动文件，比如 server.js，内容如下：

``` shell
var express = require('express');
var { graphqlHTTP } = require('express-graphql');
var { buildSchema } = require('graphql');
 
var schema = buildSchema(`
  type Query {
    hello: String
  }
`);

var root = { hello: () => 'Hello World!' };
 
var app = express();
app.use('/graphql', graphqlHTTP({
  schema: schema,
  rootValue: root,
  graphiql: true,
}));
app.listen(4000, () => console.log('Now browse to localhost:4000/graphql'));
```

这里几个主要变量说一下：

1. schema: GraphQL 中定义的所有接口、类型和操作等，这里只定义了一个 hello 的查询操作。
2. root: GraphQL 在服务端的处理器集合，这里只有一个查询的处理器，用来处理 schema 中定义的查询操作。
3. graphqlHTTP 的第三个参数 graphiql: graphhiql是GraphQL IDE的参考实现，可以看作是为GraphQL构建的浏览器上IDE工具。

### 测试

启动服务

``` shell
node server.js
```

使用浏览器访问 http://localhost:4000/graphql，可以看到 GraphiQL IDE 的界面，输入

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

或者也可以使用 curl 命令查询

``` shell
curl -X POST http://localhost:4000/graphql -d 'query=query {
  hello
}'

{"data":{"hello":"Hello world!"}}
```
