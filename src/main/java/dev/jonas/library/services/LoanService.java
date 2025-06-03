package dev.jonas.library.services;

import dev.jonas.library.dtos.loan.LoanCreateDTO;
import dev.jonas.library.dtos.loan.LoanDTO;

import java.util.List;

public interface LoanService {

    // #################### [ GET ] ####################
    List<LoanDTO> getAllLoanDTOs();

    List<LoanDTO> getLoansByUserId(Long userId);

    LoanDTO getLoanById(Long loanId);

    // #################### [ POST ] ####################
    LoanDTO addLoan(LoanCreateDTO dto);

    // #################### [ PUT] ####################
    LoanDTO extendLoan(Long loanId);

    LoanDTO returnLoan(Long loanId);

}
