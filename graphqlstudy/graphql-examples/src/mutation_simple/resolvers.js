
const user1 = {id: 1, name: 'user1', email: 'user1@gmail.com'};
const user2 = {id: 2, name: 'user2', email: 'user2@gmail.com'};
const user3 = {id: 3, name: 'user3', email: 'user3@gmail.com'};
const users = [user1, user2, user3];

const resolvers = {
  Query: {
    users: () => users,
  },

  Mutation: {
    createUser(obj, args, context, info) {
      let user = {id: users.length + 1,name: args.name, email: args.email};
      users.push(user);
      return user;
    }
  }
};

module.exports = resolvers;
