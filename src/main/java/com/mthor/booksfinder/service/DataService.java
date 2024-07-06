package com.mthor.booksfinder.service;

import com.mthor.booksfinder.model.APIResponse;
import com.mthor.booksfinder.model.Author;
import com.mthor.booksfinder.model.Book;
import com.mthor.booksfinder.repository.AuthorRepository;
import com.mthor.booksfinder.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DataService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public DataService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Transactional
    public void saveResponseData(APIResponse.ResultDTO result) {
        Book newBook = new Book();
        newBook.setIdAPI(result.id());
        newBook.setTitle(result.title());
        newBook.setDownloads(result.downloads());
        newBook.setLanguages(result.languages());

        List<Author> authors = result.authors().stream().map(authorDTO -> {
            // Buscar el autor por nombre
            Author author = authorRepository.findByName(authorDTO.name());
            if (author == null) {
                author = new Author();
                author.setName(authorDTO.name());
                author.setBirthYear(authorDTO.birth_year());
                author.setDeathYear(authorDTO.death_year());
                authorRepository.save(author);
            }
            return author;
        }).toList();

        newBook.setAuthors(authors);
        bookRepository.save(newBook);
    }

    @Transactional
    public boolean bookExists(Integer bookId){
        Optional<Book> bookRecord = bookRepository.findByIdAPI(bookId);
        return bookRecord.isPresent();
    }

    @Transactional(readOnly = true)
    public List<Author> getAllAuthors(){
        return authorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Author> getAuthorByAliveDate(int year){
        return authorRepository.findAuthorsAliveInYear(year);
    }

    @Transactional(readOnly = true)
    public List<Book> getBooksByLanguage(String lang){
        return bookRepository.findBooksByLanguage(lang);
    }
}
