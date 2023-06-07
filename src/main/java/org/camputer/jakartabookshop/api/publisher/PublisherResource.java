package org.camputer.jakartabookshop.api.publisher;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.camputer.jakartabookshop.api.book.Book;
import org.camputer.jakartabookshop.api.book.BookService;

import java.util.HashMap;
import java.util.List;

@Path("/publisher")
@RolesAllowed("READ")
public class PublisherResource {

    @Inject
    PublisherService publisherService;

    @Inject
    BookService bookService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPublishers() {
        List<Publisher> publishers = publisherService.getAllPublishers();
        return Response.ok(publishers).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPublisher(@Valid Publisher publisher) {
        Publisher result = publisherService.persistPublisher(publisher);
        if (result == null) {
            return Response.serverError().build();
        } else {
            return Response.ok(result).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePublisher(@Valid Publisher publisher) {
        Publisher currentPublisher = publisherService.getPublisher(publisher.getPublisherId());
        if (currentPublisher == null) {
            return publisherNotFound(publisher.getPublisherId());
        } else {
            Publisher result = publisherService.mergePublisher(publisher);
            if (result == null) {
                return Response.serverError().build();
            } else {
                return Response.ok(result).build();
            }
        }
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPublisher(@PathParam("id") int id) {
        Publisher publisher = publisherService.getPublisher(id);
        return Response.ok(publisher).build();
    }
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePublisher(@PathParam("id") int id) {
        Publisher publisher = publisherService.getPublisher(id);
        if (publisher == null) {
            return publisherNotFound(id);
        } else {
            boolean result = publisherService.removePublisher(publisher);
            if (!result) {
                return Response.serverError().build();
            } else {
                return Response.ok(publisher).build();
            }
        }
    }

    @GET
    @Path("/{id}/books")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooksForPublisher(@PathParam("id") int id) {
        List<Book> books = publisherService.getBooksForPublisher(id);
        return Response.ok(books).build();
    }

    @PUT
    @Path("/{publisherId}/books/{bookId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBookForPublisher(@PathParam("publisherId") int publisherId, @PathParam("bookId") int bookId) {
        Publisher publisher = publisherService.getPublisher(publisherId);
        if (publisher == null) {
            return publisherNotFound(publisherId);
        } else {
            Book book = bookService.getBook(bookId);
            if (book == null) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(new HashMap<String, String>() {{
                            put("error", "no book found for id " + bookId);
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
    @Path("/{publisherId}/books/{bookId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeBookForPublisher(@PathParam("publisherId") int publisherId, @PathParam("bookId") int bookId) {
        Publisher publisher = publisherService.getPublisher(publisherId);
        if (publisher == null) {
            return publisherNotFound(publisherId);
        } else {
            Book book = bookService.getBook(bookId);
            if (book == null) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(new HashMap<String, String>() {{
                            put("error", "no book found for id " + bookId);
                        }})
                        .build();
            } else {
                boolean result = bookService.removePublisherForBook(book);
                if (!result) {
                    return Response.serverError().build();
                } else {
                    return Response.ok(book).build();
                }
            }
        }
    }


    private Response publisherNotFound(Integer publisherId) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new HashMap<String, String>() {{
                    put("error", "no publisher found for id " + publisherId);
                }})
                .build();
    }
}
