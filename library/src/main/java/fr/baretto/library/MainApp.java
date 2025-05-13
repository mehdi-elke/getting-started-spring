package fr.baretto.library;
import fr.baretto.library.Book;
import fr.baretto.library.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        BookService bookService = context.getBean(BookService.class);

        Book book1 = new Book("1984", "George Orwell");
        Book book2 = new Book("To Kill a Mockingbird", "Harper Lee");

        bookService.addBook(book1);
        bookService.addBook(book2);

        for (Book book : bookService.listBooks()) {
            System.out.println(book);
        }
    }
}

