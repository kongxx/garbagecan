# GraphQL入门之变更输入类型

前一篇文章介绍了变更操作，在创建 User 对象的时候，只传递了 name 和 email 参数，但是如果属性太多或者创建对象的时候只需要部分必选参数，直接把属性都当成参数就不合适了，这里 GraphQL 提供了 Input Type 参数来解决这个问题。下面看个例子。

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

input UserInput {
  name: String!
  email: String!
}

type Mutation {
  createUser(user: UserInput!) : User!
}

schema {
  query: Query
  mutation: Mutation
}
```

schema 文件主要包括：

1. 新增了输入参数 UserInput 的定义，作为演示只包含两个参数 name 和 email。
2. 修改变更操作不再使用 name 和 email 作为参数，而是改成使用输入参数 UserInput。

## 实现处理器

创建 resolvers.js 文件，内容如下：

``` javascript
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
      let user = {id: users.length + 1,name: args.user.name, email: args.user.email};
      users.push(user);
      return user;
    }
  }
};

module.exports = resolvers;
```

处理器函数有些变化，主要就是 args 里不直接使用 name 和 email 属性，而是通过 args 中的 user 对象来间接使用。

## 测试

### 启动服务

```shell
node server.js
```

服务启动后，访问 [http://localhost:4000](http://localhost:4000) 进行测试。

### 变更操作

变更请求

```shell
mutation createUser($user: UserInput!) {
  createUser(user: $user) {
    id,
    name,
    email
  }
}
```

变更参数

```shell
{
  "user": {
    "name": "newuser",
    "email": "newuser@gmail.com"
  }
}
```

操作结果

```shell
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