package com.example.fumettologobackend.services;

import com.example.fumettologobackend.entities.CartDetail;
import com.example.fumettologobackend.repositories.CartDetailRepository;
import com.example.fumettologobackend.entities.Comic;
import com.example.fumettologobackend.repositories.ComicRepository;
import com.example.fumettologobackend.entities.Cart;
import com.example.fumettologobackend.entities.Order;
import com.example.fumettologobackend.repositories.OrderRepository;
import com.example.fumettologobackend.entities.OrderDetail;
import com.example.fumettologobackend.repositories.OrderDetailRepository;
import com.example.fumettologobackend.support.exceptions.*;
import com.example.fumettologobackend.entities.User;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
public class CartService {
    private final ComicService comicService;
    private final CartDetailRepository cartDetailRepository;
    private final OrderRepository orderRepository;
    private final ComicRepository comicRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserService userService;

    @Autowired // inietta automaticamente le dipendenze richieste
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

    // indica che il metodo dovrebbe essere eseguito all'interno di una transazione
    // quando viene sollevata un'eccezione la transazione viene annullata (rollback)
    // grantendo così che nessuna operazione paraziale venga commessa nel db
    @Transactional
    public Cart getCart(String email) throws UserNotFoundException {
        // prende il carrello associato all'utente (lo trova tramite mail)
        Cart cart = this.userService.findByEmail(email).getCart();

        // per ogni dettaglio del carrello
        for(CartDetail cd: cart.getCartDetails()) {
            Comic comic = cd.getComic(); // recupera il fumetto
            cd.setPrice(comic.getPrice()); // imposta il prezzo
            cd.setSubTotal(cd.getQuantity()*comic.getPrice()); // calcola e imposta il subtotale
        }

        return cart;
    }

    @Transactional
    public void addToCart(int comicId, String email) throws ComicNotFoundException, UserNotFoundException {
        Cart cart = this.userService.findByEmail(email).getCart(); // prende il carrello asssociato all'utente
        Comic comic = this.comicService.findById(comicId); // trova il fumetto tramite l'id
        CartDetail cd = this.cartDetailRepository.findByComicIdAndCartId(comicId, cart.getId()); // vede se esiste un dettaglio del carrello per quel fumetto nel carrello dell'utente e in caso lo prende

        if(cd == null) {
            // se è nullo crea un nuovo CartDetail per il fumetto specificato e lo aggiunge al carrello
            cd = new CartDetail(cart, comic);
        }
        else {
            // altrimenti incrementa la quantità del fumetto esistente nel carrello, aggiorna il prezzo
            // e ricalcola il subtotale
            int newQuantity = cd.getQuantity()+1;
            cd.setQuantity(newQuantity);
            cd.setPrice(comic.getPrice());
            cd.setSubTotal(comic.getPrice()*newQuantity);
        }

        // salva il dettaglio del carrello nel repository
        this.cartDetailRepository.save(cd);
    }

    @Transactional
    public void updateQuantity(int cartDetailId, int quantity, String email) throws OutdatedCartException,
                                                                                    UserNotFoundException {
        CartDetail cd = this.cartDetailRepository.findById(cartDetailId); // cerca il CartDetail tramite il suo id nella repository
        User user = this.userService.findByEmail(email); // preleva l'utente tramite la mail se presente

        // controlla se il CartDetail esiste e se appartiene all'utente specificato
        if(cd == null || cd.getCart().getUser().getId() != user.getId()) {
            throw new OutdatedCartException();
        }

        Comic comic = cd.getComic(); // recupera il fumetto
        cd.setQuantity(quantity); // imposta la nuova quantità del fumetto nel CartDetail
        cd.setPrice(comic.getPrice()); // aggiorna il prezzo del fumetto
        cd.setSubTotal(quantity*comic.getPrice()); // calcola e setta il subtotale
        this.cartDetailRepository.save(cd); // salva nella repository
    }

