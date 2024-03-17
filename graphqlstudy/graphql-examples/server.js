const { ApolloServer } =  require('@apollo/server');
const { startStandaloneServer } = require('@apollo/server/standalone');
const fs = require("fs");

// const typeDefs = fs.readFileSync('./query_simple/schema.graphql').toString();
// const resolvers = require('./query_simple/resolvers');

const typeDefs = fs.readFileSync('./src/query_relation/schema.graphql').toString();
const resolvers = require('./src/query_relation/resolvers');

// const typeDefs = fs.readFileSync('./query_pagination/schema.graphql').toString();
// const resolvers = require('./query_pagination/resolvers');

// const typeDefs = fs.readFileSync('./query_fragment/schema.graphql').toString();
// const resolvers = require('./query_fragment/resolvers');

// const typeDefs = fs.readFileSync('./query_directive/schema.graphql').toString();
// const resolvers = require('./query_directive/resolvers');

// const typeDefs = fs.readFileSync('./mutation_simple/schema.graphql').toString();
// const resolvers = require('./mutation_simple/resolvers');

// const typeDefs = fs.readFileSync('./mutation_inputtype/schema.graphql').toString();
// const resolvers = require('./mutation_inputtype/resolvers');

// const typeDefs = fs.readFileSync('./scalar/schema.graphql').toString();
// const resolvers = require('./scalar/resolvers');

const server = new ApolloServer({
  typeDefs,
  resolvers,
});

startStandaloneServer(server).then(function(data) {
  console.log(`🚀 Server ready at ${data.url}`);
});

