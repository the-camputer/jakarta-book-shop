package org.camputer.jakartabookshop.book;

import jakarta.persistence.*;
import org.camputer.jakartabookshop.publisher.Publisher;

import java.sql.Date;

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
    private Publisher publisher;

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
}
