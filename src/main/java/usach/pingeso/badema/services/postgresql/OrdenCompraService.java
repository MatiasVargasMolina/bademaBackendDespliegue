package usach.pingeso.badema.services.postgresql;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usach.pingeso.badema.dtos.adquisiciones.ProveedorListDTO;
import usach.pingeso.badema.dtos.obra.OrdenCompraListObraDTO;
import usach.pingeso.badema.dtos.ordencompra.*;
import usach.pingeso.badema.entities.*;
import usach.pingeso.badema.repositories.postgresql.OrdenCompraRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdenCompraService {
    final OrdenCompraRepository ordenCompraRepository;
    private final UsuarioObraService usuarioObraService;
    private final ProveedorService proveedorService;
    private final ProveedorMaterialService proveedorMaterialService;

    public OrdenCompraService(OrdenCompraRepository ordenCompraRepository, UsuarioObraService obraService,
                               ProveedorService proveedorService, ProveedorMaterialService proveedorMaterialService) {
        this.ordenCompraRepository = ordenCompraRepository;
        this.usuarioObraService = obraService;
        this.proveedorService = proveedorService;
        this.proveedorMaterialService = proveedorMaterialService;
    }

    @Transactional
    public OrdenCompraDTO saveOrden(CrearOrdenCompraDTO orden) {
        //Buscar proveedor y responsable
        ProveedorListDTO proveedor = proveedorService.getProveedorById(orden.getIdProveedor());
        UsuarioObraEntity responsable = usuarioObraService.getUsuarioObraById(orden.getIdResponsable());

        //Crear entidad de la orden
        OrdenCompraEntity nuevaOrden = new OrdenCompraEntity();
        nuevaOrden.setFechaEmision(LocalDate.now());
        nuevaOrden.setFechaEntrega(orden.getFechaEntrega());
        nuevaOrden.setEstado(0);
        nuevaOrden.setResponsable(responsable);

        //Construir lista de detalles de la orden
        List<DetalleOrdenCompraEntity> detalles = new ArrayList<>();
        int total = 0;

        for (ItemOrdenCompraDTO item : orden.getItems()) {
            DetalleOrdenCompraEntity detalle = new DetalleOrdenCompraEntity();
            detalle.setObservacion(item.getObservaciones());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getCantidad() != 0 ? item.getTotal()/item.getCantidad() : 0);
            detalle.setOrdenCompra(nuevaOrden);

            ProveedorMaterialEntity paridad = proveedorMaterialService.getByProveedorAndMaterial(
                    orden.getIdProveedor(), item.getIdMaterial()
            );
            detalle.setParidadProveedor(paridad);

            detalles.add(detalle);
            total += item.getTotal();
        }

        nuevaOrden.setDetalles(detalles);
        //Guardar la orden para obtener el id
        OrdenCompraEntity ordenGuardada = ordenCompraRepository.save(nuevaOrden);

        //Asignar el nombre a la orden
        ordenGuardada.setNumeroOrden("Orden de compra #" + ordenGuardada.getId());
        ordenCompraRepository.save(ordenGuardada); //Actualizar el nombre de la orden

        //Construcción DTO
        OrdenCompraDTO ordenDto = new OrdenCompraDTO();
        ordenDto.setId(ordenGuardada.getId());
        ordenDto.setNumeroOrden(ordenGuardada.getNumeroOrden());
        ordenDto.setFechaEmision(ordenGuardada.getFechaEmision());
        ordenDto.setFechaEntrega(ordenGuardada.getFechaEntrega());
        ordenDto.setTotal(total);
        ordenDto.setEstado(ordenGuardada.getEstado());
        ordenDto.setIdProveedor(proveedor.getId());
        ordenDto.setIdResponsable(responsable.getId());
        ordenDto.setItems(orden.getItems());
        return ordenDto;
    }

    @Transactional
    public void updateOrdenCompra(OrdenCompraEntity orden) {
        ordenCompraRepository.save(orden);
    }

    //Utilizado en ObraDetalleService
    public List<OrdenCompraListObraDTO> getOrdenesCompraByIdObra(Long idObra){
        return ordenCompraRepository.getOrdenCompraByIdObra(idObra);
    }

    public List<OrdenCompraEntity> getOrdenesEntityByIdObra(Long idObra) {
        return ordenCompraRepository.getOrdenesEntityByIdObra(idObra);
    }

    public DocumentoOrdenCompraDTO generarDocumentoOrdenCompra(Long idOrden) {
        OrdenCompraEntity orden = ordenCompraRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        ProveedorEntity proveedor = null;
        DetalleOrdenCompraEntity detalleEjemplo = orden.getDetalles().stream().findFirst().orElse(null);
        if (detalleEjemplo != null && detalleEjemplo.getParidadProveedor() != null) {
            proveedor = detalleEjemplo.getParidadProveedor().getProveedor();
        }

        if (proveedor == null) {
            throw new RuntimeException("Proveedor no encontrado para esta orden.");
        }

        //Convertir detalles
        List<ItemOrdenCompraPDFDTO> items = orden.getDetalles().stream().map(det -> {
            ItemOrdenCompraPDFDTO dto = new ItemOrdenCompraPDFDTO();
            dto.setIdMaterial(det.getParidadProveedor().getMaterial().getId());
            dto.setNombreMaterial(det.getParidadProveedor().getMaterial().getNombre());
            dto.setCantidad(det.getCantidad());
            dto.setPrecioUnitario(det.getPrecioUnitario());
            dto.setTotal(det.getCantidad() * det.getPrecioUnitario());
            dto.setObservaciones(det.getObservacion());
            return dto;
        }).toList();

        int totalNeto = items.stream().mapToInt(ItemOrdenCompraDTO::getTotal).sum();
        int iva = (int) Math.round(totalNeto * 0.19);
        int totalGlobal = totalNeto + iva;

        DocumentoOrdenCompraDTO doc = new DocumentoOrdenCompraDTO();
        doc.setNumeroOrden(orden.getId() + " - 2025");
        doc.setFechaEmision(orden.getFechaEmision());
        doc.setIdProveedor(proveedor.getId());
        doc.setNombreProveedor(proveedor.getNombreProveedor());
        doc.setRutProveedor(proveedor.getRutProveedor());
        doc.setDireccionProveedor(proveedor.getDireccionProveedor());
        doc.setNombreVendedor(proveedor.getNombreVendedor());
        doc.setTelefonoVendedor(proveedor.getTelefonoVendedor());
        doc.setEmailVendedor(proveedor.getEmailVendedor());
        doc.setCondiciones(proveedor.getCondiciones());

        doc.setIdObra(orden.getResponsable().getObra().getId());
        doc.setObraNombre(orden.getResponsable().getObra().getNombre());

        doc.setItems(items);
        doc.setTotalNeto(totalNeto);
        doc.setIva(iva);
        doc.setTotalGlobal(totalGlobal);

        return doc;
    }
}
