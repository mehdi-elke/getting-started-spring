package fr.baretto.library.xml.repository;

import fr.baretto.library.xml.model.Book;
import java.util.List;

public interface BookRepository {
    void save(Book book);
    List<Book> findAll();
}
