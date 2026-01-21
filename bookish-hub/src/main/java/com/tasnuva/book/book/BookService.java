package com.tasnuva.book.book;

import com.tasnuva.book.bookTransactionHistory.BookTransactionHistory;
import com.tasnuva.book.bookTransactionHistory.BookTransactionHistoryRepository;
import com.tasnuva.book.common.PageResponse;
import com.tasnuva.book.file.FileStorageService;
import com.tasnuva.book.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.tasnuva.book.book.BookSpecification.withOwnerId;

@RequiredArgsConstructor
@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookTransactionHistoryRepository transactionHistoryRepository;
    private final FileStorageService fileStorageService;

    public Integer saveBook(BookRequest bookRequest, Authentication connectedUser) {
     //   User user = (User) connectedUser.getPrincipal();
        Book book = bookMapper.convertToBook(bookRequest);
     //   book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    public BookResponse findBookById(@Valid Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper :: convertToBookResponse)
                .orElseThrow(()-> new EntityNotFoundException("Book not found with ID ::" + bookId));

    }

    public PageResponse<BookResponse> findAllDisplayableBooks(int page, int size, Authentication connectedUser) {
    //    User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, connectedUser.getName());
        List<BookResponse> bookResponse = books.stream().map(bookMapper :: convertToBookResponse).toList();
        return new PageResponse<>(bookResponse, books.getNumber(), books.getSize(),books.getTotalElements(), books.getTotalPages(), books.isFirst(), books.isLast());
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
     //   User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAll(withOwnerId(connectedUser.getName()), pageable);
        List<BookResponse> bookResponse = books.stream().map(bookMapper :: convertToBookResponse).toList();
        return  new PageResponse<>(bookResponse, books.getNumber(),books.getSize(),books.getTotalElements(), books.getTotalPages(), books.isFirst(), books.isLast());
    }

    public PageResponse<BookTransactionResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
       // User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBookHistory = transactionHistoryRepository.findAllBorrowedBooks(pageable, connectedUser.getName());
        List<BookTransactionResponse> borrowedBookResponses = allBorrowedBookHistory.stream().map(bookMapper :: convertToBorrowedBookResponse).toList();
        return new PageResponse<>(borrowedBookResponses, allBorrowedBookHistory.getNumber(), allBorrowedBookHistory.getSize(),
                allBorrowedBookHistory.getTotalElements(), allBorrowedBookHistory.getTotalPages(), allBorrowedBookHistory.isFirst(), allBorrowedBookHistory.isLast());

    }

    public PageResponse<BookTransactionResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
      //  User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allReturnedBookHistory = transactionHistoryRepository.findAllReturnedBooks(pageable, connectedUser.getName());
        List<BookTransactionResponse> returnedBookResponses = allReturnedBookHistory.stream().map(bookMapper :: convertToBorrowedBookResponse).toList();
        return new PageResponse<>(returnedBookResponses, allReturnedBookHistory.getNumber(), allReturnedBookHistory.getSize(),
                allReturnedBookHistory.getTotalElements(), allReturnedBookHistory.getTotalPages(), allReturnedBookHistory.isFirst(), allReturnedBookHistory.isLast());

    }

    public Integer updateShareableStatus(@Valid Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new EntityNotFoundException("Book not found with ID ::" + bookId));
    //    User user = (User) connectedUser.getPrincipal();
        if(!Objects.equals(book.getCreatedBy(), connectedUser.getName())){
            throw new OperationNoPermittedException("You can't set the shareable status of others book");
        }
        book.setSharable(!book.isSharable());
        bookRepository.save(book);
        return bookId;
    }

    public Integer updateArchivedStatus(@Valid Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new EntityNotFoundException("Book not found with ID ::" + bookId));
    //    User user = (User) connectedUser.getPrincipal();
        if(!Objects.equals(book.getCreatedBy(), connectedUser.getName())){
            throw new OperationNoPermittedException("You can't set the archived status of others book");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    public Integer borrowBook(@Valid Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new EntityNotFoundException("Book not found with ID ::" + bookId));
    //    User user = (User) connectedUser.getPrincipal();
        if(Objects.equals(book.getCreatedBy(), connectedUser.getName())){
            throw new OperationNoPermittedException("You can't borrow your own book");
        }
        if(book.isArchived() || !book.isSharable()){
            throw new OperationNoPermittedException("You can't borrow the book as it is archived or not shareable");
        }
        boolean isAlreadyBorrowedByThisUser = transactionHistoryRepository.isAlreadyBorrowedByUser(bookId, connectedUser.getName());
        if(isAlreadyBorrowedByThisUser){
            throw new OperationNoPermittedException("Your already borrowed this book and it is not returned or the return is not approved yet");
        }
        boolean isAlreadyBorrowedByOtherUser = transactionHistoryRepository.isAlreadyBorrowed(bookId, connectedUser.getName());
        if(isAlreadyBorrowedByOtherUser){
            throw new OperationNoPermittedException("The Book is already borrowed by other user");
        }
        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .userId(connectedUser.getName())
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        return transactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer returnBook(@Valid Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new EntityNotFoundException("Book not found with ID ::" + bookId));
    //    User user = (User) connectedUser.getPrincipal();
        if(Objects.equals(book.getCreatedBy(), connectedUser.getName())){
            throw new OperationNoPermittedException("You can't return your own book");
        }
        if(book.isArchived() || !book.isSharable()){
            throw new OperationNoPermittedException("You can't return the book as it is archived or not shareable");
        }

        BookTransactionHistory bookTransactionHistory = transactionHistoryRepository.findByBookIdAndUserId(bookId, connectedUser.getName())
                .orElseThrow(()-> new EntityNotFoundException("You didn't borrow the book"));
        bookTransactionHistory.setReturned(true);
        return transactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer approveReturnedBook(@Valid Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new EntityNotFoundException("Book not found with ID ::" + bookId));
        User user = (User) connectedUser.getPrincipal();

        if(book.isArchived() || !book.isSharable()){
            throw new OperationNoPermittedException("You can't approved as returned book as it is archived or not shareable");
        }
        if(!Objects.equals(book.getCreatedBy(), connectedUser.getName())){
            throw new OperationNoPermittedException("You can't approve the return of a book you don't own");
        }

        BookTransactionHistory bookTransactionHistory = transactionHistoryRepository.findByBookIdAndOwnerId(bookId, connectedUser.getName())
                .orElseThrow(()-> new OperationNoPermittedException("The book is not returned yet. You can't approve its return"));
        bookTransactionHistory.setReturnApproved(true);
        return transactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public void uploadBookCoverPicture(MultipartFile sourceFile, @Valid Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new EntityNotFoundException("Book not found with ID ::" + bookId));
     //   User user = (User) connectedUser.getPrincipal();
        var bookCoverPhoto = fileStorageService.saveFile(sourceFile, connectedUser.getName());
        book.setBookCover(bookCoverPhoto);
        bookRepository.save(book);
    }
}
