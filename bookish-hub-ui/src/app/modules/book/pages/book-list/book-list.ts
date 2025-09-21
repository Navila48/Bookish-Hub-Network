import { Component, OnInit } from '@angular/core';
import {BookService} from '../../../../services/services/book.service';
import {Router} from '@angular/router';
import {subscribe} from 'node:diagnostics_channel';
import {PageResponseBookResponse} from '../../../../services/models/page-response-book-response';
import {JsonPipe} from '@angular/common';
import {BookCard} from '../../components/book-card/book-card';
import {findAllBooks} from '../../../../services/fn/book/find-all-books';
import {range} from 'rxjs';

@Component({
  selector: 'app-book-list',
  imports: [
    JsonPipe,
    BookCard
  ],
  templateUrl: './book-list.html',
  styleUrl: './book-list.scss'
})
export class BookList implements OnInit {
  page = 0;
  size = 1;
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
}
