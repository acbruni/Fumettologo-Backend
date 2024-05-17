package com.example.fumettologobackend.controllers;

import com.example.fumettologobackend.entities.CartDetail;
import com.example.fumettologobackend.entities.Cart;
import com.example.fumettologobackend.entities.Order;
import com.example.fumettologobackend.services.CartService;
import com.example.fumettologobackend.support.authentication.JwtUtils;
import com.example.fumettologobackend.support.exceptions.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedList;

@RestController // controller di tipo REST
@RequestMapping("/profile/cart") // imposta il percorso di base per tutte le richieste gestite da questo controller

// @CrossOrigin permette richieste CORS (consente o limita le richieste effettuate da una
// fonte diversa rispetto a quella del server a cui la richiesta è destinata)
// da un'origine specifica
@CrossOrigin(origins = "http://localhost:9090", allowedHeaders = "*")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<?> getCartDetails(Authentication authentication) {
        try {
            // recupera i dettagli del carrello utente autenticato
            String email = JwtUtils.getEmailFromAuthentication(authentication);
            Cart cart = this.cartService.getCart(email);
            // restituisce i dettagli del carrello con HTTP 200 (ok) se ha successo
            return new ResponseEntity<>(cart.getCartDetails(), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            // HTTP 404 (not found) se l'utente non viene trovato
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> addToCart(@RequestParam int comicId,
                                       Authentication authentication) {
        try {
            // aggiunge un fumetto al carrello dell'utente autenticato
            String email = JwtUtils.getEmailFromAuthentication(authentication);
            this.cartService.addToCart(comicId, email);
            // HTTP 200  se ha successo
            return new ResponseEntity<>("Comic added successfully", HttpStatus.OK);
        } catch (ComicNotFoundException e) {
            // HTTP 404 se il fumetto non viene trovato
            return new ResponseEntity<>("Comic not found", HttpStatus.NOT_FOUND);
        } catch (UserNotFoundException e) {
            // HTTP 404 se l'utente non viene trovato
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateQuantity(@PathVariable("itemId") int itemId,
                                            @RequestParam int quantity,
                                            Authentication authentication) {
        try {
            // aggiorna la quantità di un articolo nel carrello dell'utente autenticato
            String email = JwtUtils.getEmailFromAuthentication(authentication);
            this.cartService.updateQuantity(itemId, quantity, email);
            // HTTP 200 se ha successo
            return new ResponseEntity<>("Item updated successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            // HTTP 404 se l'utente non viene trovato
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (OutdatedCartException e) {
            // HTTP 409 se il carrello è obsoleto
            return new ResponseEntity<>("Cart not updated", HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable("itemId") int itemId, Authentication authentication) {
        try {
            // rimuove dal carrello un articolo
            String email = JwtUtils.getEmailFromAuthentication(authentication);
            this.cartService.deleteItem(itemId, email);
            // HTTP 200 se ha successo
            return new ResponseEntity<>("Item deleted successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            // HTTP 404 se l'utente non viene trovato
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (OutdatedCartException e) {
            // HTTP 409 se il carrello è obsoleto
            return new ResponseEntity<>("Cart not updated", HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> clear(Authentication authentication) {
        try {
            // svuolta il carrello dell'utente autenticato
            String email = JwtUtils.getEmailFromAuthentication(authentication);
            this.cartService.clear(email);
            // HTTP 200 se ha successo
            return new ResponseEntity<>("Cart emptied successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            // 404 se l'utente non viene trovato
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(Authentication authentication,
                                      @Valid @RequestBody LinkedList<CartDetail> cartDetails) {
        try {
            // effettua il checkout del carrello dell'utente autenticato
            String email = JwtUtils.getEmailFromAuthentication(authentication);
            Order order = this.cartService.checkout(email, cartDetails);
            // HTTP 201 se ha successo
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (OutdatedPriceException oe) {
            return new ResponseEntity<>("Product price not updated", HttpStatus.CONFLICT);
        } catch (NegativeQuantityException ne) {
            return new ResponseEntity<>("Unavailable quantity", HttpStatus.CONFLICT);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (EmptyCartException e) {
            return new ResponseEntity<>("Cart can't be empty", HttpStatus.BAD_REQUEST);
        } catch (OutdatedCartException e) {
            return new ResponseEntity<>("Cart not updated", HttpStatus.CONFLICT);
        }
    }

}
