package fr.baretto.library.repository;

import fr.baretto.library.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryBookRepository implements BookRepository {

    private List<Book> books = new ArrayList<>();

    @Override
    public void saveBook(Book book) {
        books.add(book);
    }

    @Override
    public List<Book> findAll() {
        return books;
    }

}
