package implementation;

import modele.Book;
import repository.BookRepository;

import java.util.ArrayList;
import java.util.List;
public class InMemoryBookRepository implements BookRepository {
    private final List<Book> bookList = new ArrayList<>();
    public void save(Book book){
        bookList.add(book);
    }

    public List<Book> findAll(){
        return bookList;
    }


}
