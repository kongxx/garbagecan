
const user1 = {id: 1, name: 'user1', email: 'user1@gmail.com'};
const user2 = {id: 2, name: 'user2', email: 'user2@gmail.com'};
const user3 = {id: 3, name: 'user3', email: 'user3@gmail.com'};
const users = [user1, user2, user3];

const resolvers = {
  Query: {
    users(obj, args, context, info) {
      return users;
    },
  },
};

module.exports = resolvers;
