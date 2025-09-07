package dev.jonas.library.services.loan;

import dev.jonas.library.dtos.loan.LoanCreateDTO;
import dev.jonas.library.dtos.loan.LoanDTO;
import dev.jonas.library.entities.Book;
import dev.jonas.library.entities.Loan;
import dev.jonas.library.entities.User;
import dev.jonas.library.exceptions.BookUnavailableException;
import dev.jonas.library.exceptions.LoanAlreadyReturnedException;
import dev.jonas.library.mappers.DtoToEntityMapper;
import dev.jonas.library.mappers.EntityToDtoMapper;
import dev.jonas.library.repositories.BookRepository;
import dev.jonas.library.repositories.LoanRepository;
import dev.jonas.library.repositories.UserRepository;
import dev.jonas.library.services.book.BookServiceImpl;
import dev.jonas.library.utils.EntityFetcher;
import dev.jonas.library.utils.InputValidator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for managing loans in the library system.
 * Handles creating, extending, returning loans, and retrieving loan records.
 */
@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookServiceImpl bookServiceImpl;

    // #################### [ Constructor ] ####################

    /**
     * Constructs the LoanServiceImpl with required repositories and book service.
     */
    public LoanServiceImpl(
            LoanRepository loanRepository,
            UserRepository userRepository,
            BookRepository bookRepository,
            BookServiceImpl bookServiceImpl
    ) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.bookServiceImpl = bookServiceImpl;
    }

    // #################### [ GET ] ####################

    /**
     * Retrieves all loans.
     *
     * @return a list of LoanDTOs
     */
    @Override
    public List<LoanDTO> getAllLoanDTOs() {
        List<Loan> loans = loanRepository.findAll();
        List<LoanDTO> dtos = new ArrayList<>();

        for (Loan loan : loans) {
            LoanDTO dto = new LoanDTO(
                    loan.getLoanId(),
                    EntityToDtoMapper.mapToBookDetailsDtoSimple(loan.getBook()),
                    EntityToDtoMapper.mapToUserDtoSimple(loan.getUser()),
                    loan.getBorrowedDate(),
                    loan.getDueDate(),
                    loan.getReturnedDate()
            );
            dtos.add(dto);
        }

        return dtos;
    }

    /**
     * Retrieves all loans for a given user.
     *
     * @param userId the user's ID
     * @return list of loans for that user
     */
    @Override
    public List<LoanDTO> getLoansByUserId(Long userId) {
        return loanRepository.findByUser_userId(userId)
                .stream()
                .map(EntityToDtoMapper::mapToLoanDto)
                .toList();
    }

    /**
     * Retrieves a single loan by its ID.
     *
     * @param loanId the loan's ID
     * @return the loan as LoanDTO
     */
    @Override
    public LoanDTO getLoanById(Long loanId) {
        Loan loan = EntityFetcher.getLoanOrThrow(loanId, loanRepository);
        return EntityToDtoMapper.mapToLoanDto(loan);
    }

    // #################### [ POST ] ####################

    /**
     * Adds a new loan for a user and a book.
     * Decrements the book's available copies.
     * Default loan period is always 14 days.
     *
     * @param dto loan creation data
     * @return the created loan as LoanDTO
     */
    @Override
    @Transactional
    public LoanDTO addLoan(LoanCreateDTO dto) {
        InputValidator.requireNonNull(dto.getUserId(), "User ID");
        InputValidator.requireNonNull(dto.getBookId(), "Book ID");

        User user = EntityFetcher.getUserOrThrow(dto.getUserId(), userRepository);
        Book book = EntityFetcher.getBookOrThrow(dto.getBookId(), bookRepository);

        if (book.getAvailableCopies() <= 0) {
            throw new BookUnavailableException("No available copies of this book.");
        }

        bookServiceImpl.decrementAvailableCopies(book.getBookId());

        Loan loan = DtoToEntityMapper.mapToLoanEntity(dto, user, book);
        loan.setBorrowedDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));

        Loan savedLoan = loanRepository.save(loan);
        return EntityToDtoMapper.mapToLoanDto(savedLoan);
    }

    // #################### [ PUT ] ####################

    /**
     * Extends the due date of a loan.
     * Default extension period is always 14 days.
     *
     * @param loanId the loan's ID
     * @return the updated loan
     */
    @Override
    @Transactional
    public LoanDTO extendLoan(Long loanId) {
        Loan loan = EntityFetcher.getLoanOrThrow(loanId, loanRepository);

        if (loan.getReturnedDate() != null) {
            throw new LoanAlreadyReturnedException("Cannot extend a returned loan.");
        }

        loan.setDueDate(LocalDate.now().plusDays(14));

        Loan savedLoan = loanRepository.save(loan);
        return EntityToDtoMapper.mapToLoanDto(savedLoan);
    }

    /**
     * Marks a loan as returned and increases available book copies.
     *
     * @param loanId the loan's ID
     * @return the updated loan
     */
    @Override
    @Transactional
    public LoanDTO returnLoan(Long loanId) {
        Loan loan = EntityFetcher.getLoanOrThrow(loanId, loanRepository);

        if (loan.getReturnedDate() != null) {
            throw new LoanAlreadyReturnedException("Loan has already been returned.");
        }

        loan.setReturnedDate(LocalDate.now());
        bookServiceImpl.incrementAvailableCopies(loan.getBook().getBookId());

        Loan savedLoan = loanRepository.save(loan);
        return EntityToDtoMapper.mapToLoanDto(savedLoan);
    }

}

