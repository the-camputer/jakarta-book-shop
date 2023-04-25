package org.camputer.jakartabookshop.api.author;


import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camputer.jakartabookshop.api.book.Book;

import java.util.List;

@Path("/author")
public class AuthorResource {

    private final Logger log = LogManager.getLogger(AuthorResource.class);

    @Inject
    AuthorService authorService;

    @Context
    UriInfo uriInfo;

    @GET
    @Path("/")
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

    @GET
    @Path("/{id}/books")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooksForAuthor(@PathParam("id") int id){
        Author author = authorService.getAuthor(id);
        List<Book> books = author.getBooks();
        return Response.ok(books).build();
    }
}
