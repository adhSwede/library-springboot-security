package dev.jonas.library.unit.services;

// #################### [ Imports ] ####################

import dev.jonas.library.dtos.book.BookDetailsDTO;
import dev.jonas.library.dtos.loan.LoanCreateDTO;
import dev.jonas.library.dtos.loan.LoanDTO;
import dev.jonas.library.dtos.user.UserDTO;
import dev.jonas.library.entities.Author;
import dev.jonas.library.entities.Book;
import dev.jonas.library.entities.Loan;
import dev.jonas.library.entities.User;
import dev.jonas.library.mappers.EntityToDtoMapper;
import dev.jonas.library.repositories.BookRepository;
import dev.jonas.library.repositories.LoanRepository;
import dev.jonas.library.repositories.UserRepository;
import dev.jonas.library.services.book.BookServiceImpl;
import dev.jonas.library.services.loan.LoanServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceImplTest {

    // #################### [ Mocks ] ####################

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookServiceImpl bookServiceImpl;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    // #################### [ Tests ] ####################

    @Test
    void addLoan_returnsLoanDTO_DecreasesAvailableCopies_onValidInput() {

        // #################### [ Arrange - Entities ] ####################
        Long userId = 1L;
        Long bookId = 1L;

        User user = new User(
                "Hasse",
                "Målvakt",
                "hasse.malvakt@mail.se",
                "123",
                LocalDateTime.now()
        );

        Author author = new Author(
                "J.R.R.",
                "Tolkien",
                1892,
                "England"
        );

        Book book = new Book(
                bookId,
                "The Hobbit",
                author,
                1937,
                5,
                5
        );

        Loan loan = new Loan(
                user,
                book,
                LocalDate.now(),
                LocalDate.now().plusDays(14)
        );

        // #################### [ Arrange - DTOs ] ####################
        BookDetailsDTO bookDetailsDTO = new BookDetailsDTO(
                bookId,
                "The Hobbit",
                "J.R.R. Tolkien",
                1937,
                10,
                4
        );

        UserDTO userDTO = new UserDTO(
                userId,
                "Hasse",
                "Målvakt",
                "hasse.malvakt@mail.se",
                LocalDateTime.now()
        );

        LoanDTO expectedLoanDTO = new LoanDTO(
                1L,
                bookDetailsDTO,
                userDTO,
                LocalDate.now(),
                LocalDate.now().plusDays(14),
                null
        );

        // #################### [ Arrange - Mocks ] ####################
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(bookRepository.findById(bookId))
                .thenReturn(Optional.of(book));

        doAnswer(invocation -> {
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            return null;
        }).when(bookServiceImpl).decrementAvailableCopies(bookId);

        when(loanRepository.save(any(Loan.class)))
                .thenReturn(loan);

        try (MockedStatic<EntityToDtoMapper> mockedStatic = mockStatic(EntityToDtoMapper.class)) {
            mockedStatic.when(() -> EntityToDtoMapper.mapToLoanDto(loan))
                    .thenReturn(expectedLoanDTO);

            // #################### [ Act ] ####################
            LoanDTO result = loanService.addLoan(
                    new LoanCreateDTO(userId, bookId)
            );

            // #################### [ Assert ] ####################
            assertNotNull(result);
            assertEquals("The Hobbit", result.getBook().getTitle());
            assertEquals(4, book.getAvailableCopies());
            assertEquals(LocalDate.now().plusDays(14), result.getDueDate());
        }
    }
}
