package usach.pingeso.badema.dtos.seguimiento;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrdenCompraTrazaDTO {
    private Long idOrden;
    private String numeroOrden;
    private String nombreProveedor;
    private String direccionProveedor;
    private int cantidadTotal; //Cantidad ordenada
    private int precioUnitario;
    private String estado;
    private int cantidadEntregada; //Suma de recepciones
    private int cantidadInstalada; //Cantidad instalada (inventario)
    private LocalDate fechaCompra; //Fecha de emisión de la orden de compra
    private LocalDate fechaEstimadaEntrega; //Fecha de entrega de la orden de compra

    public OrdenCompraTrazaDTO(Long idOrden, String numeroOrden, String nombreProveedor, String direccionProveedor,
                               Integer cantidadTotal, Integer precioUnitario, Long cantidadEntregada,
                               Integer cantidadInstalada, LocalDate fechaCompra, LocalDate fechaEstimadaEntrega) {
        this.idOrden = idOrden;
        this.numeroOrden = numeroOrden;
        this.nombreProveedor = nombreProveedor;
        this.direccionProveedor = direccionProveedor;
        this.cantidadTotal = cantidadTotal != null ? cantidadTotal : 0;
        this.precioUnitario = precioUnitario != null ? precioUnitario : 0;
        this.cantidadEntregada = cantidadEntregada != null ? cantidadEntregada.intValue() : 0;
        this.cantidadInstalada = cantidadInstalada != null ? cantidadInstalada : 0;
        this.fechaCompra = fechaCompra;
        this.fechaEstimadaEntrega = fechaEstimadaEntrega;
    }
}
