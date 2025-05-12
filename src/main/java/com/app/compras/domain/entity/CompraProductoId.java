package com.app.compras.domain.entity;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Schema(description = "Clave compuesta para la relación entre compras y productos")
public class CompraProductoId implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "El ID de compra es obligatorio")
    @Min(value = 1, message = "El ID de compra debe ser mayor a 0")
    @Schema(description = "ID de la compra", example = "1")
    private int idCompra;
    
    @NotNull(message = "El ID de producto es obligatorio")
    @Min(value = 1, message = "El ID de producto debe ser mayor a 0")
    @Schema(description = "ID del producto", example = "2")
    private int idProducto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompraProductoId)) return false;
        CompraProductoId that = (CompraProductoId) o;
        return idCompra == that.idCompra && idProducto == that.idProducto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCompra, idProducto);
    }
}