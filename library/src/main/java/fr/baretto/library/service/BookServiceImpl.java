package fr.baretto.library.service;

import fr.baretto.library.Book;
import fr.baretto.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    @Autowired
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void addBook(Book book) {
        bookRepository.saveBook(book);
    }

    @Override
    public List<Book> listBooks() {
        return bookRepository.findAll();
    }
}