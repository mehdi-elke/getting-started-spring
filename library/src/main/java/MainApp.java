import modele.Book;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import service.BookService;

public class MainApp {



    public static void main (String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        BookService bookService = context.getBean("bookService", BookService.class);

        Book book=new Book("Yves","Autobiographie sur le prince de côte d'ivoire");
        bookService.addBook(book);
        bookService.listBooks().forEach(b -> {
            System.out.println("Author: " + b.getAuthor());
            System.out.println("Title: " + b.getTitle());
        });

    }
















}
