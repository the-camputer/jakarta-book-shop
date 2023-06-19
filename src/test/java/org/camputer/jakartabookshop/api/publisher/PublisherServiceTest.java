package org.camputer.jakartabookshop.api.publisher;

import jakarta.transaction.NotSupportedException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import org.camputer.jakartabookshop.api.JPATest;
import org.camputer.jakartabookshop.api.book.Book;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class PublisherServiceTest extends JPATest {

    @Mock
    UserTransaction utx;

    private PublisherService publisherService;

    @BeforeEach
    public void setUp() {
        publisherService = new PublisherService(entityManager, utx);
    }

    @Test
    public void getAllPublishers_returns_Publisher_list() {
        List<Publisher> publishers = publisherService.getAllPublishers();

        Assertions.assertEquals(2, publishers.size());
    }

    @Test
    public void getPublisher_returns_single_publisher_for_id() {
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("INSERT INTO PUBLISHER(PUBLISHER_ID, PUBLISHER_NAME) VALUES(1000, 'Secret Publishing Co')").executeUpdate();
        entityManager.getTransaction().commit();

        Publisher SecretPublishingCo = publisherService.getPublisher(1000);

        Assertions.assertNotNull(SecretPublishingCo);
        Assertions.assertEquals("Secret Publishing Co", SecretPublishingCo.getPublisherName());
    }

    @Test
    public void persistPublisher_creates_new_publisher_and_returns_data() {
        Publisher newPublisher = new Publisher();
        newPublisher.setPublisherName("Penguin");

        entityManager.getTransaction().begin();
        Publisher Penguin = publisherService.persistPublisher(newPublisher);
        entityManager.getTransaction().commit();

        Assertions.assertNotNull(Penguin);
        Assertions.assertNotNull(entityManager.find(Publisher.class, Penguin.getPublisherId()));
    }

    @Test
    public void persistPublisher_returns_null_if_error() throws SystemException, NotSupportedException {
        Publisher newPublisher = new Publisher();
        newPublisher.setPublisherName("Penguin");

        doThrow(NotSupportedException.class).when(utx).begin();

        entityManager.getTransaction().begin();
        Publisher Penguin = publisherService.persistPublisher(newPublisher);
        entityManager.getTransaction().commit();

        Assertions.assertNull(Penguin);
    }

    @Test
    public void mergePublisher_updates_publisher_and_returns_new_data() {
        Publisher Hachette = entityManager.find(Publisher.class, 100);
        Hachette.setPublisherName("Ope entering a new name here");

        Publisher updated = publisherService.mergePublisher(Hachette);

        Assertions.assertNotNull(updated);
        Assertions.assertEquals("Ope entering a new name here", updated.getPublisherName());
    }

    @Test
    public void mergePublisher_returns_null_if_error() throws SystemException, NotSupportedException {
        Publisher Hachette = entityManager.find(Publisher.class, 100);
        Hachette.setPublisherName("Ope entering a new name here");

        doThrow(NotSupportedException.class).when(utx).begin();

        Publisher updated = publisherService.mergePublisher(Hachette);

        Assertions.assertNull(updated);
    }

    @Test
    public void removePublisher_returns_true_if_publisher_removed() {
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("INSERT INTO PUBLISHER(PUBLISHER_ID, PUBLISHER_NAME) VALUES(1000, 'Secret Publishing Co')").executeUpdate();
        entityManager.getTransaction().commit();

        Publisher defunct = entityManager.find(Publisher.class, 1000);
        Assertions.assertNotNull(defunct);

        entityManager.getTransaction().begin();
        boolean result = publisherService.removePublisher(defunct);
        entityManager.getTransaction().commit();

        Assertions.assertTrue(result);
        Assertions.assertNull(entityManager.find(Publisher.class, 1000));
    }

    @Test
    public void removePublisher_returns_false_if_error() throws SystemException, NotSupportedException {
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("INSERT INTO PUBLISHER(PUBLISHER_ID, PUBLISHER_NAME) VALUES(1000, 'Secret Publishing Co')").executeUpdate();
        entityManager.getTransaction().commit();

        Publisher defunct = entityManager.find(Publisher.class, 1000);
        Assertions.assertNotNull(defunct);

        doThrow(NotSupportedException.class).when(utx).begin();

        entityManager.getTransaction().begin();
        boolean result = publisherService.removePublisher(defunct);
        entityManager.getTransaction().commit();

        Assertions.assertFalse(result);
        Assertions.assertNotNull(entityManager.find(Publisher.class, 1000));
    }

    @Test
    public void getBooksForPublisher_returns_all_books_associated_with_publisher() {
        Publisher Hachette = entityManager.find(Publisher.class, 100);

        List<Book> books = publisherService.getBooksForPublisher(Hachette.getPublisherId());

        Assertions.assertEquals(2, books.size());
        Assertions.assertTrue(books.stream().anyMatch(book -> book.getTitle().equals("The Fifth Season")));
    }

    private static Book generateBookWithPublisher(int id, String title, Publisher publisher) {
        Book book = new Book();
        book.setBookId(id);
        book.setTitle(title);
        if (publisher != null) {
            book.setPublisher(publisher);
        }
        return book;
    }
}
