const book1 = {title: 'book1'};
const book2 = {title: 'book2'};
const book3 = {title: 'book3'};
const author1 = {name: 'author1', books: [book1]};
const author2 = {name: 'author2', books: [book2, book3]};
book1.author = author1;
book2.author = author2;
book3.author = author2;

const books = [book1, book2, book3];
const authors = [author1, author2];

const resolvers = {
  Query: {
    books: () => books,
    authors: () => authors,
  },
};

module.exports = resolvers;
