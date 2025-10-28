import {Component, OnInit} from '@angular/core';
import {PageResponseBookTransactionResponse} from '../../../../services/models/page-response-book-transaction-response';
import {BookTransactionResponse} from '../../../../services/models/book-transaction-response';
import {BookService} from '../../../../services/services/book.service';
import {BookResponse} from '../../../../services/models/book-response';
import {FeedbackRequest} from '../../../../services/models/feedback-request';
import {FormsModule} from '@angular/forms';
import {Rating} from '../../components/rating/rating';
import {FeedbacksService} from '../../../../services/services/feedbacks.service';

@Component({
  selector: 'app-borrowed-book-list',
  imports: [
    FormsModule,
    Rating
  ],
  templateUrl: './borrowed-book-list.html',
  styleUrl: './borrowed-book-list.scss'
})
export class BorrowedBookList implements OnInit{

  borrowedBooks: PageResponseBookTransactionResponse ={};
  selectedBook: BookResponse | undefined = undefined;
  feedbackRequest: FeedbackRequest = {bookId: 0, comment: '', rating: 0};
  page = 0;
  size = 4;
  constructor(private bookService:BookService,
  private feedbackService: FeedbacksService) {
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
    this.selectedBook = book;
    this.feedbackRequest.bookId = book.bookId as number;
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

  returnBook(withFeedback: boolean) {
    this.bookService.returnBook({
      'book-id': this.selectedBook?.bookId as number
    }).subscribe({
      next:()=>{
        if(withFeedback){
          this.giveFeedback();
        }
        this.selectedBook = undefined;
        this.findAllBorrowedBooks();
      }
    })
  }

  private giveFeedback() {
    this.feedbackService.saveFeedback({
      body: this.feedbackRequest
    }).subscribe({
      next:()=>{

      }
    })
  }
}
