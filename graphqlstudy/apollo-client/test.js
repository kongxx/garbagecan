const { ApolloClient, InMemoryCache, ApolloProvider, gql } = require('@apollo/client');

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
