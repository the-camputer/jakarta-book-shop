package org.camputer.jakartabookshop.api.author;


import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camputer.jakartabookshop.api.book.Book;
import org.camputer.jakartabookshop.api.book.BookService;

import java.util.HashMap;
import java.util.List;

@Path("/authors")
@RolesAllowed("READ")
public class AuthorResource {

    private final Logger log = LogManager.getLogger(AuthorResource.class);

    @Inject
    AuthorService authorService;

    @Inject
    BookService bookService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        return Response.ok(authors).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthor(@PathParam("id") int id) {
        Author author = authorService.getAuthor(id);
        return Response.ok(author).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("WRITE")
    public Response addAuthor(Author author) {
        Author result = authorService.persistAuthor(author);
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
    public Response updateAuthor(@Valid Author author) {
        Author currentAuthor = authorService.getAuthor(author.getAuthorId());
        if (currentAuthor == null) {
            return authorNotFound(author.getAuthorId());
        } else {
            Author result = authorService.mergeAuthor(author);
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
    public Response deleteAuthor(@PathParam("id") Integer id) {
        Author author = authorService.getAuthor(id);
        if (author == null) {
            return authorNotFound(id);
        } else {
            boolean result = authorService.removeAuthor(author);
            if (!result) {
                return Response.serverError().build();
            } else {
                return Response.ok(author).build();
            }
        }
    }

    @GET
    @Path("/{id}/books")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooksForAuthor(@PathParam("id") int id){
        Author author = authorService.getAuthor(id);
        List<Book> books = author.getBooks();
        return Response.ok(books).build();
    }

    @PUT
    @Path("/{id}/books")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("WRITE")
    public Response addBookForAuthor(@PathParam("id") Integer authorId, @NotNull(message = "Book id must not be null") @FormParam("bookId") Integer bookId) {
        Author author = authorService.getAuthor(authorId);
        if (author == null) {
            return authorNotFound(authorId);
        } else {
            Book book = bookService.getBook(bookId);
            if (book == null) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(new HashMap<String, String>() {{
                            put("error", "No book found for id " + bookId);
                        }}).build();
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

    @DELETE
    @Path("/{id}/books")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("WRITE")
    public Response removeBookForAuthor(@PathParam("id") int authorId, @NotNull(message = "Book id must not be null") @FormParam("bookId") Integer bookId) {
        Author author = authorService.getAuthor(authorId);
        if (author == null) {
            return authorNotFound(authorId);
        } else {
            Book book = bookService.getBook(bookId);
            if (book == null) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(new HashMap<String, String>() {{
                            put("error", "No book found for id " + bookId);
                        }}).build();
            } else {
                boolean result = authorService.removeBookForAuthor(author, book);
                if (!result) {
                    return Response.serverError().build();
                } else {
                    return Response.ok(author).build();
                }
            }
        }
    }

    private Response authorNotFound(Integer authorId) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new HashMap<String, String>() {{
                    put("error", "no author found for id " + authorId);
                }})
                .build();
    }
}
