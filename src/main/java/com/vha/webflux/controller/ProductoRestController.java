package com.vha.webflux.controller;

import com.vha.webflux.models.entity.Producto;
import com.vha.webflux.repository.ProductoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/api/productos")
@Slf4j
public class ProductoRestController {

    @Autowired
    private ProductoRepository productRepo;

    @GetMapping
    public Flux<Producto> index() {

        Flux<Producto> products = productRepo.findAll().map(producto -> {
            producto.setNombre(producto.getNombre().toUpperCase());
            return producto;
        }).doOnNext(product -> log.info(product.getNombre()));

        return products;
    }

    @GetMapping("/{id}")
    public Mono<Producto> show(@PathVariable String id) {

        // Mono<Producto> product = productRepo.findById(id);

        Flux<Producto> products = productRepo.findAll();

        Mono<Producto> product = products
                .filter(p -> p.getId().equals(id))
                .next()
                .doOnNext(producto -> log.info(producto.getNombre()));

        return product;

    }

}
