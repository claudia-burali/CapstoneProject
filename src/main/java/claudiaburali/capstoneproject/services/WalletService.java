package claudiaburali.capstoneproject.services;

import claudiaburali.capstoneproject.entities.CurrencyPair;
import claudiaburali.capstoneproject.entities.Transaction;
import claudiaburali.capstoneproject.entities.User;
import claudiaburali.capstoneproject.entities.Wallet;
import claudiaburali.capstoneproject.exceptions.NotFoundException;
import claudiaburali.capstoneproject.payloads.NewWalletDTO;
import claudiaburali.capstoneproject.repositories.CurrencyPairRepository;
import claudiaburali.capstoneproject.repositories.TransactionRepository;
import claudiaburali.capstoneproject.repositories.UsersRepository;
import claudiaburali.capstoneproject.repositories.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private CurrencyPairService currencyPairService;

    @Autowired
    private CurrencyPairRepository currencyPairRepository;

    @Autowired
    private UsersService userService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Wallet> getAllWalletsByUser(UUID id) {
        User user = userService.findById(id);
        return user.getWallets();
    }

    public Wallet findById(UUID walletId) {
        return this.walletRepository.findById(walletId).orElseThrow(() -> new NotFoundException(walletId));
    }

    public Wallet findByIdAndUpdate(UUID walletId, NewWalletDTO modifiedWallet) {
        Wallet found = this.findById(walletId);
        found.setName(modifiedWallet.name());
        CurrencyPair currencyPair = currencyPairService.getByName(modifiedWallet.currencyPairName());
        found.setCurrencyPair(currencyPair);
        return this.walletRepository.save(found);
    }

    public void findByIdAndDelete(UUID walletId) {
        Wallet found = this.findById(walletId);
        for (Transaction transaction: found.getTransactions()) {
            transactionRepository.delete(transaction);
        }
        this.walletRepository.delete(found);
    }

    public Wallet saveWallet(NewWalletDTO wallet, UUID id) {
        User user = userService.findById(id);
        Wallet wallet1 = new Wallet(wallet.name());
        CurrencyPair currencyPair = currencyPairService.getByName(wallet.currencyPairName());
        wallet1.setUser(user);
        wallet1.setCurrencyPair(currencyPair);
        walletRepository.save(wallet1);
        user.getWallets().add(wallet1);
        usersRepository.save(user);
        currencyPair.getWallets().add(wallet1);
        currencyPairRepository.save(currencyPair);
        return wallet1;
    }
}