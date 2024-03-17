# GraphQL入门之查询片段的使用

前面的文章介绍了 GraphQL 的查询操作，但是有时候我们要执行类似下面的这种查询操作，在一个查询中包含多个查询操作并且返回的对象结果相同的时候，重复去写这些属性列表也是比较冗余的事情，那么怎么简化这个写法呢？下面就看看怎么通过 fragment 来简化这个写法。

```shell
query ExampleQuery {
  allusers: users {
    id
    name
    email
  }
  firstuser: user(id: 1) {
    id
    name
    email
  }
  。。。
}
```

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

``` javascript
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

### 查询操作

查询操作

```shell
fragment userfields on User {
  id
  name
  email
}
query ExampleQuery {
  allusers: users {
    ... userfields
  }
  firstuser: user(id: 1) {
    ... userfields
  }
}
```

1. 这里定义了一个 fragment，包含了所有需要返回的 User 类型的属性

2. 在所有需要写返回属性的地方，使用 "..." 操作符来引用上面定义的 fragment。

执行结果

```json
{
  "data": {
    "allusers": [
      {
        "id": "1",
        "name": "user1",
        "email": "user1@gmail.com"
      },
      {
        "id": "2",
        "name": "user2",
        "email": "user2@gmail.com"
      },
      {
        "id": "3",
        "name": "user3",
        "email": "user3@gmail.com"
      }
    ],
    "firstuser": {
      "id": "1",
      "name": "user1",
      "email": "user1@gmail.com"
    }
  }
}
```
