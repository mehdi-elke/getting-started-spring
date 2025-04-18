package fr.baretto.library.ano;


import fr.baretto.library.ano.model.Book;
import fr.baretto.library.ano.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        BookService bookService = context.getBean(BookService.class);

        bookService.addBook(new Book("1984", "George Orwell"));
        bookService.addBook(new Book("Brave New World", "Aldous Huxley"));

        bookService.listBooks().forEach(System.out::println);

    }
}
