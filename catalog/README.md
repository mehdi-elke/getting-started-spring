# base de données

`docker run --name catalog -e POSTGRES_DB=mydb -e POSTGRES_USER=user -e POSTGRES_PASSWORD=pass -p 5432:5432 -v pgdata:/var/lib/postgresql/data -d postgres`