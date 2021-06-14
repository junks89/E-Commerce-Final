package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepo;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private final CartRepository cartRepo = mock(CartRepository.class);
    private final UserRepo userRepo = mock(UserRepo.class);



    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void add_item_to_Cart_addTocart_method() {
        User user = new User();
        user.setUsername("user1");
        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> items = new ArrayList<>();
        items.add(getIceCream());
        cart.setItems(items);
        cart.setTotal(new BigDecimal("2.22"));
        cart.setUser(user);
        user.setCart(cart);
        when(userRepo.findByUsername("user1")).thenReturn(user);
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(getIceCream()));
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("user1");
        ResponseEntity<Cart> addedCartResponse = cartController.addTocart(request);

        assertNotNull(addedCartResponse);
        assertEquals(200, addedCartResponse.getStatusCodeValue());
        Cart addedCart = addedCartResponse.getBody();
        assertNotNull(addedCart);
        List<Item> cartItems = addedCart.getItems();
        assertNotNull(cartItems);
        Item retrievedItem = cartItems.get(0);
        assertEquals(2, cartItems.size());
        assertEquals(user, addedCart.getUser());
        assertNotNull(retrievedItem);
        assertEquals(new BigDecimal("4.44"), addedCart.getTotal());
        assertEquals(getIceCream(), retrievedItem);


    }

    @Test
    public void remove_item_from_cart_removeFromcart_method() {
        User user = new User();
        user.setUsername("user1");
        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> items = new ArrayList<>();
        items.add(getIceCream());
        cart.setItems(items);
        cart.setTotal(new BigDecimal("2.22"));
        cart.setUser(user);
        user.setCart(cart);
        when(userRepo.findByUsername("user1")).thenReturn(user);
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(getIceCream()));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("user1");
        request.setQuantity(1);
        request.setItemId(0L);
        ResponseEntity<Cart> responseCartRemove = cartController.removeFromcart(request);
        assertNotNull(responseCartRemove);
        assertEquals(200, responseCartRemove.getStatusCodeValue());
        Cart removedCart = responseCartRemove.getBody();
        assertNotNull(removedCart);
        List<Item> cartItems = removedCart.getItems();
        assertEquals(new BigDecimal("0.00"), removedCart.getTotal());
        assertEquals(user, removedCart.getUser());
        assertNotNull(cartItems);
        assertEquals(0, cartItems.size());


    }
    public static Item getIceCream() {
        Item item = new Item();
        item.setId(0L);
        item.setPrice(new BigDecimal("2.22"));
        item.setName("Coconut Icecream");
        item.setDescription("Delicious Coconut Icecream");
        return item;
    }

    public static Item getOttakringer() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Ottakringer");
        item.setPrice(new BigDecimal("0.99"));
        item.setDescription("tasty beer");
        return item;
    }

}
