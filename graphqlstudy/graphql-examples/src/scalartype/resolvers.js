const { GraphQLScalarType, Kind } = require('graphql');

const user1 = { id: 1, name: 'user1', email: 'user1@gmail.com', registerDateTime: new Date('2000-01-01T10:10:10.000Z') };
const user2 = { id: 2, name: 'user2', email: 'user2@gmail.com', registerDateTime: new Date('2000-01-01T10:10:10.000Z') };
const user3 = { id: 3, name: 'user3', email: 'user3@gmail.com', registerDateTime: new Date('2000-01-01T10:10:10.000Z') };
const users = [user1, user2, user3];

const datetimeScalar = new GraphQLScalarType({
  name: 'DateTime',
  description: 'DateTime custom scalar type',

  serialize(value) {
    if (value instanceof Date) {
      return value.toISOString();
    }
    throw Error('GraphQL Date Scalar serializer expected a `Date` object');
  },

  parseValue(value) {
    if (typeof value === 'string') {
      return new Date(value);
    }
    throw new Error('GraphQL Date Scalar parser expected a `string`');
  },

  parseLiteral(ast) {
    if (ast.kind === Kind.STRING) {
      return new Date(ast.value);
    }
    throw new Error('GraphQL Date Scalar invalid');
  },
});

const resolvers = {
  DateTime: datetimeScalar,

  Query: {
    users: () => users,

    user(obj, args, context, info) {
      for (let user of users) {
        if (user.registerDateTime.getTime() == args.registerDateTime.getTime()) {
          return user;
        }
      }
      return null;
    },
  },

  Mutation: {
    createUser(obj, args, context, info) {
      let user = { id: users.length + 1, name: args.name, email: args.email, registerDateTime: args.registerDateTime };
      users.push(user);
      return user;
    }
  }
};

module.exports = resolvers;
