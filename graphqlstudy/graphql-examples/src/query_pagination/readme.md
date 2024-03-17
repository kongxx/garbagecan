
```shell
query GetUsers($sortBy: SortBy, $sort: SortDirection, $offset: Int, $limit: Int) {
  users(sortBy: $sortBy, sort: $sort, offset: $offset, limit: $limit)  {
    id,
    name,
    email
  }
}
{
  "sortBy": "name",
  "sort": "ASC",
  "offset": 0,
  "limit": 5
}
```
或者
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

```shell
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