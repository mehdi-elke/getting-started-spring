package fr.baretto.library.xml.service;

import fr.baretto.library.xml.model.Book;
import fr.baretto.library.xml.repository.BookRepository;

import java.util.List;

public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    // Setter for Spring injection
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void addBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public List<Book> listBooks() {
        return bookRepository.findAll();
    }
}