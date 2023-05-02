insert into AUTHOR(author_id, author_name)
VALUES (next value for seq_author_id, 'N.K. Jemisin');

insert into PUBLISHER(PUBLISHER_ID, publisher_name)
VALUES (next value for seq_publisher_id, 'Hachette Book Group');

insert into BOOK(book_id, title, isbn13, num_pages, publication_date, publisher_id)
VALUES (next value for seq_book_id, 'The Fifth Season', '9780316229302', 512, '2015-09-04', 100),
       (next value for seq_book_id, 'The Obelisk Gate', '9780316229289', 448, '2016-09-16', 100),
       (next value for seq_book_id, 'The Stone Sky', '9780316229258', 464, '2017-09-15', 100);

insert into BOOK_AUTHOR(book_id, author_id)
VALUES (11, 100),
       (13, 100),
       (14, 100);