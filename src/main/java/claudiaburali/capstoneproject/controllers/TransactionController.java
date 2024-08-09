package claudiaburali.capstoneproject.controllers;

import claudiaburali.capstoneproject.entities.Transaction;
import claudiaburali.capstoneproject.exceptions.BadRequestException;
import claudiaburali.capstoneproject.payloads.NewTransactionDTO;
import claudiaburali.capstoneproject.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/{walletId}")
    public ResponseEntity<Object> createTransaction(@RequestBody @Validated NewTransactionDTO newTransactionDTO, BindingResult bindingResult, @PathVariable UUID walletId) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        Transaction createTransaction = transactionService.saveTransaction(newTransactionDTO, walletId);
        try {
            String response = "Transazione aggiunta correttamente!";
            return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", response));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{walletId}/{transactionId}")
    public ResponseEntity<Object> DeleteTransactionById (@PathVariable UUID walletId, @PathVariable UUID transactionId) {
        transactionService.findByIdAndDelete(walletId, transactionId);
        try {
            String response = "Transazione eliminata correttamente!";
            return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", response));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Object> findByIdAndUpdate (@RequestBody @Validated NewTransactionDTO newTransactionDTO, BindingResult bindingResult, @PathVariable UUID transactionId) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        Transaction transaction = transactionService.findByIdAndUpdate(newTransactionDTO, transactionId);
        try {
            String response = "Transazione modificata correttamente!";
            return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", response));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}
