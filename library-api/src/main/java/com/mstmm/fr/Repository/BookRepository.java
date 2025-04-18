package com.mstmm.fr.Repository;

import com.mstmm.fr.Entity.BookInterface;
import com.mstmm.fr.Entity.Book;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class BookRepository implements BookRepositoryInterface {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(BookInterface book) {
        if (((Book) book).getId() == null) {
            entityManager.persist(book);
        } else {
            entityManager.merge(book);
        }

    }

    public BookInterface findById(Long id) {
        return entityManager.find(Book.class, id);
    }

    public List<BookInterface> findAll() {
        return entityManager.createQuery("from Book", BookInterface.class).getResultList();
    }

    public void update(BookInterface book) {
        entityManager.merge(book);
    }

    public void delete(Long id) {
        BookInterface book = findById(id);
        if (book != null) {
            entityManager.remove(book);
        }
    }

    public List<BookInterface> findByAuthor(String author) {
        return entityManager.createQuery("from Book b where b.author = :author", BookInterface.class)
                .setParameter("author", author)
                .getResultList();
    }
}
