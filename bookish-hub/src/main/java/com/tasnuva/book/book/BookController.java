package com.tasnuva.book.book;

import com.tasnuva.book.common.PageResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Integer> saveBook(@Valid @RequestBody BookRequest bookRequest, Authentication connectedUser){
         return ResponseEntity.ok(bookService.saveBook(bookRequest, connectedUser));
    }

    @GetMapping("{book-id}")
    public ResponseEntity<BookResponse> findBookById( @Valid @PathVariable("book-id") Integer bookId){
        return ResponseEntity.ok(bookService.findBookById(bookId));
    }

    @GetMapping()
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                                                   @RequestParam(name="size", defaultValue= "10", required = false) int size,
                                                                   Authentication connectedUser) {
        return ResponseEntity.ok(bookService.findAllDisplayableBooks( page, size, connectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                                                          @RequestParam(name = "size", defaultValue = "10", required = false) int size,
                                                                          Authentication connectedUser) {
        return ResponseEntity.ok(bookService.findAllBooksByOwner(page, size, connectedUser));
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BookTransactionResponse>> findAllBorrowedBooks(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                                                                      @RequestParam(name = "size", defaultValue = "10", required = false) int size,
                                                                                      Authentication connectedUser){
        return ResponseEntity.ok(bookService.findAllBorrowedBooks(page, size, connectedUser));
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BookTransactionResponse>> findAllReturnedBooks(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                                                                      @RequestParam(name = "size", defaultValue = "10", required = false) int size,
                                                                                      Authentication connectedUser){
        return ResponseEntity.ok(bookService.findAllReturnedBooks(page, size, connectedUser));
    }

    @PatchMapping("/shareable/{book-id}")
    public ResponseEntity<Integer> updateShareableStatus(@Valid @PathVariable("book-id") Integer bookId, Authentication connectedUser){
        return ResponseEntity.ok(bookService.updateShareableStatus(bookId, connectedUser));
    }

    @PatchMapping("/archived/{book-id}")
    public ResponseEntity<Integer> updateArchivedStatus(@Valid @PathVariable("book-id") Integer bookId, Authentication connectedUser){
        return ResponseEntity.ok(bookService.updateArchivedStatus(bookId, connectedUser));
    }

    @PostMapping("/borrow/{book-id}")
    ResponseEntity<Integer> borrowBook(@Valid @PathVariable("book-id") Integer bookId, Authentication connectedUser){
        return ResponseEntity.ok(bookService.borrowBook(bookId,connectedUser));
    }

    @PatchMapping("borrow/return/{book-id}")
    ResponseEntity<Integer> returnBook(@Valid @PathVariable("book-id") Integer bookId, Authentication connectedUser){
        return ResponseEntity.ok(bookService.returnBook(bookId, connectedUser));
    }

    @PatchMapping("/borrow/return/approve/{book-id}")
    ResponseEntity<Integer> approveReturnedBook(@Valid @PathVariable("book-id") Integer bookId, Authentication connectedUser){
        return ResponseEntity.ok(bookService.approveReturnedBook(bookId, connectedUser));
    }

    @PostMapping(value = "/cover/{book-id}", consumes = "multipart/form-data")
    ResponseEntity<?> uploadBookCoverPicture(@Valid @PathVariable("book-id") Integer bookId, @Parameter() @RequestPart("file")MultipartFile file, Authentication connectedUser){
        bookService.uploadBookCoverPicture(file, bookId, connectedUser);
        return ResponseEntity.accepted().build();
    }
}
