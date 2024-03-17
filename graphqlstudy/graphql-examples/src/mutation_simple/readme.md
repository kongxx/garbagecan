mutation createUser($name: String!, $email: String!) {
  createUser(name: $name, email: $email) {
    id,
    name,
    email
  }
}

{
  "name": "newuser",
  "email": "newuser@gmail.com"
}

{
  "data": {
    "createUser": {
      "id": "4",
      "name": "newuser",
      "email": "newuser@gmail.com"
    }
  }
}