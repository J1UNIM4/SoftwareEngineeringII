package com.finance.project.domainLayer.entitiesInterfaces;

import com.finance.project.modules.ledger.domain.AccountID;
import com.finance.project.modules.ledger.domain.CategoryID;


public interface Owner {

    boolean addCategory(CategoryID categoryID);

    boolean addAccount(AccountID accountID);
}
