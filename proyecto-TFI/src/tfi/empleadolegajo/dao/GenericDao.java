package tfi.empleadolegajo.dao;

import java.util.List;

public interface GenericDao<T> {

    void crear(T entidad);

    T leer(long id);

    List<T> leerTodos();

    void actualizar(T entidad);

    void eliminar(long id); 
}
