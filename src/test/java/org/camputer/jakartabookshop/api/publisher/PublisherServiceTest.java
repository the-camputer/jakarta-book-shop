package org.camputer.jakartabookshop.api.publisher;

import jakarta.transaction.UserTransaction;
import org.camputer.jakartabookshop.api.JPATest;
import org.camputer.jakartabookshop.api.book.Book;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class PublisherServiceTest extends JPATest {

    @Mock
    UserTransaction utx;

    private PublisherService publisherService;

    @BeforeEach
    private void setUp() {
        publisherService = new PublisherService(entityManager, utx);
    }

    @Test
    public void getAllPublishers_returns_Publisher_list() {
        List<Publisher> publishers = publisherService.getAllPublishers();

        Assertions.assertEquals(2, publishers.size());
    }

    @Test
    public void getBooksForPublisher_returns_all_books_associated_with_publisher() {
        Publisher publisher = generatePublisher(100, "Hatchette Book Group");


    }

    private static Publisher generatePublisher(int id, String name) {
        Publisher publisher = new Publisher();
        publisher.setPublisherId(id);
        publisher.setPublisherName(name);
        return publisher;
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
