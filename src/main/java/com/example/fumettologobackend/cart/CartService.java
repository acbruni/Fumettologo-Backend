package com.example.fumettologobackend.cart;

import com.example.fumettologobackend.cartDetail.CartDetail;
import com.example.fumettologobackend.cartDetail.CartDetailRepository;
import com.example.fumettologobackend.comic.Comic;
import com.example.fumettologobackend.comic.ComicRepository;
import com.example.fumettologobackend.comic.ComicService;
import com.example.fumettologobackend.order.Order;
import com.example.fumettologobackend.order.OrderRepository;
import com.example.fumettologobackend.orderDetail.OrderDetail;
import com.example.fumettologobackend.orderDetail.OrderDetailRepository;
import com.example.fumettologobackend.support.exceptions.*;
import com.example.fumettologobackend.user.User;
import com.example.fumettologobackend.user.UserService;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
public class CartService implements CartServiceInterface {
    private final ComicService comicService;
    private final CartDetailRepository cartDetailRepository;
    private final OrderRepository orderRepository;
    private final ComicRepository comicRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserService userService;

    @Autowired
    public CartService(ComicService comicService, CartDetailRepository cartDetailRepository,
                       OrderRepository orderRepository, ComicRepository comicRepository,
                       OrderDetailRepository orderDetailRepository, UserService userService) {
        this.comicService = comicService;
        this.cartDetailRepository = cartDetailRepository;
        this.orderRepository = orderRepository;
        this.comicRepository = comicRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Cart getCart(String email) throws UserNotFoundException {
        Cart cart = this.userService.findByEmail(email).getCart();
        for(CartDetail cd: cart.getCartDetails()) {
            Comic comic = cd.getComic();
            cd.setPrice(comic.getPrice());
            cd.setSubTotal(cd.getQuantity()*comic.getPrice());
        }
        System.out.println(cart);
        return cart;
    }

    @Override
    @Transactional
    public void addToCart(int comicId, String email) throws ComicNotFoundException, UserNotFoundException {
        Cart cart = this.userService.findByEmail(email).getCart();
        Comic comic = this.comicService.findById(comicId);
        CartDetail cd = this.cartDetailRepository.findBycomicIdAndCartId(comicId, cart.getId());
        if(cd == null) {
            cd = new CartDetail(cart, comic);
        }
        else {
            int newQuantity = cd.getQuantity()+1;
            cd.setQuantity(newQuantity);
            cd.setPrice(comic.getPrice());
            cd.setSubTotal(comic.getPrice()*newQuantity);
        }
        this.cartDetailRepository.save(cd);
    }

    @Override
    @Transactional
    public void updateQuantity(int cartDetailId, int quantity, String email) throws OutdatedCartException,
                                                                                    UserNotFoundException {
        CartDetail cd = this.cartDetailRepository.findById(cartDetailId);
        User user = this.userService.findByEmail(email);
        if(cd == null || cd.getCart().getUser().getId() != user.getId()) {
            throw new OutdatedCartException();
        }
        Comic comic = cd.getComic();
        cd.setQuantity(quantity);
        cd.setPrice(comic.getPrice());
        cd.setSubTotal(quantity*comic.getPrice());
        this.cartDetailRepository.save(cd);
    }

    @Override
    @Transactional
    public void deleteItem(int cartDetailId, String email) throws OutdatedCartException, UserNotFoundException {
        CartDetail cd = this.cartDetailRepository.findById(cartDetailId);
        User user = this.userService.findByEmail(email);
        if(cd == null || cd.getCart().getUser().getId() != user.getId()) {
            throw new OutdatedCartException();
        }
        cd.setCart(null);
        this.cartDetailRepository.delete(cd);
    }

    @Override
    @Transactional
    public void clear(String email) throws UserNotFoundException {
        Cart cart = this.userService.findByEmail(email).getCart();
        for(CartDetail cd: cart.getCartDetails()) {
            cd.setCart(null);
            this.cartDetailRepository.delete(cd);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order checkout(String email, LinkedList<CartDetail> cartDetails) throws OutdatedPriceException,
                                                                                    NegativeQuantityException,
                                                                                    OptimisticLockException,
                                                                                    EmptyCartException,
                                                                                    OutdatedCartException,
                                                                                    UserNotFoundException {
        User user = this.userService.findByEmail(email);
        List<CartDetail> dbCartDetails = user.getCart().getCartDetails();
        if(dbCartDetails.size() != cartDetails.size()) {
            throw new OutdatedCartException();
        }
        if(cartDetails.isEmpty()) {
            throw new EmptyCartException();
        }

        Order savedOrder = this.orderRepository.save(new Order(user));
        float total = 0;
        for(CartDetail cd: cartDetails) {
            CartDetail item = this.cartDetailRepository.findById(cd.getId());
            if(item == null || item.getCart().getUser().getId() != user.getId()) {
                throw new OutdatedCartException();
            }
            if(item.getQuantity() != cd.getQuantity()) {
                throw new OutdatedCartException();
            }

            Comic comic = item.getComic();
            float currentPrice = comic.getPrice();
            float priceInCart = item.getPrice();
            if(Math.abs(priceInCart-currentPrice) >= 0.01f) {
                throw new OutdatedPriceException();
            }

            int quantity = item.getQuantity();
            int newQuantity = comic.getQuantity()-quantity;
            if(newQuantity < 0) {
                throw new NegativeQuantityException();
            }
            comic.setQuantity(newQuantity);
            Comic updatedComic = this.comicRepository.save(comic);

            OrderDetail od = new OrderDetail(updatedComic,savedOrder,priceInCart,quantity);
            this.orderDetailRepository.save(od);
            item.setCart(null);
            this.cartDetailRepository.delete(item);

            total += priceInCart*quantity;
        }
        savedOrder.setTotal(total);
        return savedOrder;
    }

}
