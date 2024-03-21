const { ApolloServer } =  require('@apollo/server');
const { startStandaloneServer } = require('@apollo/server/standalone');
const fs = require("fs");

// const typeDefs = fs.readFileSync('./src/query_simple/schema.graphql').toString();
// const resolvers = require('./src/query_simple/resolvers');

// const typeDefs = fs.readFileSync('./src/query_relation/schema.graphql').toString();
// const resolvers = require('./src/query_relation/resolvers');

const typeDefs = fs.readFileSync('./src/query_pagination/schema.graphql').toString();
const resolvers = require('./src/query_pagination/resolvers');

// const typeDefs = fs.readFileSync('./src/query_fragment/schema.graphql').toString();
// const resolvers = require('./src/query_fragment/resolvers');

// const typeDefs = fs.readFileSync('./src/query_directive/schema.graphql').toString();
// const resolvers = require('./src/query_directive/resolvers');

// const typeDefs = fs.readFileSync('./src/mutation_simple/schema.graphql').toString();
// const resolvers = require('./src/mutation_simple/resolvers');

// const typeDefs = fs.readFileSync('./src/mutation_inputtype/schema.graphql').toString();
// const resolvers = require('./src/mutation_inputtype/resolvers');

// const typeDefs = fs.readFileSync('./src/scalartype/schema.graphql').toString();
// const resolvers = require('./src/scalartype/resolvers');

const server = new ApolloServer({
  typeDefs,
  resolvers,
});

startStandaloneServer(server).then(function(data) {
  console.log(`🚀 Server ready at ${data.url}`);
});

