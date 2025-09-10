import { Component, OnInit } from '@angular/core';
import {BookService} from '../../../../services/services/book.service';
import {Router} from '@angular/router';
import {subscribe} from 'node:diagnostics_channel';
import {PageResponseBookResponse} from '../../../../services/models/page-response-book-response';

@Component({
  selector: 'app-book-list',
  imports: [],
  templateUrl: './book-list.html',
  styleUrl: './book-list.scss'
})
export class BookList implements OnInit {
  private page = 0;
  private size = 5;
  bookResponse : PageResponseBookResponse = {};

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
      }).subscribe(
        {
          next: (books)=>{
            this.bookResponse = books;
          }
        }
      );
  }
}
