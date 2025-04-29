package fr.baretto.library.ano.repository;

import fr.baretto.library.ano.model.Book;

import java.util.List;

public interface BookRepository {
    void save(Book book);
    List<Book> findAll();
}
