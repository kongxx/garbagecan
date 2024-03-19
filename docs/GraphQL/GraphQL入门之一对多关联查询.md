# GraphQL入门之一对多关联查询

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
type Book {
  title: String!
  author: Author!
}

type Author {
  name: String!
  books: [Book!]!
}

type Query {
  books: [Book]
  authors: [Author]
}
```

schema 文件主要包括：

1. 定义了一个一对多关系的两个类型：Book 和 Author。
2. 定义了查询操作：Book 和 Author 列表。

## 实现处理器

创建 resolvers.js 文件，内容如下：

``` shell
const book1 = {title: 'book1'};
const book2 = {title: 'book2'};
const book3 = {title: 'book3'};
const author1 = {name: 'author1', books: [book1]};
const author2 = {name: 'author2', books: [book2, book3]};
book1.author = author1;
book2.author = author2;
book3.author = author2;

const books = [book1, book2, book3];
const authors = [author1, author2];

const resolvers = {
  Query: {
    books: () => books,
    authors: () => authors,
  },
};

module.exports = resolvers;
```

处理器文件主要包括：

1. 初始化了一些一对多关联的测试数据
2. 定义处理函数，这里不做什么特殊处理，直接返回两个列表即可

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

### 列表查询操作

查询操作

``` shell
query GetBooksAndAuthors {
  books {
    title,
    author {
      name
    }
  }
  authors {
    name
    books {
      title
    }
  }
}
```

1. 执行查询操作的时候，可以指定关联对象的属性，比如：在查询 books 列表的时候，可以指定关联的 author 只返回那些属性；同样在查询 authors 列表的时候，可以指定关联的 books 列表中只返回 book 的哪些属性。

2. 这里如果我们不指定关联对象的属性，在执行查询的时候会报错，有兴趣的话，大家可以试试看报的是啥错。

查询结果

``` shell
{
  "data": {
    "books": [
      {
        "title": "book1",
        "author": {
          "name": "author1"
        }
      },
      {
        "title": "book2",
        "author": {
          "name": "author2"
        }
      },
      {
        "title": "book3",
        "author": {
          "name": "author2"
        }
      }
    ],
    "authors": [
      {
        "name": "author1",
        "books": [
          {
            "title": "book1"
          }
        ]
      },
      {
        "name": "author2",
        "books": [
          {
            "title": "book2"
          },
          {
            "title": "book3"
          }
        ]
      }
    ]
  }
}
```
