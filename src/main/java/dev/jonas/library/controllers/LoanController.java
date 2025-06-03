package dev.jonas.library.controllers;

import dev.jonas.library.dtos.loan.LoanCreateDTO;
import dev.jonas.library.dtos.loan.LoanDTO;
import dev.jonas.library.services.LoanService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing loan operations.
 * Provides endpoints to retrieve, create, extend, and return loans.
 */
@Getter
@Setter
@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    /**
     * Constructs the LoanController with required LoanService.
     *
     * @param loanService the service handling loan logic
     */
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    // ########## [ GET ] ##########

    /**
     * Retrieves all loan records.
     *
     * @return list of all loans
     */
    @GetMapping
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        List<LoanDTO> loans = loanService.getAllLoanDTOs();
        return ResponseEntity.ok(loans);
    }

    /**
     * Retrieves a loan by its ID.
     *
     * @param id the loan ID
     * @return the corresponding loan
     */
    @GetMapping("/{id}")
    public ResponseEntity<LoanDTO> getLoanById(@PathVariable Long id) {
        LoanDTO loan = loanService.getLoanById(id);
        return ResponseEntity.ok(loan);
    }

    // ########## [ POST ] ##########

    /**
     * Creates a new loan.
     *
     * @param dto data for the loan to create
     * @return the created loan
     */
    @PostMapping
    public ResponseEntity<LoanDTO> createLoan(@RequestBody LoanCreateDTO dto) {
        LoanDTO createdLoan = loanService.addLoan(dto);
        return ResponseEntity
                .created(URI.create("/loans/" + createdLoan.getLoanId()))
                .body(createdLoan);
    }

    // ########## [ PUT ] ##########

    /**
     * Extends the due date of a loan by 14 days.
     *
     * @param id the ID of the loan to extend
     * @return the updated loan
     */
    @PutMapping("/{id}/extend")
    public ResponseEntity<LoanDTO> extendLoan(@PathVariable Long id) {
        LoanDTO extendedLoan = loanService.extendLoan(id);
        return ResponseEntity.ok(extendedLoan);
    }

    /**
     * Marks a loan as returned.
     *
     * @param id the ID of the loan to return
     * @return the updated loan
     */
    @PutMapping("/{id}/return")
    public ResponseEntity<LoanDTO> returnLoan(@PathVariable Long id) {
        LoanDTO returnedLoan = loanService.returnLoan(id);
        return ResponseEntity.ok(returnedLoan);
    }
}