package fr.baretto.library;

import fr.baretto.library.xml.model.Book;
import fr.baretto.library.xml.repository.BookRepository;
import fr.baretto.library.xml.service.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book sampleBook;

    @BeforeEach
    void setUp() {
        sampleBook = new Book("The Pragmatic Programmer", "Andrew Hunt");
    }

    @Test
    void testAddBook() {
        bookService.addBook(sampleBook);
        verify(bookRepository, times(1)).save(sampleBook);
    }

    @Test
    void testListBooks() {
        List<Book> expectedBooks = Arrays.asList(sampleBook);
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        List<Book> result = bookService.listBooks();

        assertEquals(1, result.size());
        assertEquals(sampleBook.getTitle(), result.get(0).getTitle());
        verify(bookRepository, times(1)).findAll();
    }
}
