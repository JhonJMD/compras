package com.app.compras.domain.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "categorias")
@Data
@Schema(description = "Entidad que representa una categoría de productos")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    @Schema(description = "Identificador único de la categoría", example = "1")
    private int idCategoria;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 3, max = 45, message = "La descripción debe tener entre 3 y 45 caracteres")
    @Column(length = 45, nullable = false)
    @Schema(description = "Nombre o descripción de la categoría", example = "Electrónicos")
    private String descripcion;

    @NotNull(message = "El estado es obligatorio")
    @Column(nullable = false)
    @Schema(description = "Estado de la categoría (1=activo, 0=inactivo)", example = "1")
    private byte estado;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    @JsonIgnore
    @Schema(hidden = true)
    private List<Producto> productos;

    public Categoria() {
    }
}