package fr.baretto.library.service;

import fr.baretto.library.entity.Book;
import java.util.List;

public interface BookService {
    void addBook(Book book);
    List<Book> listBooks();
}
