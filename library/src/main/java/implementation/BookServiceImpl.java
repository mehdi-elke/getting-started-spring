package implementation;

import modele.Book;
import repository.BookRepository;
import service.BookService;

import java.util.ArrayList;
import java.util.List;

public class BookServiceImpl implements BookService {

    private BookRepository bookRepo;

    /*public BookServiceImpl(BookRepository bookRepository){
        this.setBookRepository(bookRepository);
    }*/

    public void setBookRepository(BookRepository bookRepository){
        this.bookRepo=bookRepository;
    }


    public void addBook(Book book) {
        if(book.getAuthor()==null || book.getTitle()==null){
            System.out.println("Objet incorrect");
            return;

        }
        this.bookRepo.save(book);
    }

    public List<Book> listBooks(){
        return this.bookRepo.findAll();
    }




}
