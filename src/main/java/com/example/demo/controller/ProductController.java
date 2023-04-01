package com.example.demo.controller;

import com.example.demo.product.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = {"api/v1/products"})
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getProducts(){
        return productService.getProducts();
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product){
        return productService.createProduct(product);
    }

    @DeleteMapping(path = {"/{id}"})
    public void deleteProduct(@PathVariable Long id){
        this.productService.deleteProduct(id);
    }

    @PutMapping(path = {"/{id}"})
    public Product updateProduct(@PathVariable Long id,@RequestParam(required = false) String name, @RequestParam(required = false) Integer quantity, @RequestParam(required = false) Integer price){
        return this.productService.updateProduct(id,name,quantity,price);
    }
}
