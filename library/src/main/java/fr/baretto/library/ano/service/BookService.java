package fr.baretto.library.ano.service;


import fr.baretto.library.ano.model.Book;

import java.util.List;

public interface BookService {
    void addBook(Book book);
    List<Book> listBooks();
}
