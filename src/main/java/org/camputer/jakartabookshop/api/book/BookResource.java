package org.camputer.jakartabookshop.api.book;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.camputer.jakartabookshop.api.author.Author;
import org.camputer.jakartabookshop.api.author.AuthorService;
import org.camputer.jakartabookshop.api.publisher.Publisher;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Path("/book")
@RolesAllowed("READ")
public class BookResource {

    @Inject
    BookService bookService;

    @Inject
    AuthorService authorService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("id") int id) {
        Book book = bookService.getBook(id);
        return Response.ok(book).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("WRITE")
    public Response addBook(Book book) {
        Book result = bookService.persistBook(book);
        if (result == null) {
            return Response.serverError().build();
        } else {
            return Response.ok(result).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("WRITE")
    public Response updateBook(Book book) {
        if (book.getBookId() == null ) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new HashMap<String, String>() {{
                        put("error", "no book id in request payload");
                    }})
                    .build();
        } else {
            Book currentBook = bookService.getBook(book.getBookId());
            if (currentBook == null) {
                return bookNotFound(book.getBookId());
            } else {
                Book result = bookService.mergeBook(book);
                if (result == null) {
                    return Response.serverError().build();
                } else {
                    return Response.ok(result).build();
                }
            }
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("WRITE")
    public Response deleteBook(@PathParam("id") Integer id) {
        Book book = bookService.getBook(id);
        if (book == null) {
            return bookNotFound(id);
        } else {
            boolean result = bookService.removeBook(book);
            if (!result) {
                return Response.serverError().build();
            } else {
                return Response.ok(book).build();
            }
        }
    }

    @GET
    @Path("/{id}/authors")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthorsForBook(@PathParam("id") int id) {
        Book book = bookService.getBook(id);
        List<Author> authors = book.getAuthors();
        return Response.ok(authors).build();
    }

    @PUT
    @Path("/{id}/authors")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("WRITE")
    public Response addAuthorToBook(@PathParam("id") Integer bookId, HashMap<String, Object> authorInfo) {
        Book book = bookService.getBook(bookId);
        if (book == null) {
            return bookNotFound(bookId);
        }  else {
            int authorId;
            try {
                authorId = ((BigDecimal) authorInfo.get("id")).intValue();
            } catch(NullPointerException e) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(new HashMap<String, String>() {{
                            put("error", "no 'id' property found for author info");
                        }})
                        .build();
            }

            Author author = authorService.getAuthor(authorId);
            if (author == null) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(new HashMap<String, String>() {{
                            put("error", "no author found for id " + authorId);
                        }})
                        .build();
            } else {
                boolean result = authorService.addBookForAuthor(author, book);
                if (!result) {
                    return Response.serverError().build();
                } else {
                    return Response.ok(author).build();
                }
            }
        }
    }


    @GET
    @Path("/{id}/publisher")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPublisherForBook(@PathParam("id") int id) {
        Book book = bookService.getBook(id);
        Publisher publisher = book.getPublisher();
        return Response.ok(publisher).build();
    }

    private Response bookNotFound(Integer bookId) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new HashMap<String, String>() {{
                    put("error", "no book found for id " + bookId);
                }})
                .build();
    }
}
