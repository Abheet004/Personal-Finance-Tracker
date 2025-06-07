package com.abheet.finance.serviceImplements;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abheet.finance.entity.Transaction;
import com.abheet.finance.entity.User;
import com.abheet.finance.repository.TransactionRepo;
import com.abheet.finance.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepo repo;

    @Override
    public boolean saveTransaction(Transaction txn) {
        try {
            repo.save(txn);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public List<Transaction> getFilteredTransactions(User user, String type, String category, String fromDate, String toDate) {
        List<Transaction> transactions = repo.findByUser(user);

        if (type != null && !type.isEmpty()) {
            transactions.removeIf(t -> !t.getType().equalsIgnoreCase(type));
        }

        if (category != null && !category.isEmpty()) {
            transactions.removeIf(t -> !t.getCategory().equalsIgnoreCase(category));
        }

        if (fromDate != null && !fromDate.isEmpty()) {
            LocalDate from = LocalDate.parse(fromDate);
            transactions.removeIf(t -> t.getDate().isBefore(from));
        }

        if (toDate != null && !toDate.isEmpty()) {
            LocalDate to = LocalDate.parse(toDate);
            transactions.removeIf(t -> t.getDate().isAfter(to));
        }

        return transactions;
    }


    @Override
    public List<Transaction> getAllTransactionsByUser(User user) {
        return repo.findByUser(user);
    }
    
    @Override
    public boolean deleteTransactionByIdAndUser(int id, User user) {
        Optional<Transaction> txnOpt = repo.findByIdAndUser(id, user);
        if (txnOpt.isPresent()) {
            repo.delete(txnOpt.get());
            return true;
        }
        return false;
    }
    
    @Override
    public Optional<Transaction> getTransactionByIdAndUser(int id, User user) {
        return repo.findByIdAndUser(id, user);
    }
}
