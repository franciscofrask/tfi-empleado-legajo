package entities;

import java.time.LocalDate;

public class Legajo {
    private Long id;
    private String numero;
    private String categoria;
    private LocalDate fechaAlta;
    private boolean eliminado;

    public Legajo() {}

    public Legajo(Long id, String numero, String categoria, LocalDate fechaAlta, boolean eliminado) {
        this.id = id;
        this.numero = numero;
        this.categoria = categoria;
        this.fechaAlta = fechaAlta;
        this.eliminado = eliminado;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public LocalDate getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }

    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }

    @Override
    public String toString() {
        return "Legajo{id=%d, numero='%s', categoria='%s', fechaAlta=%s, eliminado=%s}"
                .formatted(id, numero, categoria, fechaAlta, eliminado);
    }
}
