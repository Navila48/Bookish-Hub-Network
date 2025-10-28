import {Component, OnInit} from '@angular/core';
import {PageResponseBookTransactionResponse} from '../../../../services/models/page-response-book-transaction-response';
import {BookTransactionResponse} from '../../../../services/models/book-transaction-response';
import {BookService} from '../../../../services/services/book.service';

@Component({
  selector: 'app-borrowed-book-list',
  imports: [],
  templateUrl: './borrowed-book-list.html',
  styleUrl: './borrowed-book-list.scss'
})
export class BorrowedBookList implements OnInit{

  borrowedBooks: PageResponseBookTransactionResponse ={};
  page = 0;
  size = 1;
  constructor(private bookService:BookService) {
  }
  ngOnInit(): void {
   this.findAllBorrowedBooks();
  }

  private findAllBorrowedBooks() {
    this.bookService.findAllBorrowedBooks({
      page:this.page,
      size:this.size
    }).subscribe({
      next:(resp)=>{
        this.borrowedBooks = resp;
      }
    });
  }
  returnBorrowedBooks(book: BookTransactionResponse) {

  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBorrowedBooks();
  }

  goToPreviousPage() {
    this.page--;
    this.findAllBorrowedBooks();
  }

  goToPage(pageIndex : number) {
    this.page = pageIndex;
    this.findAllBorrowedBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllBorrowedBooks();
  }

  goToLastPage() {
    this.page = this.borrowedBooks.totalPages as number -1;
    this.findAllBorrowedBooks();
  }

  range(n:number | undefined) : number[]{
    return n ? Array.from({length: n}) : [] ;
  }

  get isLastPage() : boolean{
    return this.page === this.borrowedBooks.totalPages as number -1;
  }

}
