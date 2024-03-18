
变更请求

``` shell
mutation createUser($user: UserInput!) {
  createUser(user: $user) {
    id,
    name,
    email
  }
}
```

变更请求参数

``` shell
{
  "user": {
    "name": "newuser",
    "email": "newuser@gmail.com"
  }
}
```

变更返回结果

``` shell
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
