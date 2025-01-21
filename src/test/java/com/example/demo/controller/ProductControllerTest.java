package com.example.demo.controller;

import com.example.demo.product.Product;
import com.example.demo.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(controllers = ProductController.class, excludeAutoConfiguration = { SecurityAutoConfiguration.class })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductControllerTest {
    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    private static Product product;

    @BeforeAll
    public static void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("test");
        product.setPrice(1000);
        product.setQuantity(10);
    }

    @Test
    @DisplayName("Test create product")
    public void test_createProduct() throws Exception {
        Mockito.when(productService.createProduct(any(Product.class))).thenReturn(product);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(product));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();
        Product createdProduct = new ObjectMapper()
                .readValue(responseBodyAsString, Product.class);

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status code mismatch");
        Assertions.assertNotNull(createdProduct, "created product should not be null");
        Assertions.assertNotNull(product.getId(), "product id should not be null");
        Assertions.assertEquals(createdProduct.getId(), product.getId(), "product id should be the same");
        Assertions.assertEquals(createdProduct.getName(), product.getName(), "product name should be the same");
        Assertions.assertEquals(createdProduct.getPrice(), product.getPrice(), "price should be the same");
        Assertions.assertEquals(createdProduct.getQuantity(), product.getQuantity(), "quantity should be the same");
    }

    @Test
    @DisplayName("Test Delete Product")
    public void test_deleteProduct() throws Exception {
        Mockito.doNothing().when(productService).deleteProduct(Mockito.anyLong());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/products/" + product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(product));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status code mismatch");
        Assertions.assertEquals("", responseBodyAsString, "product should be null");
    }

    @Test
    @DisplayName("Test update product")
    public void test_updateProduct() throws Exception {
        product.setName("update test");
        product.setPrice(2000);

        Mockito.when(productService.updateProduct(Mockito.anyLong(), Mockito.any())).thenReturn(product);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/products/" + product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(product));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();
        Product updatedProduct = new ObjectMapper().readValue(responseBodyAsString, Product.class);

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status code mismatch");
        Assertions.assertNotNull(updatedProduct, "updated product should not be null");
        Assertions.assertEquals(updatedProduct.getId(), product.getId(), "product id should be the same");
        Assertions.assertEquals(updatedProduct.getName(), product.getName(), "product name should be the same");
        Assertions.assertEquals(updatedProduct.getPrice(), product.getPrice(), "price should be the same");
        Assertions.assertEquals(updatedProduct.getQuantity(), product.getQuantity(), "quantity should be the same");
    }

    @Test
    @DisplayName("Test get all products")
    public void test_getProducts () throws Exception {
        List<Product> products = new ArrayList<>();
        products.add(product);

        Mockito.when(productService.getProducts()).thenReturn(products);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/products");
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();
        List<Product> responseProducts = new ObjectMapper().readValue(responseBodyAsString, new TypeReference<List<Product>>() {});

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status code mismatch");
        Assertions.assertNotNull(responseProducts, "responseProducts should not be null");
        Assertions.assertEquals(responseProducts.size(), products.size(), "product size should be the same");
    }
}
