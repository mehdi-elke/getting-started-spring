package fr.baretto.library.xml.service;


import fr.baretto.library.xml.model.Book;
import java.util.List;

public interface BookService {
    void addBook(Book book);
    List<Book> listBooks();
}
