package org.yearup.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yearup.models.Product;
import org.yearup.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {


    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    // Arrange

    Product item1 = new Product(1, "Game1", 19.99,
            1, "testing game 1", "testing 1",
            5, true, null);

    Product item2 = new Product(2, "Game2", 29.99,
            1, "testing game 2", "testing 1",
            5, true, null);

    Product item3 = new Product(3, "Game3", 39.99,
            1, "testing game 3", "testing 3",
            5, true, null);

    @Test
    void search_allItemsSearch_ReturnsEveryItem() {
        // Arrange
        List<Product> allItems = new ArrayList<>();

        allItems.add(item1);
        allItems.add(item2);
        allItems.add(item3);

        when(productRepository.findAll()).thenReturn(allItems);

        // Act
        List<Product> games = productService.search(null, null, null, null);

        // Assert
        assertEquals(3, allItems.size());
    }


    @Test
    void update_ProductStocks_ReturnSetStock() {
        // Arrange
        Product updateItem1 = new Product(1, "Game1", 19.99,
                1, "testing game 1", "testing 1",
                111, true, null);

        when(productRepository.findById(1)).thenReturn(Optional.of(item1));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Product newItem1 = productService.update(1, updateItem1);

        // Assert
        assertEquals(111, newItem1.getStock());
        verify(productRepository).save(item1);
    }
}