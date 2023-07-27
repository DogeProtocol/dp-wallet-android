package com.dpwallet.app.entity;

public class Result<T> {
    private final T _result;
    private final T _exception;

    public Result(T result, T exception){
        this._result = result;
        this._exception = exception;
    }


    public void setData(T result, T exception){
        //_result = result;
        //_exception = exception;
    }

    public T getResult() { return _result; }

    public T getException() { return _exception; }
}
