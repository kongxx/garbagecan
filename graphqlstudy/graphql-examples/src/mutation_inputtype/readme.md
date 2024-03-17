mutation createUser($user: UserInput!) {
  createUser(user: $user) {
    id,
    name,
    email
  }
}

{
  "user": {
    "name": "newuser",
    "email": "newuser@gmail.com"
  }
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