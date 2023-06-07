package org.camputer.jakartabookshop.api.publisher;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camputer.jakartabookshop.api.author.AuthorService;
import org.camputer.jakartabookshop.api.book.Book;

import java.util.List;

@ApplicationScoped
public class PublisherService {

    @PersistenceContext(unitName = "BookshopRepository")
    private EntityManager entityManager;

    @Resource
    UserTransaction utx;

    private static final Logger log = LogManager.getLogger(AuthorService.class);

    public List<Publisher> getAllPublishers() {
        return entityManager.createNamedQuery("Publisher.getAllPublishers").getResultList();
    }

    public Publisher getPublisher(int id) {
        return entityManager.find(Publisher.class, id);
    }

    public List<Book> getBooksForPublisher(int id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);

        Root<Book> root = criteriaQuery.from(Book.class);
        Join<Book, Publisher> publisher = root.join("publisher");
        criteriaQuery.where(
                criteriaBuilder.equal(publisher.get("publisherId"), id)
        );

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public Publisher persistPublisher(Publisher publisher) {
        try {
            utx.begin();
            entityManager.persist(publisher);
            utx.commit();
        } catch (HeuristicRollbackException | RollbackException | NotSupportedException | HeuristicMixedException |
                 SystemException e) {
            log.error("Unable to persist new publisher: " + publisher.toString());
            e.printStackTrace();
            return null;
        }
        return publisher;
    }

    public Publisher mergePublisher(Publisher publisher) {
        try {
            utx.begin();
            entityManager.merge(publisher);
            utx.commit();
        } catch (HeuristicRollbackException | RollbackException | NotSupportedException | HeuristicMixedException |
                 SystemException e) {
            log.error("Unable to merge new publisher data for publisher: " + publisher.toString());
            e.printStackTrace();
            return null;
        }

        return publisher;
    }

    public boolean removePublisher(Publisher publisher) {
        try {
            utx.begin();
            publisher = entityManager.merge(publisher);
            entityManager.remove(publisher);
            utx.commit();
        } catch (HeuristicRollbackException | RollbackException | NotSupportedException | HeuristicMixedException |
                 SystemException e) {
            log.error("Unable to remove publisher: " + publisher.getPublisherId());
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
