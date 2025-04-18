package com.mstmm.fr.Repository;
import com.mstmm.fr.Entity.Book;

import java.util.List;

public interface BookRepository {
    void save(Book book);

    List<Book> findAll();
}
