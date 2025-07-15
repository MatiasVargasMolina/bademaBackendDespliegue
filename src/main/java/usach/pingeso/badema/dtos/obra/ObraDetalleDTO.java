package usach.pingeso.badema.dtos.obra;

import lombok.Getter;
import lombok.Setter;
import usach.pingeso.badema.documents.HitosObraDocument;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ObraDetalleDTO {
    private Long id;
    private String nombre;
    private String empresaContratista;
    private LocalDate fechaInicio;
    private LocalDate fechaTermino;
    private int metrosCuadrados;
    private boolean esPublico;
    private String estado;

    private List<PedidosATrabajarListDTO> pedidosATrabajar;

    private List<OrdenCompraListObraDTO> ordenesCompra;

    private HitosObraDocument hitos;

    private List<SubContratoListDTO> subContratados;

    private List<AsociadosListDTO> asociados;

    private List<UsuarioObraListDTO> administrativos;
}
