package org.camputer.jakartabookshop.api.author;

import jakarta.transaction.NotSupportedException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import org.camputer.jakartabookshop.api.JPATest;
import org.camputer.jakartabookshop.api.book.Book;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AuthorServiceTest extends JPATest {

    @Mock
    private UserTransaction utx;
    private AuthorService authorService;

    @BeforeEach
    public void setUp() {
        authorService = new AuthorService(entityManager, utx);
    }

    @Test
    public void getAllAuthors_returns_Author_list() {
        List<Author> result = authorService.getAllAuthors();

        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void getAuthor_returns_author_for_id_if_available_else_null() {

        // Positive test - can find author
        Author result = authorService.getAuthor(42);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("N.K. Jemisin", result.getAuthorName());

        // Negative test - author does not exist
        result = authorService.getAuthor(75);
        Assertions.assertNull(result);
    }

    @Test
    public void persistAuthor_creates_new_author_entity() {
        Author SylvianNeuvel = new Author();
        SylvianNeuvel.setAuthorName("Sylvian Neuvel");

        entityManager.getTransaction().begin();
        Author result = authorService.persistAuthor(SylvianNeuvel);
        entityManager.getTransaction().commit();

        Assertions.assertNotNull(result);

        Assertions.assertNotNull(entityManager.find(Author.class, result.getAuthorId()));
    }

    @Test
    public void persistAuthor_returns_null_if_error() throws SystemException, NotSupportedException {
        Author SylvianNeuvel = new Author();
        SylvianNeuvel.setAuthorName("Sylvian Neuvel");

        doThrow(NotSupportedException.class).when(utx).begin();

        Author result = authorService.persistAuthor(SylvianNeuvel);

        Assertions.assertNull(result);
    }

    @Test
    public void mergeAuthor_updates_author() {
        Author RickYancey = entityManager.find(Author.class, 2112);

        RickYancey.setAuthorName("Not Rick Yancey");

        entityManager.getTransaction().begin();
        Author newRickYancey = authorService.mergeAuthor(RickYancey);
        entityManager.getTransaction().commit();

        Assertions.assertNotNull(newRickYancey);

        entityManager.refresh(RickYancey);

        Assertions.assertEquals("Not Rick Yancey", RickYancey.getAuthorName());
    }

    @Test
    public void mergeAuthor_returns_null_if_error() throws SystemException, NotSupportedException {
        Author RickYancey = entityManager.find(Author.class, 2112);

        doThrow(NotSupportedException.class).when(utx).begin();

        Author result = authorService.mergeAuthor(RickYancey);

        Assertions.assertNull(result);
    }

    @Test
    public void removeAuthor_deletes_a_given_author() {
        Author RickYancey = entityManager.find(Author.class, 2112);

        entityManager.getTransaction().begin();
        boolean result = authorService.removeAuthor(RickYancey);
        entityManager.getTransaction().commit();

        Assertions.assertTrue(result);
        Assertions.assertNull(entityManager.find(Author.class, 2112));
    }

    @Test
    public void removeAuthor_returns_false_if_error() throws SystemException, NotSupportedException {
        Author RickYancey = entityManager.find(Author.class, 2112);

        doThrow(NotSupportedException.class).when(utx).begin();
        boolean result = authorService.removeAuthor(RickYancey);

        Assertions.assertFalse(result);
        Assertions.assertNotNull(entityManager.find(Author.class, 2112));

    }

    @Test
    public void addBookForAuthor_associates_a_given_book_to_an_author() {
        try {
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("INSERT INTO BOOK(book_id, title) VALUES(102, 'The Stone Sky')").executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        }

        Author NKJemisin = entityManager.find(Author.class, 42);
        Book TheStoneSky = entityManager.find(Book.class, 102);

        Assertions.assertEquals(2, NKJemisin.getBooks().size());

        boolean result = authorService.addBookForAuthor(NKJemisin, TheStoneSky);

        Assertions.assertTrue(result);
        NKJemisin = entityManager.merge(NKJemisin);
        Assertions.assertEquals(3, NKJemisin.getBooks().size());
    }

    @Test
    public void addBookForAuthor_returns_false_if_error() throws SystemException, NotSupportedException {
        Author NKJemisin = entityManager.find(Author.class, 42);
        Book TheFifthSeason = entityManager.find(Book.class, 100);

        doThrow(jakarta.transaction.NotSupportedException.class).when(utx).begin();

        boolean result = authorService.addBookForAuthor(NKJemisin, TheFifthSeason);

        Assertions.assertFalse(result);
    }

    @Test
    public void removeBookForAuthor_removes_book_association() {
        Author NKJemisin = entityManager.find(Author.class, 42);
        Book TheFifthSeason = entityManager.find(Book.class, 100);

        // Have to use a real transaction for this since utx is mocked
        entityManager.getTransaction().begin();
        boolean result = authorService.removeBookForAuthor(NKJemisin, TheFifthSeason);
        entityManager.getTransaction().commit();

        entityManager.refresh(NKJemisin);

        Assertions.assertTrue(result);
        Assertions.assertEquals(1, NKJemisin.getBooks().size());
    }

    @Test
    public void removeBookForAuthor_returns_false_if_error() throws SystemException, NotSupportedException {
        Book book = new Book();
        book.setTitle("The Fifth Season");

        Author NKJemisin = generateAuthor(42, "N.K. Jemisin");
        NKJemisin.setBooks(new ArrayList<>() {{ add(book); }});

        doThrow(jakarta.transaction.SystemException.class).when(utx).begin();

        boolean result = authorService.removeBookForAuthor(NKJemisin, book);

        Assertions.assertFalse(result);
    }

    private static Author generateAuthor(int id, String name) {
        Author author = new Author();
        author.setAuthorId(id);
        author.setAuthorName(name);
        return author;
    }
}
