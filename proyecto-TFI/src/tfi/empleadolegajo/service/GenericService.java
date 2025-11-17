package tfi.empleadolegajo.service;

import java.util.List;

public interface GenericService<T> {

    void insertar(T entidad);

    void actualizar(T entidad);

    void eliminar(long id);

    T getById(long id);

    List<T> getAll();
}
