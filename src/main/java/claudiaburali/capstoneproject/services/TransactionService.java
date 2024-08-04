package claudiaburali.capstoneproject.services;

import claudiaburali.capstoneproject.entities.Transaction;
import claudiaburali.capstoneproject.entities.Wallet;
import claudiaburali.capstoneproject.exceptions.NotFoundException;
import claudiaburali.capstoneproject.payloads.NewTransactionDTO;
import claudiaburali.capstoneproject.repositories.TransactionRepository;
import claudiaburali.capstoneproject.repositories.WalletRepository;
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

    @Autowired
    private WalletRepository walletRepository;

    public List<Transaction> getAllTransactionsByWallet(UUID id) {
        Wallet wallet = walletService.findById(id);
        return wallet.getTransactions();
    }

    public Transaction findById(UUID id) {
        return transactionRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Transaction saveTransaction(NewTransactionDTO transactionDTO, UUID walletId) {
        Wallet wallet = walletService.findById(walletId);
        Transaction transaction = new Transaction(transactionDTO.volume(), transactionDTO.value(), transactionDTO.amount());
        if (transactionDTO.date() != null) {
            transaction.setDate(transactionDTO.date());
        }
        if (transactionDTO.exchange() != null) {
            transaction.setExchange(transactionDTO.exchange());
        }
        transaction.setWallet(wallet);
        transactionRepository.save(transaction);
        wallet.getTransactions().add(transaction);
        walletRepository.save(wallet);
        return transaction;
    }

    public Transaction findByIdAndUpdate(NewTransactionDTO transactionDTO, UUID transactionId) {
        Transaction transaction = findById(transactionId);
        transaction.setVolume(transactionDTO.volume());
        transaction.setValue(transactionDTO.value());
        transaction.setAmount(transactionDTO.amount());
        transaction.setDate(transactionDTO.date());
        transaction.setExchange(transactionDTO.exchange());
        transactionRepository.save(transaction);
        return transaction;
    }

    public void findByIdAndDelete(UUID walletId, UUID transactionId) {
        Wallet wallet = walletService.findById(walletId);
        Transaction transaction = findById(transactionId);
        wallet.getTransactions().removeIf(transaction1 -> transaction1.getId() == transactionId);
        transactionRepository.delete(transaction);
    }
}