package fr.baretto.library;

import fr.baretto.library.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        // Charger le contexte Spring
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        // Récupérer le bean bookService
        BookService bookService = context.getBean("bookService", BookService.class);

        // Ajouter des livres
        bookService.addBook(new Book("1984", "George Orwell"));
        bookService.addBook(new Book("Brave New World", "Aldous Huxley"));

        // Lister les livres
        System.out.println("Liste des livres :");
        bookService.listBooks().forEach(System.out::println);
    }
}