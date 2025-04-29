package fr.baretto.library.ano.repository;

import fr.baretto.library.ano.model.Book;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryBookRepository implements BookRepository {

    private final List<Book> books = new ArrayList<>();

    @Override
    public void save(Book book) {
        books.add(book);
    }

    @Override
    public List<Book> findAll() {
        return books;
    }
}
