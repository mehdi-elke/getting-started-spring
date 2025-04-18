package fr.baretto.library;

import fr.baretto.library.entity.Book;
import fr.baretto.library.service.BookServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {
    public static void main(String[] args) {

        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        BookServiceImpl bookService = context.getBean(BookServiceImpl.class);

        Book book1 = new Book("Laura et l'étoile", "Thémis");
        Book book2 = new Book("The fault in our stars", "Zahra");
        Book book3 = new Book("Lipton", "Adrien");
        Book book4 = new Book("BasicFit : Go for It", "Kylian");

        bookService.addBook(book1);
        bookService.addBook(book2);
        bookService.addBook(book3);
        bookService.addBook(book4);

        System.out.println(bookService.listBooks());
    }
}
