import { ResultSetHeader } from "mysql2";
import Book from "../../domain/book";
import { MysqlHandler } from "../mysql-handler";

export interface BookDAO {
  save(book: Book): Promise<Book>;
  retrieveAll(searchParams: {
    title?: string;
    published?: boolean;
  }): Promise<Book[]>;
  retrieveById(bookId: number): Promise<Book | undefined>;
  update(book: Book): Promise<number>;
  delete(bookId: number): Promise<number>;
  deleteAll(): Promise<number>;
}

export class BookDAOImpl implements BookDAO {
  connection = new MysqlHandler().connection;
  save(book: Book): Promise<Book> {
    return new Promise((resolve, reject) => {
      this.connection.query<ResultSetHeader>(
        "INSERT INTO books (title, description, published) VALUES(?,?,?)",
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

  retrieveAll(searchParams: {
    title?: string;
    published?: boolean;
  }): Promise<Book[]> {
    let query: string = "SELECT * FROM books";

    let condition: string = "";

    if (searchParams?.published) condition += "published = TRUE";

    if (searchParams?.title)
      condition += `LOWER(title) LIKE '%${searchParams.title}%'`;

    if (condition.length) query += " WHERE " + condition;

    return new Promise((resolve, reject) => {
      this.connection.query<Book[]>(query, (err: any, res: any) => {
        if (err) reject(err);
        else resolve(res);
      });
    });
  }

  retrieveById(bookId: number): Promise<Book> {
    return new Promise((resolve, reject) => {
      this.connection.query<Book[]>(
        "SELECT * FROM books WHERE id = ?",
        [bookId],
        (err: any, res: any) => {
          if (err) reject(err);
          else resolve(res?.[0]);
        }
      );
    });
  }

  update(book: Book): Promise<number> {
    return new Promise((resolve, reject) => {
      this.connection.query<ResultSetHeader>(
        "UPDATE books SET title = ?, description = ?, published = ? WHERE id = ?",
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
        "DELETE FROM books WHERE id = ?",
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
      this.connection.query<ResultSetHeader>(
        "DELETE FROM books",
        (err: any, res: any) => {
          if (err) reject(err);
          else resolve(res.affectedRows);
        }
      );
    });
  }
}
