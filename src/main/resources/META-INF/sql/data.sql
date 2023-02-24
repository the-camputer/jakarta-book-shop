insert into AUTHOR(author_id, author_name) VALUES (
                                                   1, 'N.K. Jemisin'
                                                  );

insert into PUBLISHER(publisher_id, publisher_name) VALUES (
                                                            1, 'Hachette Book Group'
                                                           );

insert into BOOK(book_id, title, isbn13, num_pages, publication_date, publisher_id) VALUES (
                                                                                            1, 'The Fifth Season', 9780316229302, 512, '2015-09-04', 1
                                                                                           ),
                                                                                           (
                                                                                            2, 'The Obelisk Gate', 9780316229289, 448, '2016-09-16', 1
                                                                                           ),
                                                                                           (
                                                                                            3, 'The Stone Sky', 9780316229258, 464, '2017-09-15', 1
                                                                                           );