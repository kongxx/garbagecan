# 

## 列表查询

### 查询请求

``` shell
query GetUsers {
  users {
    id,
    name,
    email
  }
}
```

### 查询结果

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

## 查询指定用户

### 查询请求

``` shell
query FindUser {
  user(id: 1) {
    id,
    name,
    email
  }
}
```

### 查询结果

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

## curl

``` shell
curl -X POST http://localhost:4000/ -H "Content-Type: application/json"  -d @- << EOF
{
  "query": "query GetUsers {
    users {
      id,
      name,
      email
    }
  }"
}
EOF
```