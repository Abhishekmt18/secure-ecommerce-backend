package com.securecommerce.app.service;

import com.securecommerce.app.entity.Product;
import com.securecommerce.app.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

    @Service
    public class ProductService {

        @Autowired
        private ProductRepository productRepository;

        public Page<Product> getAllProducts(Pageable pageable) {
            return productRepository.findAll(pageable);
        }
        public Product save(Product product) {
            return productRepository.save(product);
        }
    }
