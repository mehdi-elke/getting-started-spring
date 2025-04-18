package com.mstmm.fr.Service;

import com.mstmm.fr.Entity.BookInterface;
import com.mstmm.fr.Repository.BookRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepositoryInterface bookRepository;

    public void createBook(BookInterface book) {
        bookRepository.save(book);
    }

    public BookInterface getBook(Long id) {
        return bookRepository.findById(id);
    }

    public List<BookInterface> getAllBooks() {
        return bookRepository.findAll();
    }

    public void updateBook(Long id, BookInterface book) {
        BookInterface existing = bookRepository.findById(id);
        if (existing != null) {
            existing.setTitle(book.getTitle());
            existing.setAuthor(book.getAuthor());
            bookRepository.update(existing);
        }
    }

    public void deleteBook(Long id) {
        bookRepository.delete(id);
    }

    public List<BookInterface> findByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }
}
