package tfi.empleadolegajo.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Legajo {

    private Long id;
    private boolean eliminado;

    private String nroLegajo;
    private String categoria;
    private String estado;       // "ACTIVO" / "INACTIVO"
    private LocalDate fechaAlta;
    private String observaciones;

    private Long empleadoId;     // FK a empleado.id

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Legajo() {
    }

    public Legajo(Long id, boolean eliminado, String nroLegajo,
                  String categoria, String estado, LocalDate fechaAlta,
                  String observaciones, Long empleadoId,
                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.eliminado = eliminado;
        this.nroLegajo = nroLegajo;
        this.categoria = categoria;
        this.estado = estado;
        this.fechaAlta = fechaAlta;
        this.observaciones = observaciones;
        this.empleadoId = empleadoId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public String getNroLegajo() {
        return nroLegajo;
    }

    public void setNroLegajo(String nroLegajo) {
        this.nroLegajo = nroLegajo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Long getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Long empleadoId) {
        this.empleadoId = empleadoId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Legajo{" +
                "id=" + id +
                ", eliminado=" + eliminado +
                ", nroLegajo='" + nroLegajo + '\'' +
                ", categoria='" + categoria + '\'' +
                ", estado='" + estado + '\'' +
                ", fechaAlta=" + fechaAlta +
                ", observaciones='" + observaciones + '\'' +
                ", empleadoId=" + empleadoId +
                '}';
    }
}
