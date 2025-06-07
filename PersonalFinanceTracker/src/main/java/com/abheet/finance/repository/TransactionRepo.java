package com.abheet.finance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.abheet.finance.entity.Transaction;
import com.abheet.finance.entity.User;

public interface TransactionRepo extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByUser(User user);
    Optional<Transaction> findByIdAndUser(Integer id, User user);
}
