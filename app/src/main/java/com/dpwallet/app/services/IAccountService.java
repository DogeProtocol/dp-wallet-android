package com.dpwallet.app.services;

import com.dpwallet.app.entity.Result;

public interface IAccountService {

    Result<Object> getBalanceByAccount(String address);

    Result<Object> listTxnByAccount(String address, int pageIndex);

    Result<Object> listPendingTxnByAccount(String address, int pageIndex);

    Result<Object> sendTransactionByAccount(String txData);

}
