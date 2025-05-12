package com.app.compras.domain.entity;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "compras_productos")
@Data
@Schema(description = "Entidad que representa la relación entre una compra y los productos adquiridos")
public class CompraProducto {
    @EmbeddedId
    @Valid
    @NotNull(message = "La clave compuesta es obligatoria")
    @Schema(description = "Clave compuesta que identifica la relación compra-producto")
    private CompraProductoId id;

    @ManyToOne
    @MapsId("idCompra")
    @JoinColumn(name = "id_compra")
    @Schema(description = "Compra a la que pertenece este detalle")
    private Compra compra;

    @ManyToOne
    @MapsId("idProducto")
    @JoinColumn(name = "id_producto")
    @Schema(description = "Producto incluido en esta compra")
    private Producto producto;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Column(nullable = false)
    @Schema(description = "Cantidad de unidades del producto", example = "2")
    private int cantidad;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.01", message = "El total debe ser mayor que 0")
    @Digits(integer = 14, fraction = 2, message = "El total debe tener máximo 14 dígitos enteros y 2 decimales")
    @Column(precision = 16, scale = 2, nullable = false)
    @Schema(description = "Valor total de la línea (precio × cantidad)", example = "2599.98")
    private BigDecimal total;

    @NotNull(message = "El estado es obligatorio")
    @Column(nullable = false)
    @Schema(description = "Estado del detalle de compra (1=activo, 0=inactivo)", example = "1")
    private byte estado;
}