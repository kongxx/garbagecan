

### 原查询操作

``` shell
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
}
```

### 使用 fragment 的查询操作

``` shell
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

### 查询结果

``` shell
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
