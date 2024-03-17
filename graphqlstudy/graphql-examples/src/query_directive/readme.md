
``` shell
query GetUsers($includeName: Boolean!, $skipId: Boolean!) {
  users(includeName: $includeName, skipId: $skipId) {
    id @skip(if: $skipId),
    name @include(if: $includeName),
    email
  }
}

{
  "includeName": false,
  "skipId": true
}
```

``` shell
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