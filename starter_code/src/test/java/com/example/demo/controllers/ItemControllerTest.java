package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void retrieve_items_with_getItems_Method() {
        List<Item> items = new ArrayList<>();
        items.add(getIceCream());
        items.add(getOttakringer());
        when(itemRepository.findAll()).thenReturn(items);
        ResponseEntity<List<Item>> allItemsResponse = itemController.getItems();
        assertNotNull(allItemsResponse);
        assertEquals(200, allItemsResponse.getStatusCodeValue());
        List<Item> foundItems = allItemsResponse.getBody();
        assertNotNull(foundItems);
        assertEquals(2, foundItems.size());
        assertEquals(getIceCream(), foundItems.get(0));
        assertEquals(getOttakringer(), foundItems.get(1));
    }

    @Test
    public void retrieve_item_with_getItemById_method() {
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(getIceCream()));
        ResponseEntity<Item> response = itemController.getItemById(0L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item foundItem = response.getBody();
        assertEquals(getIceCream(), foundItem);
        assertNotNull(foundItem);
        assertEquals(getIceCream().getName(), foundItem.getName());
        assertEquals(getIceCream().getId(), foundItem.getId());
        assertEquals(getIceCream().getDescription(), foundItem.getDescription());
    }

    @Test
    public void retrieve_item_with_getItemsByName_method() {
        List<Item> item = new ArrayList<>();
        item.add(getOttakringer());
        when(itemRepository.findByName("Ottakringer")).thenReturn(item);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Ottakringer");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> foundItem = response.getBody();
        assertNotNull(foundItem);
        assertEquals(1, foundItem.size());
        assertEquals(getOttakringer(), foundItem.get(0));
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
