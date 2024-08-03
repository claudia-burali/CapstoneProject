package claudiaburali.capstoneproject.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "currency_pairs")
@Getter
@Setter
@NoArgsConstructor
public class CurrencyPair {
    @Id
    @GeneratedValue
    private UUID id;
    private String name; //baseTicker + / + quoteTicker
    private String baseCurrency;
    private String quoteCurrency;
    private String baseTicker;
    private String quoteTicker;

    @OneToMany(mappedBy = "currencyPair", fetch = FetchType.EAGER)
    private List<Wallet> wallets = new ArrayList<>();

    public CurrencyPair(String name, String baseCurrency, String quoteCurrency, String baseTicker, String quoteTicker) {
        this.name = name;
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
        this.baseTicker = baseTicker;
        this.quoteTicker = quoteTicker;
    }
}
