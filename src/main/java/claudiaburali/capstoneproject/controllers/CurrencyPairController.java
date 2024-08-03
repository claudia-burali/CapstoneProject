package claudiaburali.capstoneproject.controllers;

import claudiaburali.capstoneproject.entities.CurrencyPair;
import claudiaburali.capstoneproject.exceptions.BadRequestException;
import claudiaburali.capstoneproject.payloads.NewCurrencyPairDTO;
import claudiaburali.capstoneproject.services.CurrencyPairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/currencyPairs")
public class CurrencyPairController {

    @Autowired
    private CurrencyPairService currencyPairService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createCurrencyPair(@RequestBody NewCurrencyPairDTO newCurrencyPairDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        CurrencyPair createdCurrencyPair = currencyPairService.createCurrencyPair(newCurrencyPairDTO);
        return "Coppia aggiunta correttamente!";
    }

    @GetMapping
    public List<CurrencyPair> listaDiTuttiCurrencyPair () {
        return currencyPairService.getAllCurrencyPair();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void DeleteCurrencyById (@PathVariable  UUID id) {
        currencyPairService.deleteCurrencyPair(id);
    }
}
