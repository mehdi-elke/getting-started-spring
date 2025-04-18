package com.mstmm.fr.Repository;
import com.mstmm.fr.Entity.BookInterface;

import java.util.List;

public interface BookRepositoryInterface {
    void save(BookInterface book);
    BookInterface findById(Long id);
    List<BookInterface> findAll();
    void update(BookInterface book);
    void delete(Long id);
    List<BookInterface> findByAuthor(String author);
}
