package com.mthor.booksfinder.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.stream.Collectors;

public class APIResponse {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ResponseDTO(int count,
                              List<ResultDTO> results) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ResultDTO(int id,
                            String title,
                            List<AuthorDTO> authors,
                            List<String> languages,
                            @JsonAlias("download_count") Integer downloads) {

        public ResultDTO(Book book){
            this(book.getIdAPI(),
                    book.getTitle(),
                    book.getAuthors().stream()
                    .map(AuthorDTO::new)
                    .collect(Collectors.toList()),
                    book.getLanguages(),
                    book.getDownloads());
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AuthorDTO(String name,
                            int birth_year,
                            int death_year) {

        public AuthorDTO(Author author){
            this(author.getName(), author.getBirthYear(), author.getDeathYear());
        }
    }

}
