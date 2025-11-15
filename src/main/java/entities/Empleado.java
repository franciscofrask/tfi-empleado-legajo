package entities;

public class Empleado {
    private Long id;
    private String dni;
    private String nombre;
    private String apellido;
    private boolean eliminado;

    private Legajo detalle; // Relación 1→1 unidireccional

    public Empleado() {}

    public Empleado(Long id, String dni, String nombre, String apellido, boolean eliminado, Legajo detalle) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.eliminado = eliminado;
        this.detalle = detalle;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }

    public Legajo getDetalle() { return detalle; }
    public void setDetalle(Legajo detalle) { this.detalle = detalle; }

    @Override
    public String toString() {
        return "Empleado{id=%d, dni='%s', nombre='%s', apellido='%s', eliminado=%s, detalle=%s}"
                .formatted(id, dni, nombre, apellido, eliminado, detalle);
    }
}
