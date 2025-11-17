import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Component, inject, Inject, OnInit, PLATFORM_ID, SimpleChanges } from '@angular/core';
import BookDTO from '../../domain/book';
import { BookAPIService } from '../../services/book-api.service';

@Component({
  selector: 'app-users.component',
  imports: [CommonModule],
  templateUrl: './books.component.html',
  styleUrl: './books.component.scss',
})
export class BooksComponent implements OnInit {
  books: BookDTO[] = [];
  loading = true;

  private bookAPIService: BookAPIService = <any>null;
  //constructor(private userAPIService: UserAPIService) {}4

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {
    if (isPlatformBrowser(this.platformId)) {
      if (!this.bookAPIService) {
        this.bookAPIService = inject(BookAPIService);
      }
    }
  }

  ngOnInit(): void {
    // Only start the timer when there's an actual user in a browser
    if (isPlatformBrowser(this.platformId)) {
      this.reload();
    }
    //this.reload();
  }
  ngOnChanges(changes: SimpleChanges): void {
    this.reload();
  }
  reload() {
    this.bookAPIService.getAll().subscribe({
      next: (list: any) => {
        this.books = list;
      },
      error: (err: any) => console.error(err),
      complete: () => (this.loading = false),
    });
  }

  isPublished(book: BookDTO) {
    return book.published && book.published === '1';
  }
}
