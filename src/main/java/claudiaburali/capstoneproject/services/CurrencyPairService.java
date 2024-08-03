package claudiaburali.capstoneproject.services;

import claudiaburali.capstoneproject.entities.CurrencyPair;
import claudiaburali.capstoneproject.exceptions.NotFoundException;
import claudiaburali.capstoneproject.payloads.NewCurrencyPairDTO;
import claudiaburali.capstoneproject.repositories.CurrencyPairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public CurrencyPair getCurrencyPairById(UUID id) {
        return currencyPairRepository.findById(id).orElseThrow(() -> new RuntimeException("Currency pair not found"));
    }

    public CurrencyPair getByName(String name) {
        return currencyPairRepository.findByName(name).orElseThrow(() -> new RuntimeException("Currency pair not found"));
    }

    public CurrencyPair createCurrencyPair(NewCurrencyPairDTO newCurrencyPairDTO) {
        Optional<CurrencyPair> existingPair = currencyPairRepository.findByName(newCurrencyPairDTO.name());
        if (existingPair.isPresent()) {
            throw new IllegalArgumentException("CurrencyPair già esistente!");
        }

        CurrencyPair currencyPair = new CurrencyPair(
                newCurrencyPairDTO.name(),
                newCurrencyPairDTO.baseCurrency(),
                newCurrencyPairDTO.quoteCurrency(),
                newCurrencyPairDTO.baseTicker(),
                newCurrencyPairDTO.quoteTicker()
        );
        return currencyPairRepository.save(currencyPair);
    }

    public List<CurrencyPair> getAllCurrencyPair () {
        return currencyPairRepository.findAll();
    }

    public void deleteCurrencyPair (UUID id) {
        if (!currencyPairRepository.existsById(id)) {
            throw new NotFoundException("Currency pair non esiste " + id);
        }
      currencyPairRepository.deleteById(id);
    }
}
