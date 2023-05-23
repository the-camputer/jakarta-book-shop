package org.camputer.jakartabookshop.api.publisher;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.camputer.jakartabookshop.api.book.Book;

import java.util.List;

@Path("/publisher")
@RolesAllowed("READ")
public class PublisherResource {

    @Inject
    PublisherService publisherService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPublisher(@PathParam("id") int id) {
        Publisher publisher = publisherService.getPublisher(id);
        return Response.ok(publisher).build();
    }

    @GET
    @Path("/{id}/books")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooksForPublisher(@PathParam("id") int id) {
        List<Book> books = publisherService.getBooksForPublisher(id);
        return Response.ok(books).build();
    }
}
