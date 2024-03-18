# GraphQL入门之查询指令Directive

接前面的文章，考虑这么一种场景，有时候我们希望根据某些参数条件来决定返回某些字段。比如下面的查询操作，有时候我们希望返回name字段，有时候不希望。

``` shell
query GetUsers {
  users {
    id,
    name,
    email
  }
}
```

此时我们就可以使用 GraphQL 的指令 Directive 来解决这个问题。GraphQL 中使用标识符@+已命名的参数来实现。GraphQL 规范默认支持了几个指令：
    @deprecated(reason: String) - 将字段标记为已弃用并说明原因
    @skip (if: Boolean) - 如果条件满足则跳过该字段
    @include (if: Boolean) - 如果条件满足则包括该字段

下面举个列子看一下。

## 创建 Node.js 的工程

```shell
mkdir myapp
cd myapp
npm init (一路回车)
```

## 安装依赖包

```shell
npm install @apollo/server graphql
```

## 定义 Schema

创建 schema.graphql 文件，内容如下：

```shell
type User {
    id: ID!
    name: String!
    email: String!
}

type Query {
    users(includeName: Boolean, skipId: Boolean): [User],
}
```

schema 文件主要包括：

1. 定义模型对象：定义了一个User对象，包括 id, name 和 email 属性。
2. 定义查询操作：定义了一个查询操作，返回用户列表。查询接受两个参数，includeName是否包括name字段，skipId是否跳过id字段。

## 实现处理器

创建 resolvers.js 文件，内容如下：

``` javascript
const user1 = {id: 1, name: 'user1', email: 'user1@gmail.com'};
const user2 = {id: 2, name: 'user2', email: 'user2@gmail.com'};
const user3 = {id: 3, name: 'user3', email: 'user3@gmail.com'};
const users = [user1, user2, user3];

const resolvers = {
  Query: {
    users(obj, args, context, info) {
      return users;
    },
  },
};

module.exports = resolvers;
```

处理器文件主要包括

1. 准备测试数据：这里创建了一个 User 对象的数据组并初始化了几条数据。
2. 定义处理函数：不做特殊处理，直接返回所有 User 的数组。

## 主程序

创建 server.js 文件，内容如下：

``` javascript
const { ApolloServer } =  require('@apollo/server');
const { startStandaloneServer } = require('@apollo/server/standalone');
const fs = require("fs");

const typeDefs = fs.readFileSync('./schema.graphql').toString();
const resolvers = require('./resolvers');

const server = new ApolloServer({
  typeDefs,
  resolvers,
});

startStandaloneServer(server).then(function(data) {
  console.log(`🚀 Server ready at ${data.url}`);
});
```

## 测试

### 启动服务

```shell
node server.js
```

服务启动后，访问 [http://localhost:4000](http://localhost:4000) 进行测试。

### 列表操作

查询操作

```shell
query GetUsers($includeName: Boolean!, $skipId: Boolean!) {
  users(includeName: $includeName, skipId: $skipId) {
    id @skip(if: $skipId),
    name @include(if: $includeName),
    email
  }
}
```

查询参数

``` json
{
  "includeName": false,
  "skipId": true
}
```

查询结果

``` json
{
  "data": {
    "users": [
      {
        "email": "user1@gmail.com"
      },
      {
        "email": "user2@gmail.com"
      },
      {
        "email": "user3@gmail.com"
      }
    ]
  }
}
```
