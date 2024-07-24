package claudiaburali.capstoneproject.services;

import claudiaburali.capstoneproject.entities.Transaction;
import claudiaburali.capstoneproject.entities.Wallet;
import claudiaburali.capstoneproject.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WalletService walletService;

    public List<Transaction> getAllTransactionsByWallet(UUID id) {
        Wallet wallet = walletService.findById(id);
        return wallet.getTransactions();
    }

    /*public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }*/
}