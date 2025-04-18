package fr.baretto.library.xml;


import fr.baretto.library.xml.model.Book;
import fr.baretto.library.xml.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        BookService bookService = context.getBean("bookService", BookService.class);

        bookService.addBook(new Book("1984", "George Orwell"));
        bookService.addBook(new Book("Brave New World", "Aldous Huxley"));

        bookService.listBooks().forEach(System.out::println);

    }
}
