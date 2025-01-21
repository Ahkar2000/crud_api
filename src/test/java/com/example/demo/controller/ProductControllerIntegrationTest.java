package com.example.demo.controller;

import com.example.demo.product.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductControllerIntegrationTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    private Long productId;

    @Test
    @Order(1)
    public void test_createProduct() throws JSONException {
        JSONObject productReq = new JSONObject();
        productReq.put("id", null);
        productReq.put("name", "test");
        productReq.put("price", 1000);
        productReq.put("quantity", 10);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(productReq.toString(), headers);
        ResponseEntity<Product> response = testRestTemplate.postForEntity("/api/v1/products/", entity, Product.class);

        Product product = response.getBody();

        Assertions.assertNotNull(product, "product is null");
        Assertions.assertNotNull(product.getId(), "product id is null");

        productId = product.getId();
    }

    @Test
    @Order(2)
    public void test_getProducts () throws Exception {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/api/v1/products/" , String.class);

        List<Product> responseProducts = new ObjectMapper().readValue(response.getBody(), new TypeReference<List<Product>>() {});

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue(), "Status code mismatch");
        Assertions.assertNotNull(responseProducts, "responseProducts should not be null");
        Assertions.assertEquals(1, responseProducts.size(), "product size should be the same");
    }

    @Test
    @Order(3)
    public void test_updateProduct () throws Exception {
        JSONObject productReq = new JSONObject();
        productReq.put("id", productId);
        productReq.put("name", "test update");
        productReq.put("price", 1001);
        productReq.put("quantity", 10);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(productReq.toString(), headers);

        ResponseEntity<Product> response = testRestTemplate.exchange("/api/v1/products/" + productId,
                HttpMethod.PUT,
                entity,
                Product.class);

        Product updatedProduct = response.getBody();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue(), "Status code mismatch");
        Assertions.assertNotNull(updatedProduct, "updated product should not be null");
        Assertions.assertEquals(productId, updatedProduct.getId(),"product id should be the same");
        Assertions.assertEquals("test update", updatedProduct.getName(), "product name should be the same");
        Assertions.assertEquals(1001, updatedProduct.getPrice(), "price should be the same");
        Assertions.assertEquals(10, updatedProduct.getQuantity(), "quantity should be the same");
    }

    @Test
    @Order(4)
    public void test_deleteProduct () throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange("/api/v1/products/" + productId,
                HttpMethod.DELETE,
                entity,
                String.class);

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue(), "Status code mismatch");
        Assertions.assertEquals(null, response.getBody(), "product should be null");
    }

    @Test
    @Order(5)
    public void test_getProducts_afterDelete () throws Exception {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/api/v1/products/" , String.class);

        List<Product> responseProducts = new ObjectMapper().readValue(response.getBody(), new TypeReference<List<Product>>() {});

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue(), "Status code mismatch");
        Assertions.assertEquals(0, responseProducts.size(), "product size should be the same");
    }
}
