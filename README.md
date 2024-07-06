### MyBookFinder

The application is in charge of querying books by name using the Gutendex API. Each book consulted is stored in a database along with the author's data. This stored information is accessible with a few options.

#### How to install:
- Clone the project.
- Create a database (preferably in PostgreSQL).
- Configure the access data in system variables (or if you prefer, directly in the 'application.properties').
```ini
spring.datasource.url=jdbc:${DB_URL}/${DB_NAME}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
```
- Execute. 

#### Main Menu
1-Search book by title.
2-List registered books.
3-List registered authors.
4-List living authors in a given year.
5-List books by language.
0-Exit.

```shell
1 - Buscar libro por titulo.
2 - Listar libros registrados.
3 - Listar autores registrados
4 - Listar autores vivos en un determinado año.
5 - Listar libros por idioma.

0 - Salir

```
