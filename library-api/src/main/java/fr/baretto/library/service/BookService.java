package fr.baretto.library.service;

import fr.baretto.library.model.Book;
import fr.baretto.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {


    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository repository){
        bookRepository = repository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void addBook(Book book) {
        bookRepository.save(book);
    }
}
