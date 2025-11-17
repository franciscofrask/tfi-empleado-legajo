package tfi.empleadolegajo.dao;

import java.util.List;
import tfi.empleadolegajo.entities.Empleado;

public interface EmpleadoDao extends GenericDao<Empleado> {

    Empleado buscarPorDni(String dni);

    List<Empleado> buscarNoEliminados();
}
