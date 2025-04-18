package com.mstmm.fr.Controller;

import com.mstmm.fr.Entity.BookInterface;
import com.mstmm.fr.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody BookInterface book) {
        bookService.createBook(book);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<BookInterface> getAll(@RequestParam(required = false) String author) {
        return author != null ? bookService.findByAuthor(author) : bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookInterface> getById(@PathVariable Long id) {
        BookInterface book = bookService.getBook(id);
        return book != null ? ResponseEntity.ok(book) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody BookInterface book) {
        if (bookService.getBook(id) == null) return ResponseEntity.notFound().build();
        bookService.updateBook(id, book);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (bookService.getBook(id) == null) return ResponseEntity.notFound().build();
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }
}
