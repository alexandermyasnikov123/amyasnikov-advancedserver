## API configuration user manual

### Pre-requirements
To run this project on your local computer you need to create '<b><u>hidden.properties</b></u>' file
in the '<u>src/main/resources</u>' directory.

The resulting file will be located at [src/main/resources/hidden.properties](src/main/resources/hidden.properties).

### What should be in the [<u>hidden.properties</u>](src/main/resources/hidden.properties) file?
This file must consists of next required variables:

#### Jwt configuration options:
```properties
username-claim=username
role-claim=role
uuid-claim=uuid
prod-expiration-minutes=10
dev-expiration-minutes=60
bearer-secret=insert_here_your_HS512_secret_key
```

### !!!It's highly recommended for you to use both profiles!!!

#### Run application with dev profile settings

```properties
dev-db-url=jdbc_uri_to_your_database
dev-db-username=the_username_of_your_database
dev-db-password=the_password_of_your_database
```

#### Run application with production profile settings
```properties
prod-db-url=jdbc_uri_to_your_database
prod-db-username=the_username_of_your_database
prod-db-password=the_password_of_your_database
```

### The example of the resulting output file:
```properties
username-claim=username
role-claim=role
uuid-claim=uuid
prod-expiration-minutes=10
dev-expiration-minutes=60
bearer-secret=insert_here_your_HS512_secret_key

dev-db-url=jdbc:h2:mem:testdb
dev-db-username=sa_user
dev-db-password=sa_password_123

prod-db-url=jdbc:postgresql://localhost:5432/news_api
prod-db-username=news_db_user
prod-db-password=news_db_password123
```
