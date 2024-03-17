# 

## 查询操作

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

## 查询结果

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
