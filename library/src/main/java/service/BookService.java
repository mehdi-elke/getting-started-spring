package service;

import modele.Book;

import java.util.List;

public interface BookService {

    void addBook(Book book);

    List<Book> listBooks();

}
