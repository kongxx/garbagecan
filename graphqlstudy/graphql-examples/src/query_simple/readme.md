# GraphQL入门之简单查询

接前面几篇文章

GraphQL对数据支持的操作有：
    查询（Query）： 获取数据的基本查询。
    变更（Mutation）： 支持对数据的增删改等操作。
    订阅（Subscription）： 用于监听数据变动、并靠websocket等协议推送变动的消息给对方。

``` shell
query GetUsers {
  users {
    id,
    name,
    email
  }
}
```

``` shell
{
  "data": {
    "users": [
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
    ]
  }
}
```

``` shell
query FindUser {
  user(id: 1) {
    id,
    name,
    email
  }
}
```

``` shell
{
  "data": {
    "user": {
      "id": "1",
      "name": "user1",
      "email": "user1@gmail.com"
    }
  }
}
```



*/