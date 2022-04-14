package com.vha.webflux.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Document(collection = "productos")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Producto {

    @Id
    private String id;

    private String nombre;
    private Double precio;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAt;

}
