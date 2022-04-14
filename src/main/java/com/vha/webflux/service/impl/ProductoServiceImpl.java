package com.vha.webflux.service.impl;

import com.vha.webflux.models.entity.Producto;
import com.vha.webflux.repository.ProductoRepository;
import com.vha.webflux.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productRepo;

    @Override
    public Flux<Producto> findAll() {
        return productRepo.findAll();
    }

    @Override
    public Flux<Producto> findAllConNombreUpperCase() {
        return productRepo.findAll().map(producto -> {
            producto.setNombre(producto.getNombre().toUpperCase());
            return producto;
        });
    }

    @Override
    public Flux<Producto> findAllConNombreUpperCaseRepeat() {
        return findAllConNombreUpperCase().repeat(5000);
    }

    @Override
    public Mono<Producto> findById(String id) {
        return productRepo.findById(id);
    }

    @Override
    public Mono<Producto> save(Producto producto) {
        return productRepo.save(producto);
    }

    @Override
    public Mono<Void> delete(Producto producto) {
        return productRepo.delete(producto);
    }
}
