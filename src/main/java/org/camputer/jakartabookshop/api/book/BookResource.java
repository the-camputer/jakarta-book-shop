package org.camputer.jakartabookshop.api.book;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.camputer.jakartabookshop.api.author.Author;
import org.camputer.jakartabookshop.api.author.AuthorService;
import org.camputer.jakartabookshop.api.publisher.Publisher;
import org.camputer.jakartabookshop.api.publisher.PublisherService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Path("/books")
@RolesAllowed("READ")
public class BookResource {

    @Inject
    BookService bookService;

    @Inject
    AuthorService authorService;

    @Inject
    PublisherService publisherService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getallBooks() {
        // TODO: In a real-world scenario, this should be required to use pagination
        List<Book> books = bookService.getAllBooks();
        return Response.ok(books).build();
    }

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
    public Response updateBook(@Valid Book book) {
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
    public Response addAuthorForBook(@PathParam("id") Integer bookId, HashMap<String, Object> authorInfo) {
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
                    return Response.ok(book).build();
                }
            }
        }
    }

    @DELETE
    @Path("/{id}/authors")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("WRITE")
    public Response removeAuthorForBook(@PathParam("id") Integer bookId, @NotNull(message = " authorId must not be null") @FormParam("authorId") Integer authorId) {
        Book book = bookService.getBook(bookId);
        if (book == null) {
            return bookNotFound(bookId);
        }  else {
            Author author = authorService.getAuthor(authorId);
            if (author == null) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(new HashMap<String, String>() {{
                            put("error", "no author found for id " + authorId);
                        }})
                        .build();
            } else {
                boolean result = authorService.removeBookForAuthor(author, book);
                if (!result) {
                    return Response.serverError().build();
                } else {
                    return Response.ok(book).build();
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

    @PUT
    @Path("/{id}/publisher")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPublisherForBook(@PathParam("id") int bookId, @NotNull(message = "publisherId must not be null") @FormParam("publisherId") Integer publisherId) {
        Book book = bookService.getBook(bookId);
        if (book == null) {
            return bookNotFound(bookId);
        } else {
            Publisher publisher = publisherService.getPublisher(publisherId);
            if (publisher == null) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(new HashMap<String, String>() {{
                            put("error", "no publisher found for id " + publisherId);
                        }})
                        .build();
            } else {
                boolean result = bookService.addPublisherForBook(book, publisher);
                if (!result) {
                    return Response.serverError().build();
                } else {
                    return Response.ok(book).build();
                }
            }
        }
    }

    @DELETE
    @Path("/{id}/publisher")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removePublishserForBook(@PathParam("id") int bookId) {
        Book book = bookService.getBook(bookId);
        if (book == null) {
            return bookNotFound(bookId);
        } else {
            boolean result = bookService.removePublisherForBook(book);
            if (!result) {
                return Response.serverError().build();
            } else {
                return Response.ok(book).build();
            }
        }
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
