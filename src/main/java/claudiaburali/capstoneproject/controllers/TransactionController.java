package claudiaburali.capstoneproject.controllers;

import claudiaburali.capstoneproject.entities.Transaction;
import claudiaburali.capstoneproject.exceptions.BadRequestException;
import claudiaburali.capstoneproject.payloads.NewTransactionDTO;
import claudiaburali.capstoneproject.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/{walletId}")
    public String createTransaction(@RequestBody @Validated NewTransactionDTO newTransactionDTO, BindingResult bindingResult, @PathVariable UUID walletId) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        Transaction createTransaction = transactionService.saveTransaction(newTransactionDTO, walletId);
        return "Transazione aggiunta correttamente!";
    }

    @DeleteMapping("/{walletId}/{transactionId}")
    public void DeleteTransactionById (@PathVariable UUID walletId, @PathVariable UUID transactionId) {
        transactionService.findByIdAndDelete(walletId, transactionId);
    }

    @PutMapping("/{transactionId}")
    public String findByIdAndUpdate (@RequestBody @Validated NewTransactionDTO newTransactionDTO, BindingResult bindingResult, @PathVariable UUID transactionId) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        Transaction transaction = transactionService.findByIdAndUpdate(newTransactionDTO, transactionId);
        return "Transazione modificata correttamente!";
    }
}
