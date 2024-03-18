# GraphQL入门之变更（Mutation）操作

前面几篇文章都是说的查询（Query）操作，这一篇讲一下变更操作。还是老样子，看一个简单的例子。

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
}

type Mutation {
  createUser(name: String!, email: String!) : User!
}

schema {
  query: Query
  mutation: Mutation
}
```

schema 文件主要包括：

1. 定义模型对象：定义了一个User对象，包括 id, name 和 email 属性。
2. 定义查询操作：定义了一个查询操作，返回所有用户数组。
3. 定义变更操作：这里只定义了一个创建用户的变更操作，接收两个参数name和email，返回新创建的 User 对象。

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
  },

  Mutation: {
    createUser(obj, args, context, info) {
      let user = {id: users.length + 1,name: args.name, email: args.email};
      users.push(user);
      return user;
    }
  }
};

module.exports = resolvers;
```

处理器文件主要包括

1. 准备测试数据：这里创建了一个 User 对象的数据组并初始化了几条数据。
2. 定义查询处理函数：users：返回上面初始化的 User 数组。
3. 定义变更处理函数：创建一个新的 User 对象，并添加到列表中，同时作为返回值返回。其中 args 里包含了所有传递过来的参数，这里就是name和email。

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

## 测试

### 启动服务

```shell
node server.js
```

服务启动后，访问 [http://localhost:4000](http://localhost:4000) 进行测试。

### 变更操作

变更请求

```shell
mutation createUser($name: String!, $email: String!) {
  createUser(name: $name, email: $email) {
    id,
    name,
    email
  }
}
```

变更请求参数

```shell
{
  "name": "newuser",
  "email": "newuser@gmail.com"
}
```

变更返回结果，返回新创建的 User 对象

```json
{
  "data": {
    "createUser": {
      "id": "4",
      "name": "newuser",
      "email": "newuser@gmail.com"
    }
  }
}
```
