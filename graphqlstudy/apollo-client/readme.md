# GraphQL入门之使用ApolloClient查询

前一篇文章介绍了怎么使用 ApolloServer 搭建 GraphQL server，今天看看怎么使用 ApolloClient 来执行查询。

## 安装依赖

```shell
npm install @apollo/client graphql react
```

## 初始化 ApolloClient

```shell
# 导入依赖库
const { ApolloClient, InMemoryCache, gql } = require('@apollo/client');

# 创建ApolloClient实例
const client = new ApolloClient({
    uri: 'http://localhost:4000/',
    cache: new InMemoryCache(),
});
```

创建实例的时候使用 uri 和 cache 参数：

* uri: 指定 GraphQL server 地址，这里使用前一篇文章中启动的Apollo Server。
* cache: Apollo Client用来缓存查询结果。

## 使用ApolloClient执行查询

```shell
# 执行查询
client.query({
    query: gql`
        query {
            hello
        }
    `,
}).then((result) => {
    console.log(result);
});
```

## 完整代码

``` shell
const { ApolloClient, InMemoryCache, gql } = require('@apollo/client');

const client = new ApolloClient({
    uri: 'http://localhost:4000/',
    cache: new InMemoryCache(),
});

client.query({
    query: gql`
        query {
            hello
        }
    `,
}).then((result) => {
    console.log(result);
});
```

## 测试

将上面代码保存到 test.js 文件中，然后运行

``` shell
node test.js
{ data: { hello: 'Hello World!' }, loading: false, networkStatus: 7 }
```
