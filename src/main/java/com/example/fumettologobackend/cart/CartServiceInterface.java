package com.example.fumettologobackend.cart;

import com.example.fumettologobackend.cartDetail.CartDetail;
import com.example.fumettologobackend.order.Order;
import com.example.fumettologobackend.support.exceptions.*;
import jakarta.persistence.OptimisticLockException;
import java.util.LinkedList;

public interface CartServiceInterface {
    Cart getCart(String email) throws UserNotFoundException;
    void addToCart(int comicId, String email) throws ComicNotFoundException, UserNotFoundException;
    void updateQuantity(int cartDetailId, int quantity, String email) throws OutdatedCartException,
                                                                             UserNotFoundException;
    void deleteItem(int cartDetailId, String email) throws OutdatedCartException, UserNotFoundException;
    void clear(String email) throws UserNotFoundException;
    Order checkout(String email, LinkedList<CartDetail> cartDetails) throws OutdatedPriceException,
            NegativeQuantityException,
            OptimisticLockException,
            EmptyCartException,
            OutdatedCartException, UserNotFoundException;
}