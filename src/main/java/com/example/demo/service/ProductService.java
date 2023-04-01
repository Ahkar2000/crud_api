package com.example.demo.service;

import com.example.demo.product.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    public Product createProduct(Product product) {
        Optional<Product> productByName = this.productRepository.findProductByName(product.getName());
        if(productByName.isPresent()){
            throw new IllegalStateException("Product already exists!");
        }else{
            this.productRepository.save(product);
        }
        return product;
    }

    public void deleteProduct(Long id) {
        boolean check = this.productRepository.existsById(id);
        if(!check){
            throw new IllegalStateException("Product does not exist!");
        }else {
            this.productRepository.deleteById(id);
        }
    }

    public Product updateProduct(Long id,String name, Integer quantity, Integer price) {
        boolean check = this.productRepository.existsById(id);
        if(!check){
            throw new IllegalStateException("Product does not exist!");
        }else{
            Product product = (Product) this.productRepository.findById(id).get();
            if(name != null && name.length() > 0 && !Objects.equals(product.getName(),name)){
                product.setName(name);
            }
            if(quantity != null && quantity > 0){
                product.setQuantity(quantity);
            }
            if(price != null && price > 0){
                product.setPrice(price);
            }
            return product;
        }
    }
}
