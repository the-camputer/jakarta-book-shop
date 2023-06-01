package org.camputer.jakartabookshop.api.author;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camputer.jakartabookshop.api.book.Book;

import java.util.List;


@ApplicationScoped
public class AuthorService {

    @PersistenceContext(unitName = "BookshopRepository")
    private EntityManager entityManager;

    @Resource
    UserTransaction utx;

    private static final Logger log = LogManager.getLogger(AuthorService.class);

    public Author getAuthor(Integer id) {
//        EntityManager em = emf.createEntityManager();
        return entityManager.find(Author.class, id);
    }

    public List<Author> getAllAuthors() {
//        EntityManager em = emf.createEntityManager();
        return entityManager.createNamedQuery("Author.getAllAuthors").getResultList();
    }

    public Author persistAuthor(Author author) {
//        EntityManager em = emf.createEntityManager();
        try {
            utx.begin();
            entityManager.persist(author);
            utx.commit();
        } catch(Exception e) {
            log.error("Unable to persist new author: " + author.toString());
            e.printStackTrace();
            return null;
        }
        return author;
    }

    public Author mergeAuthor(Author author) {
        try {
            utx.begin();
            entityManager.merge(author);
            utx.commit();
        } catch(Exception e) {
            log.error("Unable to merge author changes: " + author.toString());
            return null;
        }
        return author;
    }

    public boolean removeAuthor(Author author) {
        try {
            utx.begin();
            if (!entityManager.contains(author)) {
                author = entityManager.merge(author);
            }
            entityManager.remove(author);
            utx.commit();
        } catch(Exception e) {
            log.error("Unable to remove author: " + author.getAuthorId());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean addBookForAuthor(Author author, Book book) {
        try {
            utx.begin();
            author = entityManager.merge(author);
            book = entityManager.merge(book);
            author.addBook(book);
            entityManager.merge(author);
            utx.commit();
        } catch(Exception e) {
            log.error("Unable to associate book to author: " + author.getAuthorId());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean removeBookForAuthor(Author author, Book book) {
        try {
            utx.begin();
            author = entityManager.merge(author);
            book = entityManager.merge(book);
            author.removeBook(book);
            entityManager.merge(author);
            utx.commit();
        } catch (HeuristicRollbackException | RollbackException | NotSupportedException | HeuristicMixedException |
                 SystemException e) {
            log.error("Unable to remove book association to author: " + author.getAuthorId());
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
