import {Component, OnInit} from '@angular/core';
import {BookCard} from "../../components/book-card/book-card";
import {PageResponseBookResponse} from '../../../../services/models/page-response-book-response';
import {BookService} from '../../../../services/services/book.service';
import {Router, RouterLink} from '@angular/router';
import {BookResponse} from '../../../../services/models/book-response';

@Component({
  selector: 'app-my-book',
  imports: [
    BookCard,
    RouterLink
  ],
  templateUrl: './my-book.html',
  styleUrl: './my-book.scss'
})
export class MyBook implements OnInit {
  page = 0;
  size = 2;
  bookResponse: PageResponseBookResponse = { };

  constructor(
    private bookService: BookService,
    private router: Router
  ) {
  }

  ngOnInit(){
    this.findALlBooks();
  }

  private findALlBooks() {
    this.bookService.findAllBooksByOwner({
      page: this.page,
      size: this.size
    })
      .subscribe({
        next: (books)=>{
          console.log('API Response:', books);
          this.bookResponse = books;
        }
      });
  }

  goToFirstPage() {
    this.page = 0;
    this.findALlBooks();
  }

  goToPreviousPage() {
    this.page--;
    this.findALlBooks();
  }

  goToPage(pageIndex : number) {
    this.page = pageIndex;
    this.findALlBooks();
  }

  goToNextPage() {
    this.page++;
    this.findALlBooks();
  }

  goToLastPage() {
    this.page = this.bookResponse.totalPages as number -1;
    this.findALlBooks();
  }

  range(n:number | undefined) : number[]{
    return n ? Array.from({length: n}) : [] ;
  }

  get isLastPage() : boolean{
    return this.page === this.bookResponse.totalPages as number -1;
  }

  archiveBook(book: BookResponse) {
    this.bookService.updateArchivedStatus({
      'book-id': book.bookId as number
    }).subscribe({
      next:()=>{
        book.archived = !book.archived
      }
    });
  }

  shareBook(book: BookResponse) {
    this.bookService.updateShareableStatus({
      'book-id': book.bookId as number
    }).subscribe({
      next:()=>{
        book.sharable= !book.sharable
      }
    });
  }

  editBook(book: BookResponse) {
    this.router.navigate(['books','manage',book.bookId])
  }
}
