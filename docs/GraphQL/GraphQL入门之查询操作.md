# GraphQL入门之查询操作

接前面几篇文章，GraphQL 支持的数据操作有：

- 查询（Query）： 获取数据的基本查询。
- 变更（Mutation）： 对数据的增删改等操作。
- 订阅（Subscription）： 用于监听数据变动并协议推送变动的消息。

今天先看一下怎么执行一个简单的 Query 操作。

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
    users: [User],
    user(id: ID!): User,
}
```

schema 文件主要包括：

1. 定义模型对象：定义了一个User对象，包括 id, name 和 email 属性。
2. 定义查询操作：定义了两个查询操作，users查询所有用户，user(id)根据id查询用户。

## 实现处理器

创建 resolvers.js 文件，内容如下：

```shell
const user1 = {id: 1, name: 'user1', email: 'user1@gmail.com'};
const user2 = {id: 2, name: 'user2', email: 'user2@gmail.com'};
const user3 = {id: 3, name: 'user3', email: 'user3@gmail.com'};
const users = [user1, user2, user3];

const resolvers = {
  Query: {
    users: () => users,

    user(obj, args, context, info) {
      for (let user of users) {
        if (user.id == args.id) {
          return user;
        }
      }
      return null;
    },
  },
};

module.exports = resolvers;
```

处理器文件主要包括

1. 准备测试数据：这里创建了一个 User 对象的数据组并初始化了几条数据。
2. 定义处理函数：
    users：返回上面初时的 User 数组。
    user(obj, args, context, info)：根据传入的 id 在 User 数组中查找，并返回查询结果。

## 主程序

创建 server.js 文件，内容如下：

```
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
