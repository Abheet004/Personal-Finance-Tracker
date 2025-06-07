package com.abheet.finance.controller;

import com.abheet.finance.entity.Transaction;
import com.abheet.finance.entity.User;
import com.abheet.finance.exporter.ExcelExporter;
import com.abheet.finance.exporter.PDFExporter;
import com.abheet.finance.service.TransactionService;
import com.abheet.finance.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    


    @GetMapping("/dashboard")
    public String openDashboard(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            Model model,
            HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/loginPage";

        List<Transaction> txns = transactionService.getFilteredTransactions(user, type, category, fromDate, toDate);

        double income = txns.stream().filter(t -> "income".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount).sum();
        double expense = txns.stream().filter(t -> "expense".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount).sum();

        model.addAttribute("income", income);
        model.addAttribute("expense", expense);
        model.addAttribute("balance", income - expense);
        model.addAttribute("txns", txns);

        model.addAttribute("type", type);
        model.addAttribute("category", category);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        return "dashboard";
    }
    
    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response, HttpSession session) throws IOException {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            response.sendRedirect("/loginPage");
            return;
        }

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=transactions.xlsx";
        response.setHeader(headerKey, headerValue);

        List<Transaction> txns = transactionService.getAllTransactionsByUser(user);
        ExcelExporter exporter = new ExcelExporter(txns);
        exporter.export(response);
    }

    @GetMapping("/export/pdf")
    public void exportToPDF(HttpServletResponse response, HttpSession session) throws IOException {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            response.sendRedirect("/loginPage");
            return;
        }

        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=transactions.pdf";
        response.setHeader(headerKey, headerValue);

        List<Transaction> txns = transactionService.getAllTransactionsByUser(user);
        PDFExporter exporter = new PDFExporter(txns);
        exporter.export(response);
    }


    // Show Add Transaction Form
    @GetMapping("/addTransaction")
    public String openAddTransactionPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/loginPage";
        }

        model.addAttribute("transaction", new Transaction());
        
        return "addTransaction";
    }

    // Submit New Transaction
    @PostMapping("/saveTransaction")
    public String saveTransaction(@ModelAttribute("transaction") Transaction transaction, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/loginPage";
        }

        transaction.setUser(user);
        transaction.setDate(LocalDate.now());
        transactionService.saveTransaction(transaction);
        return "redirect:/dashboard";
    }

    @GetMapping("/editTransaction/{id}")
    public String editTransaction(@PathVariable int id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/loginPage";
        }

        Optional<Transaction> txnOpt = transactionService.getTransactionByIdAndUser(id, user);
        if (txnOpt.isPresent()) {
            model.addAttribute("transaction", txnOpt.get());
            return "addTransaction";
        } else {
            return "redirect:/dashboard";
        }
    }

    @PostMapping("/updateTransaction")
    public String updateTransaction(@ModelAttribute("transaction") Transaction txn, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/loginPage";

        txn.setUser(user);
        transactionService.saveTransaction(txn);
        return "redirect:/dashboard";
    }

    @GetMapping("/deleteTransaction/{id}")
    public String deleteTransaction(@PathVariable int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user != null) {
            transactionService.deleteTransactionByIdAndUser(id, user);
        }
        return "redirect:/dashboard";
    }
}
