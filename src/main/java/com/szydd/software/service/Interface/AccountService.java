package com.szydd.software.service.Interface;

import com.szydd.software.domain.Account;
import com.szydd.software.domain.Activity;
import com.szydd.software.domain.User;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Set;

public interface AccountService {
    Account addAccount(Account account) throws Exception;
    Long findLargestAccountId();
    String getID(String accountId);
    void deleteAccount(String id);
    void deleteUserAccountByAccountId(String accountId);

    Account updateAccount(Account account);

    Account findAccountById(String id);
    Account findAccountByAccountid(String accountid);

    List<Account> FindAllAccount();
    List<Account> queryAllByAssociationId (String associationId, int page, int row) throws Exception;

}
