package claudiaburali.capstoneproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@NoArgsConstructor
public class Wallet {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "currency_pair_id")
    private CurrencyPair currencyPair;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.EAGER/*, cascade = CascadeType.ALL, orphanRemoval = true*/)
    private List<Transaction> transactions = new ArrayList<>();

    public Wallet(String name) {
        this.name = name;
    }
}
