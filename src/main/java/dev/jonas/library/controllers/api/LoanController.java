package dev.jonas.library.controllers.api;

import dev.jonas.library.dtos.loan.LoanCreateDTO;
import dev.jonas.library.dtos.loan.LoanDTO;
import dev.jonas.library.services.loan.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing loan operations.
 * Provides endpoints to retrieve, create, extend, and return loans.
 */
@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    // ==================== [ GET ] ====================
    @GetMapping
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        List<LoanDTO> loans = loanService.getAllLoanDTOs();
        return ResponseEntity.ok(loans);
    }

    @PreAuthorize("@userAccessValidator.isAdminOrSelf(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<LoanDTO> getLoanById(@PathVariable Long id) {
        LoanDTO loan = loanService.getLoanById(id);
        return ResponseEntity.ok(loan);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/user")
    public ResponseEntity<List<LoanDTO>> getLoansForCurrentUser(Authentication auth) {
        String email = auth.getName();
        List<LoanDTO> userLoans = loanService.getLoansByUserEmail(email);
        return ResponseEntity.ok(userLoans);
    }

    // ==================== [ POST ] ====================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<LoanDTO> createLoan(@RequestBody @Valid LoanCreateDTO dto) {
        LoanDTO createdLoan = loanService.addLoan(dto);
        return ResponseEntity
                .created(URI.create("/loans/" + createdLoan.getLoanId()))
                .body(createdLoan);
    }

    // ==================== [ PUT ] ====================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/extend")
    public ResponseEntity<LoanDTO> extendLoan(@PathVariable Long id) {
        LoanDTO extendedLoan = loanService.extendLoan(id);
        return ResponseEntity.ok(extendedLoan);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/return")
    public ResponseEntity<LoanDTO> returnLoan(@PathVariable Long id) {
        LoanDTO returnedLoan = loanService.returnLoan(id);
        return ResponseEntity.ok(returnedLoan);
    }
}