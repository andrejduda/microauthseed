/*
 * Copyright (c) 2019-2021 ADUNET s.r.o. https://adunet.eu
 * This file is subject to terms and conditions defined in file 'LICENSE.txt'
 * which is part of this source code package.
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential. 
 * 
 */
package com.company.account.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author ADUNET s.r.o.
 */
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @Column(name = "token")
    public String token;
    @Column(name = "login", nullable = false, length = 255)
    public String login;
    @Column(name = "expire_at")
    public Long expireAt;
    

    public RefreshToken() {
    }

    public RefreshToken(String token, String login, Long expireAt) {
        this.token = token;
        this.login = login;
        this.expireAt = expireAt;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the expireAt
     */
    public Long getExpireAt() {
        return expireAt;
    }

    /**
     * @param expireAt the expireAt to set
     */
    public void setExpireAt(Long expireAt) {
        this.expireAt = expireAt;
    }



}
