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

    public Product updateProduct(Long id, Product product) {
        boolean check = this.productRepository.existsById(id);
        if(!check){
            throw new IllegalStateException("Product does not exist!");
        }else{
            Product uProduct = this.productRepository.findById(id).get();
            if(Objects.nonNull(product.getName()) && product.getName().length() > 0 && !Objects.equals(product.getName(),uProduct.getName())){
                uProduct.setName(product.getName());
            }
            if(Objects.nonNull(product.getQuantity()) && product.getQuantity() > 0){
                uProduct.setQuantity(product.getQuantity());
            }
            if(Objects.nonNull(product.getPrice()) && product.getPrice() > 0){
                uProduct.setPrice(product.getPrice());
            }
            this.productRepository.save(uProduct);
            return uProduct;
        }
    }
}
