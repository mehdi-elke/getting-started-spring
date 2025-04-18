package com.mstmm.fr.Service;

import com.mstmm.fr.Entity.Book;
import com.mstmm.fr.Entity.BookInterface;
import com.mstmm.fr.Repository.BookRepository;
import com.mstmm.fr.Repository.BookRepositoryInterface;

import java.util.List;

public class BookService {
    
    private BookRepositoryInterface repository;

    public void addBook(BookInterface book) {
        repository.save(book);
    }

    public List<BookInterface> listBooks() {
       return repository.findAll();
    }

    public void setBookRepository(BookRepository bookRepository) {
        this.repository = bookRepository;
    }
}
