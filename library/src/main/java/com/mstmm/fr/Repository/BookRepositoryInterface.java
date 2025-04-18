package com.mstmm.fr.Repository;
import com.mstmm.fr.Entity.Book;
import com.mstmm.fr.Entity.BookInterface;

import java.util.List;

public interface BookRepositoryInterface {
    void save(BookInterface book);

    List<BookInterface> findAll();
}
