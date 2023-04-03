package org.camputer.jakartabookshop.book;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.camputer.jakartabookshop.author.Author;
import org.camputer.jakartabookshop.publisher.Publisher;

import java.util.List;

@Path("/book")
public class BookResource {

    @Inject
    BookService bookService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("id") int id) {
        Book book = bookService.getBook(id);
        return Response.ok(book).build();
    }

    @GET
    @Path("/{id}/authors")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthorsForBook(@PathParam("id") int id) {
        Book book = bookService.getBook(id);
        List<Author> authors = book.getAuthors();
        return Response.ok(authors).build();
    }

    @GET
    @Path("/{id}/publisher")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPublisherForBook(@PathParam("id") int id) {
        Book book = bookService.getBook(id);
        Publisher publisher = book.getPublisher();
        return Response.ok(publisher).build();
    }
}
