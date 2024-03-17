
const user1 = {id: 1, name: 'user1', email: 'user1@gmail.com', registerDateTime: new Date('2000-01-01T10:10:10.000Z')};
const user2 = {id: 2, name: 'user2', email: 'user2@gmail.com', registerDateTime: new Date('2000-01-01T10:10:10.000Z')};
const user3 = {id: 3, name: 'user3', email: 'user3@gmail.com', registerDateTime: new Date('2000-01-01T10:10:10.000Z')};
const users = [user1, user2, user3];

const resolvers = {
  Query: {
    users: () => users,
  },

  Mutation: {
    createUser(obj, args, context, info) {
      let user = {id: users.length + 1,name: args.name, email: args.email, registerDateTime: args.registerDateTime};
      users.push(user);
      return user;
    }
  }
};

module.exports = resolvers;
