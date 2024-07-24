package claudiaburali.capstoneproject.services;

import claudiaburali.capstoneproject.entities.CurrencyPair;
import claudiaburali.capstoneproject.repositories.CurrencyPairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CurrencyPairService {
    @Autowired
    private CurrencyPairRepository currencyPairRepository;

    public CurrencyPair getOrCreateCurrencyPair(String baseCurrency, String quoteCurrency) {
        return currencyPairRepository.findByBaseCurrencyAndQuoteCurrency(baseCurrency, quoteCurrency)
                .orElseGet(() -> {
                    CurrencyPair newPair = new CurrencyPair();
                    newPair.setBaseCurrency(baseCurrency);
                    newPair.setQuoteCurrency(quoteCurrency);
                    return currencyPairRepository.save(newPair);
                });
    }

    public List<CurrencyPair> getAllCurrencyPairs() {
        return currencyPairRepository.findAll();
    }

    public CurrencyPair getCurrencyPairById(UUID id) {
        return currencyPairRepository.findById(id).orElseThrow(() -> new RuntimeException("Currency pair not found"));
    }

    public CurrencyPair getByName(String name) {
        return currencyPairRepository.findByName(name).orElseThrow(() -> new RuntimeException("Currency pair not found"));
    }
}
