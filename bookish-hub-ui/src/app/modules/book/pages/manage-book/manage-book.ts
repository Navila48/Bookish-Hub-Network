import {Component, OnInit} from '@angular/core';
import {BookRequest} from '../../../../services/models/book-request';
import {FormsModule} from '@angular/forms';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {BookService} from '../../../../services/services/book.service';

@Component({
  selector: 'app-manage-book',
  imports: [
    FormsModule,
    RouterLink
  ],
  templateUrl: './manage-book.html',
  styleUrls: ['./manage-book.scss']
})
export class ManageBook implements OnInit{
  bookRequest: BookRequest = {authorName: "", isbn: "", synopsis: "", title: ""};
  errorMsg:Array<String> = [];
  selectedBookCover: any;
  selectedPicture: string | undefined;

  constructor(private bookService: BookService,
              private router: Router,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
       const bookId = this.activatedRoute.snapshot.params['bookId'];
       if(bookId){
         this.bookService.findBookById({
           'book-id':bookId
         }).subscribe({
           next: (book)=>{
             this.bookRequest = {
               id: book.bookId,
               title: book.title as string,
               authorName: book.author as string,
               isbn: book.isbn as string,
               synopsis: book.synopsis as string,
               shareable: book.sharable
             };
             if (book.bookCover) {
               this.selectedPicture = 'data:image/jpg;base64,' + book.bookCover;
             }
           }
         })
       }
    }

  onFileSelected(event: any) {
    this.selectedBookCover =  event.target.files[0];
    console.log("Selected Book Cover" + this.selectedBookCover);
    if(this.selectedBookCover){
      const reader = new FileReader();
      reader.onload = () =>{
        this.selectedPicture = reader.result as string;
      }
      reader.readAsDataURL(this.selectedBookCover);
    }
  }

  saveBook() {
    this.bookService.saveBook({
      body: this.bookRequest
    }).subscribe({
      next: (bookId)=> {
        if (!this.selectedBookCover) {
          this.router.navigate(['/books/my-book']);
          return;
        }
        this.bookService.uploadBookCoverPicture({
          'book-id': bookId,
          body: {
           file: this.selectedBookCover as File
          }
        }).subscribe({
          next: () => {
            this.router.navigate(['/books/my-book'])
          }
        });
    },
      error: (err) =>{
        console.log(err);
        this.errorMsg = err.error.validationErrors;
      }
    });
  }

}
