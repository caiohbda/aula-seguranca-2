package com.fintech.controller;

import com.fintech.dto.AccountRequest;
import com.fintech.dto.AccountResponse;
import com.fintech.dto.TransactionResponse;
import com.fintech.dto.TransferRequest;
import com.fintech.service.AccountService;
import com.fintech.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class WebController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public WebController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public String index(Authentication authentication) {
        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }

    // ── Login ──────────────────────────────────────────────────────────────────

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // ── Register ───────────────────────────────────────────────────────────────

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("accountRequest", new AccountRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("accountRequest") AccountRequest request,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            accountService.createAccount(request);
            redirectAttributes.addFlashAttribute("successMessage", "Conta criada com sucesso! Faça login.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("cpf", "cpf.exists", e.getMessage());
            return "register";
        }
    }

    // ── Dashboard ──────────────────────────────────────────────────────────────

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String cpf = authentication.getName();
        AccountResponse account = accountService.getAccountByCpf(cpf);
        model.addAttribute("account", account);
        return "dashboard";
    }

    // ── Transfer ───────────────────────────────────────────────────────────────

    @GetMapping("/transfer")
    public String transferPage(Model model) {
        model.addAttribute("transferRequest", new TransferRequest());
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer(@Valid @ModelAttribute("transferRequest") TransferRequest request,
                           BindingResult bindingResult,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        String cpf = authentication.getName();
        request.setFromCpf(cpf);

        if (bindingResult.hasFieldErrors("toCpf") || bindingResult.hasFieldErrors("amount")) {
            return "transfer";
        }

        try {
            transactionService.transfer(request, cpf);
            redirectAttributes.addFlashAttribute("successMessage", "Transferência realizada com sucesso!");
            return "redirect:/dashboard";
        } catch (IllegalArgumentException | SecurityException e) {
            bindingResult.reject("transfer.error", e.getMessage());
            return "transfer";
        }
    }

    // ── Transactions ───────────────────────────────────────────────────────────

    @GetMapping("/transactions")
    public String transactions(Authentication authentication, Model model) {
        String cpf = authentication.getName();
        List<TransactionResponse> history = transactionService.getHistory(cpf, cpf);
        model.addAttribute("transactions", history);
        model.addAttribute("myCpf", cpf);
        return "transactions";
    }
}
