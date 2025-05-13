package fr.baretto.library.repository;

import fr.baretto.library.Book;

import java.util.List;

public interface BookRepository {

    void saveBook(Book book);
    List<Book> findAll();

}