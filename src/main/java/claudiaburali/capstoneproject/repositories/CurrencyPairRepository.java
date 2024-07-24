package claudiaburali.capstoneproject.repositories;

import claudiaburali.capstoneproject.entities.CurrencyPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CurrencyPairRepository extends JpaRepository<CurrencyPair, UUID> {
    Optional<CurrencyPair> findByBaseCurrencyAndQuoteCurrency(String baseCurrency, String quoteCurrency);
    Optional<CurrencyPair> findByName(String name);
}
