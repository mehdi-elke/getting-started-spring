package com.mstmm.fr.Repository;

import com.mstmm.fr.Entity.Book;
import com.mstmm.fr.Entity.BookInterface;

import java.util.ArrayList;
import java.util.List;

public class BookRepository implements BookRepositoryInterface{

    private final List<BookInterface> books = new ArrayList<>();

    public void save(BookInterface book) {
        books.add(book);
    }

    public List<BookInterface> findAll() {
        return new ArrayList<>(books);
    }
}
