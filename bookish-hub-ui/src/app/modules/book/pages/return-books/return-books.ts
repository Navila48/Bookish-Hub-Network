import {Component, OnInit} from '@angular/core';
import {PageResponseBookTransactionResponse} from '../../../../services/models/page-response-book-transaction-response';
import {BookResponse} from '../../../../services/models/book-response';
import {FeedbackRequest} from '../../../../services/models/feedback-request';
import {BookService} from '../../../../services/services/book.service';
import {FeedbacksService} from '../../../../services/services/feedbacks.service';
import {BookTransactionResponse} from '../../../../services/models/book-transaction-response';

@Component({
  selector: 'app-return-books',
  imports: [],
  templateUrl: './return-books.html',
  styleUrl: './return-books.scss'
})
export class ReturnBooks implements OnInit{
  returnedBooks: PageResponseBookTransactionResponse ={};
  page = 0;
  size = 4;
  message: string = '';
  status = 'success';
  constructor(private bookService:BookService) {
  }
  ngOnInit(): void {
    this.findAllReturnedBooks();
  }

  private findAllReturnedBooks() {
    this.bookService.findAllReturnedBooks({
      page:this.page,
      size:this.size
    }).subscribe({
      next:(resp)=>{
        this.returnedBooks = resp;
      }
    });
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllReturnedBooks();
  }

  goToPreviousPage() {
    this.page--;
    this.findAllReturnedBooks();
  }

  goToPage(pageIndex : number) {
    this.page = pageIndex;
    this.findAllReturnedBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllReturnedBooks();
  }

  goToLastPage() {
    this.page = this.returnedBooks.totalPages as number -1;
    this.findAllReturnedBooks();
  }

  range(n:number | undefined) : number[]{
    return n ? Array.from({length: n}) : [] ;
  }

  get isLastPage() : boolean{
    return this.page === this.returnedBooks.totalPages as number -1;
  }

  approveBookReturn(book: BookTransactionResponse) {
    if(!book.returned){
      this.status = 'error';
      this.message = 'The Book is not returned yet';
      return ;
    }
    this.bookService.approveReturnedBook({
      'book-id': book.bookId as number
    }).subscribe({
      next:()=>{
        this.status = 'success';
        this.message = 'Book return approved';
        this.findAllReturnedBooks();
      },
      error:(err)=>{
        console.log(err);
        this.status = 'error';
        this.message = err.error.error;
      }
    })
  }
}
