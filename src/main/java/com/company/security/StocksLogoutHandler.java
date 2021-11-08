/*
 * Copyright (c) 2019-2021 ADUNET s.r.o. https://adunet.eu
 * This file is subject to terms and conditions defined in file 'LICENSE.txt'
 * which is part of this source code package.
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential. 
 * 
 */
package com.company.security;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.security.handlers.LogoutHandler;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADUNET s.r.o.
 */
@Singleton
public class StocksLogoutHandler implements LogoutHandler {

    @Inject
    private StocksRefreshTokenPersistence refreshService;
    private static final String PARAM_KEY_REFRESH_TOKEN = "refresh_token";

    @Override
    public MutableHttpResponse<?> logout(HttpRequest<?> request) {
        Map<String, List<String>> paramMap = request.getParameters().asMap();
        if (paramMap.containsKey(PARAM_KEY_REFRESH_TOKEN) && paramMap.get(PARAM_KEY_REFRESH_TOKEN) != null && !paramMap.get(
                PARAM_KEY_REFRESH_TOKEN).isEmpty()) {
            String fullToken = paramMap.get(PARAM_KEY_REFRESH_TOKEN).get(0);
            String token = parseToken(fullToken);
            refreshService.removeToken(token);
        }
        return HttpResponse.accepted();
    }

    String parseToken(String fullToken) {
        return new String(Base64.getDecoder().decode(fullToken.split("\\.")[1]));
    }

}
