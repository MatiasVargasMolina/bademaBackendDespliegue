package usach.pingeso.badema.dtos.ordencompra;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DocumentoOrdenCompraDTO {
    //Datos de la Orden de Compra
    private String numeroOrden;
    private LocalDate fechaEmision;

    //Datos del Proveedor
    private Long idProveedor;
    private String nombreProveedor;
    private String rutProveedor;
    private String direccionProveedor;
    private String nombreVendedor;
    private String telefonoVendedor;
    private String emailVendedor;
    private String condiciones;

    //Datos de la Obra
    private Long idObra;
    private String obraNombre;

    //Datos Totales
    private List<ItemOrdenCompraPDFDTO> items;
    private Integer totalNeto; //Suma de subtotales de los  ('cantidad * precio_unitario')
    private Integer iva;  //Iva = 19%
    private Integer totalGlobal; //Suma del total neto + IVA
}