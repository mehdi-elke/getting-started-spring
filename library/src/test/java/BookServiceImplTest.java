import fr.baretto.library.Book;
import fr.baretto.library.repository.BookRepository;
import fr.baretto.library.service.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    public void setUp() {
        // Initialisation avant chaque test, si nécessaire
    }

    @Test
    public void testAddBook() {
        Book book = new Book("1984", "George Orwell");
        bookService.addBook(book);
        verify(bookRepository, times(1)).saveBook(book);
    }

    @Test
    public void testListBooks() {
        Book book1 = new Book("1984", "George Orwell");
        Book book2 = new Book("To Kill a Mockingbird", "Harper Lee");
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<Book> books = bookService.listBooks();

        assertEquals(2, books.size());
        assertEquals("1984", books.get(0).getTitle());
        assertEquals("To Kill a Mockingbird", books.get(1).getTitle());
    }
}
