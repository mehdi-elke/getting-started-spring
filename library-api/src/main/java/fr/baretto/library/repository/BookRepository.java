package fr.baretto.library.repository;

import fr.baretto.library.model.Book;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class BookRepository {


    private SessionFactory sessionFactory;

    @Autowired
    public BookRepository(SessionFactory sessionFact){
        sessionFactory = sessionFact;
    }

    public List<Book> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Book", Book.class)
                .list();
    }

    public void save(Book book) {
        sessionFactory.getCurrentSession().saveOrUpdate(book);
    }
}