package com.vha.webflux.controller;

import com.vha.webflux.models.entity.Producto;
import com.vha.webflux.repository.ProductoRepository;
import com.vha.webflux.service.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Locale;

@SessionAttributes("producto")
@Controller
@Slf4j
public class ProductoController {

    @Autowired
    private ProductoService service;


    @GetMapping({"/listar", "/"})
    public Mono<String> listar(Model model) {

        Flux<Producto> productos = service.findAll().map(producto -> {
            producto.setNombre(producto.getNombre().toUpperCase());
            return producto;
        });

        productos.subscribe(product -> log.info(product.getNombre()));

        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Listado de productos");

        return Mono.just("listar");

    }

    @GetMapping("/form")
    public Mono<String> crear(Model model) {

        model.addAttribute("producto", new Producto());
        model.addAttribute("titulo", "Formulario de producto");
        model.addAttribute("boton", "Crear nuevo producto");


        return Mono.just("form");
    }

    @GetMapping("/form/{id}")
    public Mono<String> editar (@PathVariable String id, Model model){

        Mono<Producto> productoMono = service.findById(id).doOnNext( p -> {
            log.info("Productor: "+ p.getNombre());
        }).defaultIfEmpty(new Producto());

        model.addAttribute("titulo", "editar producto");
        model.addAttribute("producto", productoMono);
        model.addAttribute("boton", "Editar nuevo producto");

        return Mono.just("form");
    }

    @GetMapping("/form-v2/{id}")
    public Mono<String> editarV2 (@PathVariable String id, Model model){

        return  service.findById(id).doOnNext( p -> {
            log.info("Productor: "+ p.getNombre());

            model.addAttribute("titulo", "editar producto");
            model.addAttribute("producto", p);
            model.addAttribute("boton", "Editar nuevo producto");

        }).defaultIfEmpty(new Producto())
                .flatMap( p -> {
                    if(p.getId() == null){
                        return Mono.error(new InterruptedException("No existe el producto"));
                    }
                    return Mono.just(p);
                })
                .then(Mono.just("/form"))
                .onErrorResume( ex -> Mono.just("redirect:/listar?error=no+existe+el+producto"));
    }

    @PostMapping("/form")
    public Mono<String> guardar(Producto producto, SessionStatus status) {
        status.setComplete();
        return service.save(producto)
                .doOnNext(p -> log.info("Producto guardado " + p.getNombre()))
                .thenReturn("redirect:/listar");
    }

    @GetMapping("/listar-data-driver")
    public String listarDataDriver(Model model) {

        Flux<Producto> productos = service.findAllConNombreUpperCase().delayElements(Duration.ofSeconds(1));

        productos.subscribe(product -> log.info(product.getNombre()));

        model.addAttribute("productos", new ReactiveDataDriverContextVariable(productos, 1));
        model.addAttribute("titulo", "Listado de productos");

        return "listar";

    }

    @GetMapping("/listar-full")
    public String listarFull(Model model) {

        Flux<Producto> productos = service.findAllConNombreUpperCaseRepeat();

        productos.subscribe(product -> log.info(product.getNombre()));

        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Listado de productos");

        return "listar";

    }

    @GetMapping("/listar-chunked")
    public String listarChunked(Model model) {

        Flux<Producto> productos = service.findAll().map(producto -> {
            producto.setNombre(producto.getNombre().toUpperCase());
            return producto;
        }).repeat(5000);

        productos.subscribe(product -> log.info(product.getNombre()));

        model.addAttribute("productos", new ReactiveDataDriverContextVariable(productos, 1));
        model.addAttribute("titulo", "Listado de productos");

        return "listar-chunked";

    }
}
