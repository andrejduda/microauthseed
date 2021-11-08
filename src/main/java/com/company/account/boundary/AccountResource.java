/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.account.boundary;

import com.company.account.control.AccountRepository;
import com.company.account.entity.Account;
import com.company.security.BCrypt;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;

/**
 *
 * @author ADUNET s.r.o.
 */
@Controller("acc")
public class AccountResource {

    private String adminPassword;
    private AccountRepository accountRepository;

    public AccountResource(@Value("${admin.pswd}") String adminPassword, AccountRepository accountRepository) {
        this.adminPassword = adminPassword;
        this.accountRepository = accountRepository;
    }

    @Post
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<Account> createAccount(@Body @Valid Account account, @QueryValue String pswd) {
        if (pswd.equalsIgnoreCase(adminPassword)) {
            account.setId(UUID.randomUUID());
            account.setPassword(hashPassword(account.getPassword()));
            return HttpResponse.ok(accountRepository.save(account));
        }
        return HttpResponse.unauthorized();
    }
    public static String hashPassword(String password) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);
    }

    @Get
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<List<Account>> getAccounts(@QueryValue String pswd) {
        if (pswd.equalsIgnoreCase(adminPassword)) {
            return HttpResponse.ok(accountRepository.findAll());
        }
        return HttpResponse.unauthorized();
        
    }
}
