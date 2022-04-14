package com.vha.webflux.repository;

import com.vha.webflux.models.entity.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository  extends ReactiveMongoRepository<Producto, String> {
}
