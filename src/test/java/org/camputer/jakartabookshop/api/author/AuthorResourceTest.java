package org.camputer.jakartabookshop.api.author;

import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import org.camputer.jakartabookshop.api.book.BookService;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AuthorResourceTest extends JerseyTest {

    AuthorService authorService = mock(AuthorService.class);

    BookService bookService = mock(BookService.class);

    @Override
    protected Application configure() {
        return new ResourceConfig(AuthorResource.class).register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(authorService).to(AuthorService.class).ranked(1);
                bind(bookService).to(BookService.class).ranked(1);
            }
        });
    }

    @Test
    public void hitting_root_retrieves_all_authors() {
        when(authorService.getAllAuthors()).thenReturn(new ArrayList<>() {{
            add(new Author(100));
            add(new Author(101));
        }});

        final Response response = target("/authors").request().get();
        System.out.println(response.getStatus());
        final List  authors = response.readEntity(List.class);

        Assertions.assertEquals(2, authors.size());
    }
}
