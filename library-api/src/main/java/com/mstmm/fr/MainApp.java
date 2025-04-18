package com.mstmm.fr;

import com.mstmm.fr.Controller.PingController;
import com.mstmm.fr.Entity.Book;
import com.mstmm.fr.Entity.BookInterface;
import com.mstmm.fr.Service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("Library API is running...");

        ApplicationContext context = new FileSystemXmlApplicationContext("library-api/src/main/webapp/WEB-INF/applicationContext.xml");

        BookService bookService = context.getBean(BookService.class);

        BookInterface book = new Book(1L, "1984", "George Orwell");

        bookService.createBook(book);

    }
}