    @Transactional
    public void deleteItem(int cartDetailId, String email) throws OutdatedCartException, UserNotFoundException {
        CartDetail cd = this.cartDetailRepository.findById(cartDetailId); // recupera il dettaglio del carrello
        User user = this.userService.findByEmail(email); // recupera lo user tramite l'email

        // se non esiste il CartDetail o l'utente non è quello richiesto, lancia l'eccezione
        if(cd == null || cd.getCart().getUser().getId() != user.getId()) {
            throw new OutdatedCartException();
        }

        cd.setCart(null); // imposta il CartDetail a null
        this.cartDetailRepository.delete(cd); // elimina il dettaglio del carrello dal repository
    }

    @Transactional
    public void clear(String email) throws UserNotFoundException {
        Cart cart = this.userService.findByEmail(email).getCart(); // recupera il carrello associato all'utente

        for(CartDetail cd: cart.getCartDetails()) {
            cd.setCart(null); // imposta il carrello del dettaglio a null
            this.cartDetailRepository.delete(cd); // elimina il CartDetail dal repository
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Order checkout(String email, LinkedList<CartDetail> cartDetails) throws OutdatedPriceException,
                                                                                    NegativeQuantityException,
                                                                                    OptimisticLockException,
                                                                                    EmptyCartException,
                                                                                    OutdatedCartException,
                                                                                    UserNotFoundException {
        User user = this.userService.findByEmail(email); // recupera l'utente tramite l'email
        List<CartDetail> dbCartDetails = user.getCart().getCartDetails(); // recupera i dettagli del carrello dell'utente dal db

        // verifica se il numero di CartDetails nella richiesta è diverso da quello nel db
        if(dbCartDetails.size() != cartDetails.size()) {
            throw new OutdatedCartException();
        }
        // verifica se il carrello è vuoto
        if(cartDetails.isEmpty()) {
            throw new EmptyCartException();
        }

        Order savedOrder = this.orderRepository.save(new Order(user)); // crea e salva un nuovo ordine per l'utente
        float total = 0;

        // per ogni CartDetails
        for(CartDetail cd: cartDetails) {
            // recupera il CartDetail dal db
            CartDetail item = this.cartDetailRepository.findById(cd.getId());

            // verifica se il CartDetail esiste e appartiene all'utente specificato
            if(item == null || item.getCart().getUser().getId() != user.getId()) {
                throw new OutdatedCartException();
            }
            // verifica se la quantità del CartDetails ha subito variazioni
            if(item.getQuantity() != cd.getQuantity()) {
                throw new OutdatedCartException();
            }

            Comic comic = item.getComic(); // recupera il fumetto associato al cd

            // verifica se il prezzo nel carrello è diverso dal prezzo corrente
            float currentPrice = comic.getPrice();
            float priceInCart = item.getPrice();
            if(Math.abs(priceInCart-currentPrice) >= 0.01f) {
                throw new OutdatedPriceException();
            }

            // calcola la nuova quantità disponibile del fumetto
            int quantity = item.getQuantity();
            int newQuantity = comic.getQuantity()-quantity;
            if(newQuantity < 0) {
                throw new NegativeQuantityException();
            }

            // aggiorna la quantità del fumetto e salva le modifiche nel db
            comic.setQuantity(newQuantity);
            Comic updatedComic = this.comicRepository.save(comic);

            // crea e salva gli OrderDetail nel db
            OrderDetail od = new OrderDetail(updatedComic,savedOrder,priceInCart,quantity);
            this.orderDetailRepository.save(od);

            // imposta il CartDetail del carrello a null e lo elimina dal db
            item.setCart(null);
            this.cartDetailRepository.delete(item);

            // aggiorna il totla edell'ordine
            total += priceInCart*quantity;
        }
        savedOrder.setTotal(total); // imposta il ttoale dell'ordine
        return savedOrder;
    }

}
