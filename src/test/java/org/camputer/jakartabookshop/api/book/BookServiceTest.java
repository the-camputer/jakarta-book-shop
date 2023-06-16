package org.camputer.jakartabookshop.api.book;

import jakarta.transaction.*;
import org.camputer.jakartabookshop.api.JPATest;
import org.camputer.jakartabookshop.api.publisher.Publisher;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BookServiceTest extends JPATest {
    @Mock
    UserTransaction utx;

    private BookService bookService;

    @BeforeEach
    public void setUp() {
        bookService = new BookService(entityManager, utx);
    }

    @Test
    public void getAllBooks_returns_Book_list() {
        List<Book> result = bookService.getAllBooks();

        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void getBook_returns_book_for_id_if_available_else_null() {
        // Positive test - book does exist
        Book result = bookService.getBook(100);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("The Fifth Season", result.getTitle());

        // Negative test - book does not exist
        result = bookService.getBook(75);
        Assertions.assertNull(result);
    }

    @Test
    public void addPublisherForBook_associates_a_given_book_to_a_publisher() {
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("INSERT INTO BOOK(BOOK_ID, TITLE) VALUES(103, 'The 5th Wave')").executeUpdate();
        entityManager.getTransaction().commit();
        Publisher publisher = entityManager.find(Publisher.class, 101);
        Book The5thWave = entityManager.find(Book.class, 103);

        Assertions.assertNull(The5thWave.getPublisher());

        boolean result = bookService.addPublisherForBook(The5thWave, publisher);

        Assertions.assertTrue(result);
        Assertions.assertEquals(publisher, The5thWave.getPublisher());
    }

    @Test
    public void addPublisherForBook_returns_false_if_error() throws SystemException, NotSupportedException {
        Publisher publisher = entityManager.find(Publisher.class, 101);
        Book TheFifthSeason = entityManager.find(Book.class, 100);

        doThrow(jakarta.transaction.NotSupportedException.class).when(utx).begin();

        boolean result = bookService.addPublisherForBook(TheFifthSeason, publisher);

        Assertions.assertFalse(result);
    }


    @Test
    public void removePublisherForBook_removes_publisher_association() {
        Book TheFifthSeason = entityManager.find(Book.class, 100);

        Assertions.assertNotNull(TheFifthSeason.getPublisher());

        boolean result = bookService.removePublisherForBook(TheFifthSeason);

        Assertions.assertTrue(result);
        Assertions.assertNull(TheFifthSeason.getPublisher());
    }

    @Test
    public void removePublisherForBook_returns_false_if_error() throws SystemException, NotSupportedException {
        Book TheFifthSeason = entityManager.find(Book.class, 100);

        doThrow(jakarta.transaction.NotSupportedException.class).when(utx).begin();

        boolean result = bookService.removePublisherForBook(TheFifthSeason);

        Assertions.assertFalse(result);
    }

    private Book generateBook(int id, String title) {
        Book book = new Book();
        book.setBookId(id);
        book.setTitle(title);
        return book;
    }
}
