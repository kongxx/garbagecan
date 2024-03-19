# GraphQL入门之自定义标量类型

GraphQL 默认支持五种标量类型：Int，Float，String，Boolean 和 ID，可以满足大部分的使用场景，但有时候需要一些特殊的属性类型，此时我们就可以使用自定义标量类型来实现。下面看一下怎么通过自定义标量类型来实现一个 DateTime 类型。

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

``` shell
scalar DateTime

type User {
    id: ID!
    name: String!
    email: String!
    registerDateTime: DateTime!
}

type Query {
    users: [User],
    
    user(registerDateTime: DateTime!): User,
}

type Mutation {
  createUser(name: String!, email: String!, registerDateTime: DateTime!) : User!
}

schema {
  query: Query
  mutation: Mutation
}

```

schema 文件主要包括：

1. 定义了一个标量类型 DateTime
2. 在用户对象中添加了一个标量类型的属性 registerDateTime
3. 查询操作的时候按用户 registerDateTime 来查找
4. 变更操作的时候添加了一个 registerDateTime 的标量类型

## 实现处理器

创建 resolvers.js 文件，内容如下：

``` shell
const { GraphQLScalarType, Kind } = require('graphql');

const user1 = { id: 1, name: 'user1', email: 'user1@gmail.com', registerDateTime: new Date('2000-01-01T10:10:10.000Z') };
const user2 = { id: 2, name: 'user2', email: 'user2@gmail.com', registerDateTime: new Date('2000-01-01T10:10:10.000Z') };
const user3 = { id: 3, name: 'user3', email: 'user3@gmail.com', registerDateTime: new Date('2000-01-01T10:10:10.000Z') };
const users = [user1, user2, user3];

const datetimeScalar = new GraphQLScalarType({
  name: 'DateTime',
  description: 'DateTime custom scalar type',

  serialize(value) {
    if (value instanceof Date) {
      return value.toISOString();
    }
    throw Error('GraphQL Date Scalar serializer expected a `Date` object');
  },

  parseValue(value) {
    if (typeof value === 'string') {
      return new Date(value);
    }
    throw new Error('GraphQL Date Scalar parser expected a `string`');
  },

  parseLiteral(ast) {
    if (ast.kind === Kind.STRING) {
      return new Date(ast.value);
    }
    throw new Error('GraphQL Date Scalar invalid');
  },
});

const resolvers = {
  DateTime: datetimeScalar,

  Query: {
    users: () => users,

    user(obj, args, context, info) {
      for (let user of users) {
        if (user.registerDateTime.getTime() == args.registerDateTime.getTime()) {
          return user;
        }
      }
      return null;
    },
  },

  Mutation: {
    createUser(obj, args, context, info) {
      let user = { id: users.length + 1, name: args.name, email: args.email, registerDateTime: args.registerDateTime };
      users.push(user);
      return user;
    }
  }
};

module.exports = resolvers;
```

处理器文件主要包括：

1. 初始化数据的时候添加了DateTime类型属性的初始化
2. 定义了一个 GraphQLScalarType 类型来实现自定义标量 DateTime 的功能。其中定义了三个方法：
  * serialize: 定义了后端对象类型转json格式值的方法
  * parseValue: 定义了json格式值转后端对象类型的方法
  * parseLiteral: 当传入的查询字符串包含标量作为硬编码的参数值时，该值是查询文档的抽象语法树(AST)的一部分。此方法将值的AST表示转换为标量的后端表示。
3. 变更函数里使用 registerDateTime 属性初始化 User 对象

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

执行变更操作的时候会调用 GraphQLScalarType 类 parseValue 方法。

* 变更请求

``` shell
mutation createUser($name: String!, $email: String!, $registerDateTime: DateTime!) {
  createUser(name: $name, email: $email, registerDateTime: $registerDateTime) {
    id,
    name,
    email
    registerDateTime
  }
}
```

* 变更参数

``` shell
{
  "name": "newuser",
  "email": "newuser@gmail.com",
  "registerDateTime": "2000-01-01T10:10:10.000Z"
}
```

* 变更返回结果

``` shell
{
  "data": {
    "createUser": {
      "id": "4",
      "name": "newuser",
      "email": "newuser@gmail.com",
      "registerDateTime": "2000-01-01T02:10:10.000Z"
    }
  }
}
```

### 查询列表操作

执行列表查询操作的时候会调用 GraphQLScalarType 类 serialize 方法。

* 查询请求

``` shell
query GetUsers {
  users {
    id,
    name,
    email,
    registerDateTime
  }
}
```

* 查询结果

``` shell
{
  "data": {
    "users": [
      {
        "id": "1",
        "name": "user1",
        "email": "user1@gmail.com",
        "registerDateTime": "2000-01-01T10:10:10.000Z"
      },
      {
        "id": "2",
        "name": "user2",
        "email": "user2@gmail.com",
        "registerDateTime": "2000-01-01T10:10:10.000Z"
      },
      {
        "id": "3",
        "name": "user3",
        "email": "user3@gmail.com",
        "registerDateTime": "2000-01-01T10:10:10.000Z"
      }
    ]
  }
}
```

### 对象查询操作

执行对象查询操作的时候会调用 GraphQLScalarType 类 parseLiteral 方法，主要用了处理查询请求中 hard code 的 registerDateTime 参数。

* 查询请求

``` shell
query FindUser {
  user(registerDateTime: "2000-01-01T10:10:10.000Z") {
    id,
    name,
    email,
    registerDateTime
  }
}
```

* 查询结果

``` shell
{
  "data": {
    "user": {
      "id": "1",
      "name": "user1",
      "email": "user1@gmail.com",
      "registerDateTime": "2000-01-01T10:10:10.000Z"
    }
  }
}
```
