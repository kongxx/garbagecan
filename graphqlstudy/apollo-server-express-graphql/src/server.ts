import { ApolloServer } from '@apollo/server';
import { expressMiddleware } from '@apollo/server/express4';
import { ApolloServerPluginDrainHttpServer } from '@apollo/server/plugin/drainHttpServer'
import express from 'express';
import http from 'http';
import cors from 'cors';
import bodyParser from 'body-parser';

// The GraphQL schema
const typeDefs = `#graphql
  type Query {
    hello: String
  }
`;

// A map of functions which return data for the schema.
const resolvers = {
  Query: {
    hello: () => 'Hello World!',
  },
};

const app = express();
const httpServer = http.createServer(app);

// Set up Apollo Server
const server = new ApolloServer({
  typeDefs,
  resolvers,
  plugins: [ApolloServerPluginDrainHttpServer({ httpServer })],
});
server.start().then(() => {
  app.use(
    cors(),
    bodyParser.json(),
    expressMiddleware(server),
  );
  
  new Promise((resolve: any) => {
    httpServer.listen({ port: 4000 }, resolve)
  }).then(() => {
    console.log(`🚀 Server ready at http://localhost:4000`);
  });
});
