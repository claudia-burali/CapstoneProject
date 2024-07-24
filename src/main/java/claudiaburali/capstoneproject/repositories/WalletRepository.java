package claudiaburali.capstoneproject.repositories;

import claudiaburali.capstoneproject.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
}
