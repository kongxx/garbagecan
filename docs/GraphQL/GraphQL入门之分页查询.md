# GraphQL入门之分页查询

前一篇文章讲了怎么创建 GraphQL 的查询操作，今天在此基础上看看要实现一个简单的分页查询应该怎么做，顺便可以介绍一下 GraphQL 里的枚举类型和查询参数应该怎么用。

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

enum SortBy {
  name
  email
}

enum SortDirection {
  ASC
  DESC
}

type Query {
  users(
    sortBy: SortBy = name
    sort: SortDirection = ASC
    offset: Int = 0
    limit: Int = 10
  ): [User]
}
```

schema 文件主要包括：

1. 定义类型：定义了一个User类型，包括 id, name 和 email 属性；定义了两个枚举类型，分别表示排序字段和排序类型。
2. 定义查询操作：定义了一个列表查询操作，支持按指定字段和类型排序，并支持分页查询。

## 实现处理器

创建 resolvers.js 文件，内容如下：

```javascript
const users = [];
for (let i = 0; i < 100; i++) {
  users.push({id: i, name: 'user_' + i, email: 'user' + i + '@gmail.com'});
}

const resolvers = {
  Query: {
    users(obj, args, context, info) {
      let result = users.sort((a, b) => {
        const valueA = a[args.sortBy];
        const valueB = b[args.sortBy];
        if (valueA < valueB) {
          return -1;
        }
        if (valueA > valueB) {
          return 1;
        }
        return 0;
      });
      if (args.sort == "DESC") {
        result = result.reverse();
      }
      result = result.slice(args.offset, args.offset + args.limit);
      return result;
    },

  },
};

module.exports = resolvers;
```

处理器文件主要包括

1. 准备测试数据：这里创建了一个 User 对象的数据组并初始化了几条数据。
2. 定义处理函数：users(obj, args, context, info)

## 主程序

创建 server.js 文件，内容如下：

```javascript
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

### 分页查询

查询操作

```shell
query GetUsers($sortBy: SortBy, $sort: SortDirection, $offset: Int = 0, $limit: Int = 5) {
  users(sortBy: $sortBy, sort: $sort, offset: $offset, limit: $limit)  {
    id,
    name,
    email
  }
}
{
  "sortBy": "name",
  "sort": "ASC"
}
```

查询结果

```json
{
  "data": {
    "users": [
      {
        "id": "0",
        "name": "user_0",
        "email": "user0@gmail.com"
      },
      {
        "id": "1",
        "name": "user_1",
        "email": "user1@gmail.com"
      },
      {
        "id": "10",
        "name": "user_10",
        "email": "user10@gmail.com"
      },
      {
        "id": "11",
        "name": "user_11",
        "email": "user11@gmail.com"
      },
      {
        "id": "12",
        "name": "user_12",
        "email": "user12@gmail.com"
      }
    ]
  }
}
```