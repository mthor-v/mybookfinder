package com.mthor.booksfinder.principal;

import com.mthor.booksfinder.model.APIResponse;
import com.mthor.booksfinder.model.Author;
import com.mthor.booksfinder.model.Book;
import com.mthor.booksfinder.service.APIConnect;
import com.mthor.booksfinder.service.DataConverterImpl;
import com.mthor.booksfinder.service.DataService;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MainControl {

    private final Scanner sc = new Scanner(System.in);
    private final APIConnect connAPI = new APIConnect();
    private final String URL_BASE = "https://gutendex.com/books/?";
    private final DataConverterImpl converter = new DataConverterImpl();

    private final DataService dataService;

    public MainControl(DataService dataService) {
        this.dataService = dataService;
    }

    public void menu(){
        int opt = -1;
        while (opt != 0) {
            try{
                System.out.println("-------------------------------------------");
                var menu = """
                    1 - Buscar libro por titulo.
                    2 - Listar libros registrados.
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año.
                    5 - Listar libros por idioma.
           
                    0 - Salir
                    """;
                System.out.println(menu);
                System.out.print("Elige una opción: ");
                opt = sc.nextInt();
                sc.nextLine();

                switch (opt) {
                    case 1 -> getTitle();
                    case 2 -> printAllBooks();
                    case 3 -> printAllAuthors();
                    case 4 -> printAuthorsAliveInYear();
                    case 5 -> printBooksByLanguage();
                    case 0 -> System.out.println("Cerrando la aplicación...");
                    default -> System.out.println("Opción inválida");
                }
            } catch (InputMismatchException ime) {
                System.out.println("Ingresaste un caracter inválido.");
                sc.nextLine();
            }
        }
    }

    public void getTitle() {
        System.out.println("----------------------------------------------------");
        System.out.print("Escribe el nombre del libro que deseas buscar: ");
        String title = sc.nextLine();
        String jsonString = connAPI.gettingData(URL_BASE.concat("search=".concat(title.replace(" ", "+"))));
        // System.out.println(jsonString);
        APIResponse.ResponseDTO response = converter.getData(jsonString, APIResponse.ResponseDTO.class);
        if (response.count() == 1){
            response.results()
                    .forEach(r -> {
                        if (!dataService.bookExists(r.id())){
                            dataService.saveResponseData(r);
                            System.out.println("Libro almacenado correctamente.");
                        } else {
                            System.out.println("***Libro ya existe en la base de datos.");
                        }
                        printBook(r);
                    });
        } else if (response.count() >= 1){
            System.out.println("----------------------------------------------------------------------------");
            System.out.println("Tu busqueda coincide con varios libros, ingresa el código correspondiente: ");
            System.out.println("----------------------------------------------------------------------------");
            List<Integer> booksIds = new ArrayList<>();
            response.results()
                    .forEach(r -> {
                        booksIds.add(r.id());
                        System.out.println("Código: "+ r.id()+", Titulo: "+r.title());
                    });
            System.out.println("----------------------------------------------------------------------------");
            System.out.print("Código: ");
            int titleId = sc.nextInt();
            if (booksIds.contains(titleId)){
                response.results().stream()
                        .filter(r -> r.id() == titleId)
                        .forEach(r -> {
                            if (!dataService.bookExists(r.id())){
                                dataService.saveResponseData(r);
                                System.out.println("Libro almacenado correctamente.");
                            } else {
                                System.out.println("***Libro ya existe en la base de datos.");
                            }
                            printBook(r);
                        });
            } else {
                System.out.println("\n***Código ingresado no coincide.\n");
            }
        } else {
            System.out.println("\n***No se encontraron libros asociados a tu busqueda.\n");
        }
    }

    private <T> void printBook(T result){
        APIResponse.ResultDTO rec = null;

        if (result instanceof Book book){
            rec = new APIResponse.ResultDTO(book);
        } else {
            rec = (APIResponse.ResultDTO) result;
        }

        String title = rec.title();
        String authors = rec.authors().stream()
                .map(APIResponse.AuthorDTO::name)
                .reduce((a, b) -> a + "; " + b)
                .orElse("***No hay autores disponibles.");
        List<String> languages = rec.languages();
        Integer downloads = rec.downloads();

        assert languages != null;
        System.out.println("------LIBRO-------\n" +
                "Titulo: " + title + "\nAutor(es): " + authors +
                "\nIdiomas: " + String.join(", ", languages+
                "\nNúmero de descargas: "+downloads+"\n------------------\n"));
    }

    private void printAuthor(Author author){
        String birth = (author.getBirthYear() != 0) ? String.valueOf(author.getBirthYear()) :"No registra.";
        String death = (author.getDeathYear() != 0) ? String.valueOf(author.getDeathYear()) :"No registra.";
        List<String> books = author.getResults().stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
        System.out.println("------------------------------");
        System.out.println("Autor: "+author.getName()+"\nFecha de nacimiento: "+birth+
                "\nFecha de fallecimiento: "+death+"\nLibros: "+String.join(", ", books.toString())+"\n");
    }

    public void printAllBooks(){
        dataService.getAllBooks().forEach(this::printBook);
    }

    public void printAllAuthors(){
        dataService.getAllAuthors().forEach(this::printAuthor);
    }

    public void printAuthorsAliveInYear(){
        try {
            System.out.print("Ingresa el año: ");
            int year = sc.nextInt();
            List<Author> filteredAuthors = dataService.getAuthorByAliveDate(year);
            if (!filteredAuthors.isEmpty()){
                filteredAuthors.forEach(this::printAuthor);
            } else {
                System.out.println("***No se encuentran registros que correspondan con el valor ingresado.");
            }
        } catch (InputMismatchException ime){
            System.out.println("***El dato ingresado no corresponde.");
            sc.nextLine();
        }
    }

    public void printBooksByLanguage(){
        try {
            System.out.println("""
                Ingresa el idioma para iniciar la busqueda:
                es - Español.
                en - Inglés.
                fr - Francés.
                pt - Portugués.
                """);
            System.out.print("Idioma: ");
            var language = sc.next();
            List<Book> filteredBooks = dataService.getBooksByLanguage(language.toLowerCase());
            if (!filteredBooks.isEmpty()){
                filteredBooks.forEach(this::printBook);
            } else {
                System.out.println("***No se encuentran registros que correspondan con el valor ingresado.");
            }
        } catch (InputMismatchException ime) {
            System.out.println("***El dato ingresado no corresponde.");
            sc.nextLine();
        }

    }
}
