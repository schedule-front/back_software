package com.szydd.software.service.Implements;

import com.szydd.software.domain.Association;
import com.szydd.software.domain.Award;
import com.szydd.software.reporistory.AccountRepository;
import com.szydd.software.domain.Account;
import com.szydd.software.domain.Activity;
import com.szydd.software.service.Interface.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("AccountService")
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Account addAccount(Account account) throws Exception {

        if(accountRepository.findByAccountId(account.get_id()) == null) {
            return accountRepository.insert(account);
        } else {
            throw new Exception("用户以存在");
        }
    }



    @Override
    public String getID(String accountId) {
        return findAccountByAccountid(accountId).get_id();
    }

    @Override
    public void deleteAccount(String id) {
        accountRepository.deleteById(id);
    }

    @Override
    public void deleteUserAccountByAccountId(String accountId) {
        accountRepository.delete(accountRepository.findByAccountId(accountId));
    }

    @Override
    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account findAccountById(String id) {
        return accountRepository.findById(id).get();
    }

    @Override
    public Account findAccountByAccountid(String accountid) {
        return  accountRepository.findByAccountId(accountid);
    }

    @Override
    public List<Account> FindAllAccount() {
        return null;
    }

    @Override
    public Long findLargestAccountId() {
//        Criteria criteria = new Criteria().where("associationId").is(associationId);
        Aggregation agg = Aggregation.newAggregation(
//                Aggregation.match(criteria),
                Aggregation.sort(Sort.Direction.DESC,"activityId"),
                Aggregation.skip((long)0),
                Aggregation.limit(1)
        );
        AggregationResults<Account> acountAggregationResults = mongoTemplate.aggregate(agg
                ,"Acount"
                ,Account.class);
        List<Account> acounts = acountAggregationResults.getMappedResults();
        if (acounts.size() == 0) return (long)0;
        else return acounts.get(0).getAccountId();
    }
    @Override
    public List<Account> queryAllByAssociationId(String associationId, int page, int row) throws Exception {
        Criteria criteria = new Criteria().where("associationId").is(associationId);
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.sort(Sort.Direction.DESC,"accountId"),
                Aggregation.skip((long)row*(page - 1)),
                Aggregation.limit(row)
        );
        AggregationResults<Account> acountAggregationResults = mongoTemplate.aggregate(agg
                ,"Acount"
                ,Account.class);
        List<Account> acounts = acountAggregationResults.getMappedResults();
        return acounts;
//        PageRequest pr = PageRequest.of(page-1, row);
//        return accountRepository.queryAllByAssociationId(associationId,pr).getContent();
    }
}
