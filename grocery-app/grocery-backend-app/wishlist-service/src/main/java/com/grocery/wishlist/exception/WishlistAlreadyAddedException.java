package com.grocery.wishlist.exception;

public class WishlistAlreadyAddedException extends RuntimeException {
    public WishlistAlreadyAddedException(String message) {
        super(message);
    }
}
