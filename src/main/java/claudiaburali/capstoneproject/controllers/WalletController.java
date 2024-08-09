package claudiaburali.capstoneproject.controllers;

import claudiaburali.capstoneproject.entities.User;
import claudiaburali.capstoneproject.entities.Wallet;
import claudiaburali.capstoneproject.exceptions.BadRequestException;
import claudiaburali.capstoneproject.payloads.NewWalletDTO;
import claudiaburali.capstoneproject.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/wallets")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @GetMapping
    public List<Wallet> getAllWalletsByUser(@AuthenticationPrincipal User user) {
        return walletService.getAllWalletsByUser(user.getId());
    }

    @PostMapping
    public Wallet saveWallet(@RequestBody @Validated NewWalletDTO wallet, @AuthenticationPrincipal User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return walletService.saveWallet(wallet, user.getId());
    }

    @PatchMapping("/{walletId}")
    public Wallet updateWallet(@RequestBody @Validated NewWalletDTO wallet, @AuthenticationPrincipal User user, @PathVariable UUID walletId, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return walletService.findByIdAndUpdate(walletId, wallet);
    }

    @DeleteMapping("/{walletId}")
    public ResponseEntity<Object> deleteWallet(@PathVariable UUID walletId) {
        walletService.findByIdAndDelete(walletId);
        try {
            String response = "Wallet eliminato correttamente!";
            return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", response));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}

