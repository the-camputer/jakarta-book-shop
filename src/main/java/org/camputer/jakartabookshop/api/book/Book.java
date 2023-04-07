package org.camputer.jakartabookshop.api.book;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.ws.rs.core.Link;
import org.camputer.jakartabookshop.api.author.Author;
import org.camputer.jakartabookshop.api.publisher.Publisher;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name="book")
public class Book {

    @Id
    @Column(name="book_id")
    private int bookId;

    @Column(name="title")
    private String title;

    @Column(name="isbn13")
    private String isbn13;

    @Column(name="num_pages")
    private int numPages;

    @Column(name="publication_date")
    private Date publicationDate;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    @JsonbTransient
    private Publisher publisher;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name="book_author",
            joinColumns = { @JoinColumn(name="book_id") },
            inverseJoinColumns = { @JoinColumn(name="author_id") }
    )
    @JsonbTransient
    private List<Author> authors;

    @Transient
    @InjectLinks({
            @InjectLink(value = "book/${instance.getBookId()}", rel = "self"),
            @InjectLink(value = "book/${instance.getBookId()}/authors", rel = "authors"),
            @InjectLink(value = "book/${instance.getBookId()}/publisher", rel = "publisher")
    })
    public List<Link> links;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setBooks(List<Author> authors) {
        this.authors = authors;
    }
}
