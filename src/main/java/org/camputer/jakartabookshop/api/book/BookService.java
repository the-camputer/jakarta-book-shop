package org.camputer.jakartabookshop.api.book;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class BookService {

    @PersistenceContext(unitName = "BookshopRepository")
    private EntityManager entityManager;

    @Resource
    UserTransaction utx;

    private static final Logger log = LogManager.getLogger(BookService.class);

    public Book getBook(int id) {
        return entityManager.find(Book.class, id);
    }

    public Book persistBook(Book book) {
        try {
            utx.begin();
            entityManager.persist(book);
            utx.commit();
        } catch (HeuristicRollbackException | SystemException | HeuristicMixedException | NotSupportedException |
                 RollbackException e) {
            log.error("Unable to persist new author: " + book.toString());
            e.printStackTrace();
            return null;
        }
        return book;
    }

    public Book mergeBook(Book book) {
        try {
            utx.begin();
            entityManager.merge(book);
            utx.commit();
        } catch (HeuristicRollbackException | RollbackException | NotSupportedException | HeuristicMixedException |
                 SystemException e) {
            log.error("Unable to merge book changes: " + book.toString());
            e.printStackTrace();
            return null;
        }
        return book;
    }

    public boolean removeBook(Book book) {
        try {
            utx.begin();
            entityManager.remove(book);
            utx.commit();
        } catch (HeuristicRollbackException | RollbackException | NotSupportedException | HeuristicMixedException |
                 SystemException e) {
            log.error("Unable to remove book: " + book.getBookId());
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
