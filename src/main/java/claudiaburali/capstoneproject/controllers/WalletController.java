package claudiaburali.capstoneproject.controllers;

import claudiaburali.capstoneproject.entities.User;
import claudiaburali.capstoneproject.entities.Wallet;
import claudiaburali.capstoneproject.exceptions.BadRequestException;
import claudiaburali.capstoneproject.payloads.NewWalletDTO;
import claudiaburali.capstoneproject.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}

