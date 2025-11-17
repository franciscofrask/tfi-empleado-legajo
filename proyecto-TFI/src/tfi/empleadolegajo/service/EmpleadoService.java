package tfi.empleadolegajo.service;

import tfi.empleadolegajo.entities.Empleado;
import tfi.empleadolegajo.entities.Legajo;

public interface EmpleadoService extends GenericService<Empleado> {

    void crearEmpleadoConLegajo(Empleado empleado, Legajo legajo);

    void eliminarEmpleadoYLegajo(long empleadoId);
}
