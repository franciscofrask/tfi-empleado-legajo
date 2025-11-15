package dao;

import entities.Legajo;
import java.sql.Connection;
import java.util.Optional;

public interface LegajoDao extends GenericDao<Legajo> {
    Optional<Legajo> buscarPorNumero(String numero, Connection conn) throws Exception;
    Optional<Legajo> buscarPorEmpleadoId(long empleadoId, Connection conn) throws Exception;
}
