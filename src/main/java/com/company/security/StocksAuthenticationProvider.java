/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.security;

import com.company.account.control.AccountRepository;
import com.company.account.entity.Account;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.reactivex.rxjava3.core.Maybe;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Map;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ADUNET s.r.o.
 */
@Singleton
public class StocksAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(StocksAuthenticationProvider.class);

    private final AccountRepository accountRepository;

    @Inject
    public StocksAuthenticationProvider(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest){
        return Maybe.<AuthenticationResponse>create(emitter -> {
        if (authenticationRequest.getIdentity() == null) {
            emitter.onSuccess(new AuthenticationFailed());
            return;
        }
        if (authenticationRequest.getSecret() == null) {
            emitter.onSuccess(new AuthenticationFailed());
            return;
        }

        Account account = accountRepository.findByLogin(authenticationRequest.getIdentity().toString()).orElse(null);
        if (account != null && BCrypt.checkpw(authenticationRequest.getSecret().toString(), account.getPassword())) {
            emitter.onSuccess(AuthenticationResponse.success(authenticationRequest.getIdentity().toString(), 
                    Map.of("acc", account.getId().toString(), "fullName", account.getFullName())));
            return;

        }

        emitter.onSuccess(new AuthenticationFailed());
        }).toFlowable();
    }

}
