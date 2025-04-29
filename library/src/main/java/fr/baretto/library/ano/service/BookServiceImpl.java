package fr.baretto.library.ano.service;

import fr.baretto.library.ano.model.Book;
import fr.baretto.library.ano.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    @Autowired
    BookServiceImpl(BookRepository repository){
        bookRepository = repository;
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