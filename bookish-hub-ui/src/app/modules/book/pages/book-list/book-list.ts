import { Component, OnInit } from '@angular/core';
import {BookService} from '../../../../services/services/book.service';
import {Router} from '@angular/router';
import {PageResponseBookResponse} from '../../../../services/models/page-response-book-response';
import {BookCard} from '../../components/book-card/book-card';
import {BookResponse} from '../../../../services/models/book-response';


@Component({
  selector: 'app-book-list',
  imports: [
    BookCard
  ],
  templateUrl: './book-list.html',
  styleUrl: './book-list.scss'
})
export class BookList implements OnInit {
  page = 0;
  size = 2;
  bookResponse: PageResponseBookResponse = { };
  message = '';
  status = 'success';

  constructor(
    private bookService: BookService,
    private router: Router
  ) {
  }

  ngOnInit(){
    this.findALlBooks();
  }

  private findALlBooks() {
      this.bookService.findAllBooks({
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

  borrowBook(book: BookResponse) {
    this.message = '';
    this.bookService.borrowBook({
      'book-id' : book.bookId as number
    }).subscribe({
      next: ()=>{
        this.message = "Book added successfully in your list";
        this.status = 'success';
      },
      error: (err)=>{
        console.log(err)
        this.message = err.error.error;
        this.status = 'error';
      }
    })
  }
}
