
import fr.baretto.library.Book;
import fr.baretto.library.repository.BookRepository;
import fr.baretto.library.service.BookServiceImpl;
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
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository; // Mock du repository

    @InjectMocks
    private BookServiceImpl bookService; // Injecte le mock dans le service

    @Test
    void testAddBook() {
        // Ajouter un livre
        Book book = new Book("1984", "George Orwell");
        bookService.addBook(book);

        // Vérifier que save() a été appelé une fois
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testListBooks() {
        // Liste simulée
        List<Book> books = Arrays.asList(
                new Book("1984", "George Orwell"),
                new Book("Brave New World", "Aldous Huxley")
        );
        when(bookRepository.findAll()).thenReturn(books);

        // Vérifier que listBooks() retourne la liste simulée
        List<Book> result = bookService.listBooks();
        assertEquals(books, result); // Vérifie que le résultat correspond à la liste simulée
    }
}