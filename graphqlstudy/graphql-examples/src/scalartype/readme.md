
https://www.apollographql.com/docs/apollo-server/schema/custom-scalars#including-scalar-specification


## 查询操作（列表）

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

## 查询操作（查找）

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

## 变更操作

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

``` shell
{
  "name": "newuser",
  "email": "newuser@gmail.com",
  "registerDateTime": "2000-01-01T10:10:10.000Z"
}
```

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
