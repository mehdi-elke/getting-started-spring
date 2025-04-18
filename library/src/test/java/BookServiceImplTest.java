import fr.baretto.library.entity.Book;
import fr.baretto.library.repository.BookRepository;
import fr.baretto.library.service.BookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void testAddBook() {
        Book book1 = new Book("Laura et l'étoile", "Thémis");
        bookService.addBook(book1);
        verify(bookRepository, times(1)).save(book1);
    }

    @Test
    public void testListBooks() {
        Book book1 = new Book("Laura et l'étoile", "Thémis");
        when(bookRepository.findAll()).thenReturn(List.of(book1));
        List<Book> liste = bookService.listBooks();
        Assertions.assertEquals(List.of(book1), liste);
        verify(bookRepository, times(1)).findAll();

    }
}
