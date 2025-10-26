import { ResultSetHeader } from 'mysql2';
import BookDTO from '../../../app/domain/book';
import BookEntity from '../../domain/book';
import { MysqlHandler } from '../mysql-handler';

export interface BookDAO {
  save(book: BookDTO): Promise<BookEntity>;
  retrieveAll(searchParams: { title?: string; published?: boolean }): Promise<BookDTO[]>;
  retrieveById(bookId: number): Promise<BookEntity | undefined>;
  update(book: BookDTO): Promise<number>;
  delete(bookId: number): Promise<number>;
  deleteAll(): Promise<number>;
}

export class BookDAOImpl implements BookDAO {
  connection = new MysqlHandler().connection;
  save(book: BookDTO): Promise<BookEntity> {
    return new Promise((resolve, reject) => {
      this.connection.query<ResultSetHeader>(
        'INSERT INTO books (title, description, published) VALUES(?,?,?)',
        [book.title, book.description, book.published ? book.published : false],
        (err: any, res: any) => {
          if (err) reject(err);
          else
            this.retrieveById(res.insertId)
              .then((book) => resolve(book!))
              .catch(reject);
        }
      );
    });
  }

  retrieveAll(searchParams: { title?: string; published?: boolean }): Promise<BookDTO[]> {
    let query: string = 'SELECT * FROM books';

    let condition: string = '';

    if (searchParams?.published) condition += 'published = TRUE';

    if (searchParams?.title) condition += `LOWER(title) LIKE '%${searchParams.title}%'`;

    if (condition.length) query += ' WHERE ' + condition;

    return new Promise((resolve, reject) => {
      let books: BookDTO[];
      this.connection.query<BookEntity[]>(query, (err: any, res: any) => {
        books = this.convertAndAddTo(res);
        if (err) reject(err);
        else resolve(books);
      });
    });
  }

  retrieveById(bookId: number): Promise<BookEntity> {
    return new Promise((resolve, reject) => {
      this.connection.query<BookEntity[]>(
        'SELECT * FROM books WHERE id = ?',
        [bookId],
        (err: any, res: any) => {
          if (err) reject(err);
          else resolve(res?.[0]);
        }
      );
    });
  }

  update(book: BookDTO): Promise<number> {
    return new Promise((resolve, reject) => {
      this.connection.query<ResultSetHeader>(
        'UPDATE books SET title = ?, description = ?, published = ? WHERE id = ?',
        [book.title, book.description, book.published, book.id],
        (err: any, res: any) => {
          if (err) reject(err);
          else resolve(res.affectedRows);
        }
      );
    });
  }

  delete(bookId: number): Promise<number> {
    return new Promise((resolve, reject) => {
      this.connection.query<ResultSetHeader>(
        'DELETE FROM books WHERE id = ?',
        [bookId],
        (err: any, res: any) => {
          if (err) reject(err);
          else resolve(res.affectedRows);
        }
      );
    });
  }

  deleteAll(): Promise<number> {
    return new Promise((resolve, reject) => {
      this.connection.query<ResultSetHeader>('DELETE FROM books', (err: any, res: any) => {
        if (err) reject(err);
        else resolve(res.affectedRows);
      });
    });
  }

  private convertAndAddTo(entities: BookEntity[]) {
    const books: BookDTO[] = [];
    entities.forEach((dbBook) => {
      const book: BookDTO = {
        id: dbBook.id,
        description: dbBook.description,
        title: dbBook.title,
        published: dbBook.published,
      };
      books.push(book);
    });
    return books;
  }
}
