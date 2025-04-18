package fr.baretto.library.repository;

import fr.baretto.library.entity.Book;
import java.util.List;

public interface BookRepository {

    void save(Book book);

    List<Book> findAll();
}
