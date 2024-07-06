package com.mthor.booksfinder.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "authors", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
@Getter
@Setter
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private int birthYear;
    private int deathYear;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.EAGER)
    private List<Book> results;

}
