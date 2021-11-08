/*
 * Copyright (c) 2018-2020 Protonchains s.r.o. https://protonchains.com
 * This file is subject to terms and conditions defined in file 'LICENSE.txt'
 * which is part of this source code package.
 * 
 */
package com.company.account.control;

import com.company.account.entity.Account;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author ADUNET s.r.o.
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, UUID> {

    @Override
    public List<Account> findAll();
    
    public Long countByLogin(String login);
    public Optional<Account> findByLogin(String login);
    
}
