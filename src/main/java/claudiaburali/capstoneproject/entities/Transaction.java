package claudiaburali.capstoneproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue
    private UUID id;
    private double volume;//quantità in EUR
    private double value; //prezzo di acquisto
    private double amount;//quantità in BTC
    private LocalDate date;
    private String exchange;
    private String address;

    @JsonIgnore
    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    public Transaction(double volume, double value, double amount, LocalDate date, String exchange, String address) {
        this.volume = volume;
        this.value = value;
        this.amount = amount;
        this.date = date;
        this.exchange = exchange;
        this.address = address;
    }
}

