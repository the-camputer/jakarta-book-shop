package org.camputer.jakartabookshop.api.publisher;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camputer.jakartabookshop.api.author.AuthorService;
import org.camputer.jakartabookshop.api.book.Book;

import java.util.List;

@ApplicationScoped
public class PublisherService {

    @PersistenceUnit(unitName = "BookshopRepository")
    private EntityManagerFactory emf;

    private static final Logger log = LogManager.getLogger(AuthorService.class);

    public Publisher getPublisher(int id) {
        EntityManager em = emf.createEntityManager();
        return em.find(Publisher.class, id);
    }

    public List<Book> getBooksForPublisher(int id) {
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);

        Root<Book> root = criteriaQuery.from(Book.class);
        Join<Book, Publisher> publisher = root.join("publisher");
        criteriaQuery.where(
                criteriaBuilder.equal(publisher.get("publisherId"), id)
        );

        return em.createQuery(criteriaQuery).getResultList();
    }
}
