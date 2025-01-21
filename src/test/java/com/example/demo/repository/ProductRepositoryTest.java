package com.example.demo.repository;

import com.example.demo.product.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

@DataJpaTest
@TestPropertySource(locations = "/application-test.properties")
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp() {
        Product product = new Product();
        product.setName("test");
        product.setPrice(1000);
        product.setQuantity(10);
        entityManager.persistAndFlush(product);
    }

    @Test
    public void test_findProductByName() {
        Optional<Product> product = productRepository.findProductByName("test");

        Assertions.assertTrue(product.isPresent(), "Product not found");

        Product product1 = product.get();

        Assertions.assertEquals("test", product1.getName());
    }
}
