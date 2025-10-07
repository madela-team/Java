package ru.madela.ignite3computedemo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.madela.RapidRepeatJobResponse;
import ru.madela.ignite3computedemo.model.TransactionModel;
import ru.madela.ignite3computedemo.service.IgniteComputeService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class IgniteComputeController {

    private final IgniteComputeService igniteComputeService;

    @GetMapping("/{number}")
    public boolean primeCheck(@PathVariable Integer number) {
        return igniteComputeService.primeCheck(number);
    }

    @GetMapping("find/{accountId}")
    public List<TransactionModel> findByAccountId(@PathVariable UUID accountId) {
        return igniteComputeService.findByAccountId(accountId);
    }

    @GetMapping("/rapid-repeat")
    public List<RapidRepeatJobResponse> scanAllAccountsRapidRepeats() {
        return igniteComputeService.findAllAccountsRapidRepeats();
    }

    @GetMapping("/rapid-repeat/{accountId}")
    public RapidRepeatJobResponse findRapidRepeats(@PathVariable UUID accountId) {
        return igniteComputeService.findRapidRepeats(accountId);
    }

}
