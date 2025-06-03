package dev.jonas.library.repositories;

import dev.jonas.library.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// ########## [ Queries ] ##########
@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUser_userId(Long userId);
}