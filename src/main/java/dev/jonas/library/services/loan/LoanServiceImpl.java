package dev.jonas.library.services.loan;

import dev.jonas.library.dtos.loan.LoanCreateDTO;
import dev.jonas.library.dtos.loan.LoanDTO;
import dev.jonas.library.entities.Book;
import dev.jonas.library.entities.Loan;
import dev.jonas.library.entities.User;
import dev.jonas.library.exceptions.api.BookUnavailableException;
import dev.jonas.library.exceptions.api.LoanAlreadyReturnedException;
import dev.jonas.library.mappers.DtoToEntityMapper;
import dev.jonas.library.mappers.EntityToDtoMapper;
import dev.jonas.library.mappers.RolesToAuthorityMapper;
import dev.jonas.library.repositories.BookRepository;
import dev.jonas.library.repositories.LoanRepository;
import dev.jonas.library.repositories.UserRepository;
import dev.jonas.library.security.UserAccessValidator;
import dev.jonas.library.services.book.BookServiceImpl;
import dev.jonas.library.utils.EntityFetcher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for managing loans in the library system.
 * Handles creating, extending, returning loans, and retrieving loan records.
 */
@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookServiceImpl bookServiceImpl;
    private final RolesToAuthorityMapper rolesToAuthorityMapper;
    private final UserAccessValidator userAccessValidator;

    // ==================== [ GET ] ====================
    @Override
    public List<LoanDTO> getAllLoanDTOs() {
        List<Loan> loans = loanRepository.findAll();
        List<LoanDTO> DTOs = new ArrayList<>();

        for (Loan loan : loans) {
            LoanDTO dto = new LoanDTO(
                    loan.getLoanId(),
                    EntityToDtoMapper.mapToBookDetailsDtoSimple(loan.getBook()),
                    EntityToDtoMapper.mapToUserDtoSimple(loan.getUser()),
                    loan.getBorrowedDate(),
                    loan.getDueDate(),
                    loan.getReturnedDate()
            );
            DTOs.add(dto);
        }

        return DTOs; // Using a loop for clarity, could also use streams
    }

    @Override
    public List<LoanDTO> getLoansByUserId(Long userId) {
        return loanRepository.findByUser_userId(userId)
                .stream()
                .map(EntityToDtoMapper::mapToLoanDto)
                .toList();
    }

    public List<LoanDTO> getLoansByUserEmail(String email) {
        User user = EntityFetcher.getUserOrThrow(email, userRepository);
        return loanRepository.findByUser_userId(user.getUserId())
                .stream()
                .map(EntityToDtoMapper::mapToLoanDto)
                .toList();
    }

    @Override
    public LoanDTO getLoanById(Long loanId) {
        Loan loan = EntityFetcher.getLoanOrThrow(loanId, loanRepository);
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByEmailIgnoreCase(currentEmail)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        userAccessValidator.validateUserAccess(currentUser.getUserId()); // Validate access

        return EntityToDtoMapper.mapToLoanDto(loan);
    }

    // ==================== [ POST ] ====================
    @Override
    @Transactional
    public LoanDTO addLoan(LoanCreateDTO dto) {
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

    // ==================== [ PUT ] ====================
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