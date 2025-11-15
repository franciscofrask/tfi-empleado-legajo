package dao;

import entities.Empleado;
import java.sql.Connection;
import java.util.Optional;

public interface EmpleadoDao extends GenericDao<Empleado> {
    Optional<Empleado> buscarPorDni(String dni, Connection conn) throws Exception;
}
