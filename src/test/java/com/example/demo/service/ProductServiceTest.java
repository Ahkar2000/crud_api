package com.example.demo.service;

import com.example.demo.product.Product;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("test");
        product.setPrice(1000);
        product.setQuantity(10);
    }

    @Test
    public void test_getAllProducts() {
        Mockito.when(productRepository.findAll()).thenReturn(Arrays.asList(product));
        List<Product> results = productService.getProducts();
        Assertions.assertNotNull(results);
        Assertions.assertFalse(results.isEmpty(), "There should be at least one product");
    }

    @Test
    public void test_createProduct(){
        Mockito.when(productRepository.findProductByName(product.getName())).thenReturn(Optional.empty());

        Mockito.when(productRepository.save(product)).thenReturn(product);

        Product createdProduct = productService.createProduct(product);

        Assertions.assertNotNull(createdProduct, "product is null");
        Assertions.assertNotNull(createdProduct.getId(), "product id is null");
    }

    @Test
    public void test_createProduct_throwsException(){
        Mockito.when(productRepository.findProductByName(product.getName())).thenReturn(Optional.of(product));
        IllegalStateException ex = Assertions.assertThrows(IllegalStateException.class, () -> productService.createProduct(product));
        Assertions.assertNotNull(ex.getMessage(), "Product already exists!");
    }

    @Test
    public void test_updateProduct(){
        product.setName("update test");

        Mockito.when(productRepository.existsById(product.getId())).thenReturn(true);

        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        Mockito.when(productRepository.save(product)).thenReturn(product);

        Product createdProduct = productService.updateProduct(product.getId(), product);

        Assertions.assertNotNull(createdProduct, "product is null");
        Assertions.assertNotNull(createdProduct.getId(), "product id is null");
        Assertions.assertEquals(createdProduct.getId(), product.getId(), "product id is different");
        Assertions.assertEquals(createdProduct.getName(), product.getName(), "product name is different");
    }

    @Test
    public void test_updateProduct_throwsException(){
        product.setName("update test");

        Mockito.when(productRepository.existsById(product.getId())).thenReturn(false);

        IllegalStateException ex = Assertions.assertThrows(IllegalStateException.class ,() -> productService.updateProduct(product.getId(), product));

        Assertions.assertEquals("Product does not exist!", ex.getMessage(), "error message is different");
    }
}
