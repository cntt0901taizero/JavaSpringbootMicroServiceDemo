package com.taichu.productservice.controller;

import com.taichu.productservice.dto.ProductRequest;
import com.taichu.productservice.dto.ProductResponse;
import com.taichu.productservice.dto.ResponseObject;
import com.taichu.productservice.model.Product;
import com.taichu.productservice.repository.ProductRepository;
import com.taichu.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @PostMapping("/create-product")
    public ResponseEntity<ResponseObject> createProduct(@RequestBody ProductRequest productRequest) {
        Product productFounded = productRepository.findOneByName(productRequest.getName());
        if (productFounded != null) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("false", "Product is already taken", "")
            );
        }
        productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Request successfully", "")
        );
    }

    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<ResponseObject> updateProduct(@RequestBody ProductRequest productRequest, @PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);
        Product dataSave = new Product();
        dataSave.setId(id);
        dataSave.setName(productRequest.getName());
        dataSave.setPrice(productRequest.getPrice());
        dataSave.setDescription(productRequest.getDescription());

        if (!product.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("false", "Can not find Product", "")
            );
        }
        productRepository.save(dataSave);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Request successfully", "")
        );
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Request successfully", "")
        );
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<ResponseObject> getAllProducts() {
        Optional<List<ProductResponse>> products = Optional.ofNullable(productService.getAllProducts());
        return products.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Request successfully", products)
                ) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("false","No product can found", "")
                );
    }

}
