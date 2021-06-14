package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private final UserRepo userRepo= mock(UserRepo.class);
    private final OrderRepository orderRepo = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        com.example.demo.TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
        com.example.demo.TestUtils.injectObjects(orderController, "userRepository", userRepo);

    }

    @Test
    public void submit_order_with_submit_method() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("password1234");
        user.setId(0L);
        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> items = new ArrayList<>();
        items.add(getIceCream());
        cart.setItems(items);
        cart.setTotal(new BigDecimal("2.22"));
        cart.setUser(user);
        user.setCart(cart);
        when(userRepo.findByUsername("user1")).thenReturn(user);

        ResponseEntity<UserOrder> orderResponse =  orderController.submit("user1");
        assertNotNull(orderResponse);
        assertEquals(200, orderResponse.getStatusCodeValue());

        UserOrder orderResponseBody = orderResponse.getBody();
        assertNotNull(orderResponseBody);
        assertNotNull(orderResponseBody.getUser());
        assertEquals(orderResponseBody.getUser(), user);
        assertNotNull(orderResponseBody.getItems());
        assertEquals(orderResponseBody.getItems(), items);
        assertNotNull(orderResponseBody.getTotal());

    }


    @Test
    public void get_orders_with_getOrdersForUser_method() {

        User user = new User();
        user.setUsername("user1");
        user.setPassword("password1234");
        user.setId(0L);
        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(getIceCream());
        cart.setItems(itemList);
        cart.setTotal(new BigDecimal("2.22"));
        cart.setUser(user);
        user.setCart(cart);
        when(userRepo.findByUsername("user1")).thenReturn(user);
        ResponseEntity<UserOrder> newOrder =  orderController.submit("user1");
        ResponseEntity<List<UserOrder>> orderResponse = orderController.getOrdersForUser("user1");
        assertNotNull(orderResponse);
        assertEquals(200, orderResponse.getStatusCodeValue());
        List<UserOrder> orderResponseBody = orderResponse.getBody();
        assertNotNull(orderResponseBody);
        assertEquals(0, orderResponseBody.size());
    }
    public static Item getIceCream() {
        Item item = new Item();
        item.setId(0L);
        item.setPrice(new BigDecimal("2.22"));
        item.setName("Coconut Icecream");
        item.setDescription("Delicious Coconut Icecream");
        return item;
    }


}
