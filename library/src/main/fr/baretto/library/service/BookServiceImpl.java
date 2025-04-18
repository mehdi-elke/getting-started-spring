package fr.baretto.library.service;

import fr.baretto.library.entity.Book;
import fr.baretto.library.repository.BookRepository;

import java.util.List;

public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;

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
