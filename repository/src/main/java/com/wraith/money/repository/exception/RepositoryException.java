package com.wraith.money.repository.exception;

/**
 * User: rowan.massey
 * Date: 17/09/2014
 * Time: 21:12
 */
public class RepositoryException extends RuntimeException {

    public RepositoryException(String errorMessage) {
        super(errorMessage);
    }

    public RepositoryException(Exception e) {
        super(e);
    }
}
