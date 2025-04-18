package fr.baretto.library.repository;

import fr.baretto.library.entity.Book;

import java.util.ArrayList;
import java.util.List;

public class InMemoryBookRepository implements BookRepository {

    private final List<Book> books = new ArrayList<>();

    @Override
    public void save(Book book) {
        this.books.add(book);
    }

    @Override
    public List<Book> findAll() {
        return books;
    }
}
