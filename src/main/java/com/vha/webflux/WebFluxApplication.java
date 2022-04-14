package com.vha.webflux;

import com.vha.webflux.models.entity.Producto;
import com.vha.webflux.repository.ProductoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;

@SpringBootApplication
@Slf4j
public class WebFluxApplication implements CommandLineRunner {

    @Autowired
    private ProductoRepository productoRepo;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    public static void main(String[] args) {
        SpringApplication.run(WebFluxApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        mongoTemplate.dropCollection("productos").subscribe();

        Flux.just(Producto.builder().nombre("TV Panasonix Pantalla LCD").precio(133.9).build(),
                Producto.builder().nombre("TV Samsung Pantalla LCD").precio(122.0).build(),
                Producto.builder().nombre("TV Sony Pantalla LCD").precio(154.0).build(),
                Producto.builder().nombre("TV LG Pantalla LCD").precio(123.3).build(),
                Producto.builder().nombre("Iphone 13 64GB").precio(1333.9).build(),
                Producto.builder().nombre("Iphone S ").precio(1433.9).build(),
                Producto.builder().nombre("Portatil Lenovo 13 pul").precio(1533.9).build(),
                Producto.builder().nombre("Portatil Lenovo 15 pul").precio(1633.9).build(),
                Producto.builder().nombre("Tablet Xaomi 10 pul").precio(1833.9).build(),
                Producto.builder().nombre("Tablet Xaomi 8 pul").precio(1233.9).build()
        ).flatMap(producto -> {
            producto.setCreateAt(new Date());
            return productoRepo.save(producto);
        }).subscribe(producto -> log.info("Insert: " + producto.getId() + " " + producto.getNombre()));

    }
}
