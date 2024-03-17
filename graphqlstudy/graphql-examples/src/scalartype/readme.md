
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



---


mutation createUser($name: String!, $email: String!, $registerDateTime: DateTime!) {
  createUser(name: $name, email: $email, registerDateTime: $registerDateTime) {
    id,
    name,
    email
    registerDateTime
  }
}

{
  "name": "newuser",
  "email": "newuser@gmail.com",
  "registerDateTime": "2000-01-01T10:10:10"
}

{
  "data": {
    "createUser": {
      "id": "4",
      "name": "newuser",
      "email": "newuser@gmail.com",
      "registerDateTime": "2000-01-01T10:10:10"
    }
  }
}