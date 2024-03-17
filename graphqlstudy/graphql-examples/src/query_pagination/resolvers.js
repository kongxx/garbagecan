const users = [];
for (let i = 0; i < 100; i++) {
  users.push({id: i, name: 'user_' + i, email: 'user' + i + '@gmail.com'});
}

const resolvers = {
  Query: {
    users(obj, args, context, info) {
      let result = users.sort((a, b) => {
        const valueA = a[args.sortBy];
        const valueB = b[args.sortBy];
        if (valueA < valueB) {
          return -1;
        }
        if (valueA > valueB) {
          return 1;
        }
        return 0;
      });
      if (args.sort == "DESC") {
        result = result.reverse();
      }
      result = result.slice(args.offset, args.offset + args.limit);
      return result;
    },

    user(obj, args, context, info) {
      for (let user of users) {
        if (user.id == args.id) {
          return user;
        }
      }
      return null;
    },

  },
};

module.exports = resolvers;
