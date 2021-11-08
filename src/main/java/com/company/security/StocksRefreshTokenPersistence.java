/*
 * Copyright (c) 2019-2021 ADUNET s.r.o. https://adunet.eu
 * This file is subject to terms and conditions defined in file 'LICENSE.txt'
 * which is part of this source code package.
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential. 
 * 
 */
package com.company.security;

import com.company.account.control.AccountRepository;
import com.company.account.control.RefreshTokenRepository;
import com.company.account.entity.Account;
import com.company.account.entity.RefreshToken;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.errors.IssuingAnAccessTokenErrorCode;
import io.micronaut.security.errors.OauthErrorResponseException;
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent;
import io.micronaut.security.token.refresh.RefreshTokenPersistence;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;

/**
 *
 * @author ADUNET s.r.o.
 */
@Singleton
public class StocksRefreshTokenPersistence implements RefreshTokenPersistence {

    @Inject
    private RefreshTokenRepository refreshTokenRepository;

    @Inject
    private AccountRepository accountRepository;


    @Override
    public void persistToken(RefreshTokenGeneratedEvent event) {
        if (event != null
                && event.getRefreshToken() != null
                && event.getAuthentication() != null) {
            String token = event.getRefreshToken();
            Authentication user = event.getAuthentication();
            Long expireAt = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1);
            RefreshToken rt = new RefreshToken(token, user.getName(), expireAt);
            refreshTokenRepository.save(rt);
        }
    }



    @Override
    public Publisher<Authentication> getAuthentication(String refreshToken){
//    public Publisher<UserDetails> getUserDetails(String refreshToken) {
        return Flowable.create(emitter -> {
            RefreshToken rt = refreshTokenRepository.findById(refreshToken).orElse(null);
            long now = System.currentTimeMillis();
            if (rt != null && rt.expireAt != null && rt.expireAt >= now) {
                rt.setExpireAt(now + TimeUnit.HOURS.toMillis(1));
                refreshTokenRepository.update(rt);
                try {
                    Account account = accountRepository.findByLogin(rt.login).orElse(null);
                    if (account != null) {
                        emitter.onNext(Authentication.build(account.getLogin(),
                        Map.of("acc", account.getId().toString(), "fullName", account.getFullName())));
                        emitter.onComplete();
                    }
                } catch (Exception e) {
                    emitter.onError(new OauthErrorResponseException(IssuingAnAccessTokenErrorCode.INVALID_GRANT,
                            "refresh token not available", null));
                }
            } else {
                emitter.onError(new OauthErrorResponseException(IssuingAnAccessTokenErrorCode.INVALID_GRANT,
                        "refresh token revoked", null));
            }
        }, BackpressureStrategy.ERROR);
    }

    public void removeToken(String token) {
        RefreshToken rt = refreshTokenRepository.findById(token).orElse(null);
        if (rt != null){
            refreshTokenRepository.delete(rt);
        }
    }

}
