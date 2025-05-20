# base de données prod

`docker run --name catalog -e POSTGRES_DB=mydb -e POSTGRES_USER=user -e POSTGRES_PASSWORD=pass -p 5432:5432 -v pgdata:/var/lib/postgresql/data -d postgres`

# base de données test

`docker run --name catalog-test -e POSTGRES_DB=testdb -e POSTGRES_USER=user -e POSTGRES_PASSWORD=pass -p 5432:5432 -v pgdata:/var/lib/postgresql/test-data -d postgres`
