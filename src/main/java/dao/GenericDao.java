package dao;

import java.sql.Connection;
import java.util.List;

public interface GenericDao<T> {
    T crear(T t, Connection conn) throws Exception;
    T leer(long id, Connection conn) throws Exception;
    List<T> leerTodos(Connection conn) throws Exception;
    T actualizar(T t, Connection conn) throws Exception;
    boolean eliminar(long id, Connection conn) throws Exception;
}
