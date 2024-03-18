
查询请求

``` shell
query GetUsers($includeName: Boolean!, $skipId: Boolean!) {
  users(includeName: $includeName, skipId: $skipId) {
    id @skip(if: $skipId),
    name @include(if: $includeName),
    email
  }
}
```

查询参数

``` shell
{
  "includeName": false,
  "skipId": true
}
```

查询结果

``` json
{
  "data": {
    "users": [
      {
        "email": "user1@gmail.com"
      },
      {
        "email": "user2@gmail.com"
      },
      {
        "email": "user3@gmail.com"
      }
    ]
  }
}
```
