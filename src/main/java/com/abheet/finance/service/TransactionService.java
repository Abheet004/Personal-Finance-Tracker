package com.abheet.finance.service;

import java.util.List;
import java.util.Optional;

import com.abheet.finance.entity.Transaction;
import com.abheet.finance.entity.User;

public interface TransactionService {
    boolean saveTransaction(Transaction txn);
    List<Transaction> getAllTransactionsByUser(User user);
    Optional<Transaction> getTransactionByIdAndUser(int id, User user);

    List<Transaction> getFilteredTransactions(User user, String type, String category, String fromDate, String toDate);

    boolean deleteTransactionByIdAndUser(int id, User user);
    
}
