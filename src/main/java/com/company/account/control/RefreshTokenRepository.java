/*
 * Copyright (c) 2018-2020 Protonchains s.r.o. https://protonchains.com
 * This file is subject to terms and conditions defined in file 'LICENSE.txt'
 * which is part of this source code package.
 * 
 */
package com.company.account.control;

import com.company.account.entity.RefreshToken;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;

/**
 *
 * @author ADUNET s.r.o.
 */
@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    @Override
    public List<RefreshToken> findAll();
    
}
