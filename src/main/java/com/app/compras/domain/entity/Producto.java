package com.app.compras.domain.entity;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "productos")
@Data
@Schema(description = "Entidad que representa un producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    @Schema(description = "Identificador único del producto", example = "1")
    private int idProducto;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 45, message = "El nombre debe tener entre 3 y 45 caracteres")
    @Column(length = 45, nullable = false)
    @Schema(description = "Nombre del producto", example = "Laptop HP")
    private String nombre;

    @NotBlank(message = "El código de barras es obligatorio")
    @Size(min = 8, max = 150, message = "El código de barras debe tener entre 8 y 150 caracteres")
    @Column(length = 150, name = "codigo_barras", nullable = false)
    @Schema(description = "Código de barras del producto", example = "789012345678")
    private String codigoBarras;

    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que 0")
    @Digits(integer = 14, fraction = 2, message = "El precio debe tener máximo 14 dígitos enteros y 2 decimales")
    @Column(precision = 16, scale = 2, name = "precio_venta", nullable = false)
    @Schema(description = "Precio de venta del producto", example = "1299.99")
    private BigDecimal precioVenta;

    @NotNull(message = "La cantidad en stock es obligatoria")
    @Min(value = 0, message = "La cantidad en stock no puede ser negativa")
    @Column(name = "cantidad_stock", nullable = false)
    @Schema(description = "Cantidad disponible en inventario", example = "10")
    private int cantidadStock;

    @NotNull(message = "El estado es obligatorio")
    @Column(nullable = false)
    @Schema(description = "Estado del producto (1=activo, 0=inactivo)", example = "1")
    private byte estado;

    @NotNull(message = "La categoría es obligatoria")
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    @Schema(description = "Categoría a la que pertenece el producto")
    private Categoria categoria;

    public Producto() {
    }
}