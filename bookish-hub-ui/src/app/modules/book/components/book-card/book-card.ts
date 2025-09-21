import {Component, EventEmitter, Input, Output} from '@angular/core';
import {BookResponse} from '../../../../services/models/book-response';
import {Rating} from '../rating/rating';

@Component({
  selector: 'app-book-card',
  imports: [
    Rating
  ],
  templateUrl: './book-card.html',
  styleUrl: './book-card.scss'
})
export class BookCard {

  private _book : BookResponse = {};
  private _bookCover : String | undefined;
  private _manage = false;

  get book(): BookResponse {
    return this._book;
  }

  @Input()
  set book(value: BookResponse) {
    this._book = value;
  }

  get bookCover(): String | undefined {
    if(this._book.bookCover){
      return 'data:image/jpg;base64, '+ this._book.bookCover;
    }
    return 'https://picsum.photos/1900/800';
  }

  get manage(): boolean {
    return this._manage;
  }

  @Input()
  set manage(value: boolean) {
    this._manage = value;
  }

  @Output() private detail : EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private borrow : EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private addToWishList : EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private edit : EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private share : EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private archive : EventEmitter<BookResponse> = new EventEmitter<BookResponse>();

  onShowDetails() {
    this.detail.emit(this._book);
  }

  onBorrow() {
    this.borrow.emit(this._book);
  }

  onAddToWishList() {
    this.addToWishList.emit(this._book);
  }

  onEdit() {
    this.edit.emit(this._book);
  }

  onShare() {
    this.share.emit(this._book);
  }

  onArchive() {
    this.archive.emit(this._book);
  }
}
