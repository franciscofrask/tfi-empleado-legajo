package service;

import java.util.List;

public interface GenericService<T> {
    T insertar(T t) throws Exception;
    T actualizar(T t) throws Exception;
    boolean eliminar(long id) throws Exception;
    T getById(long id) throws Exception;
    List<T> getAll() throws Exception;
}
