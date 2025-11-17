package tfi.empleadolegajo.service.impl;

import tfi.empleadolegajo.dao.LegajoDao;
import tfi.empleadolegajo.dao.impl.LegajoDaoImpl;
import tfi.empleadolegajo.entities.Legajo;
import tfi.empleadolegajo.service.LegajoService;

import java.util.List;

public class LegajoServiceImpl implements LegajoService {

    private final LegajoDao legajoDao = new LegajoDaoImpl();

    @Override
    public void insertar(Legajo entidad) {
        // validación nro_legajo único + dao.crear(...)
    }

    @Override
    public void actualizar(Legajo entidad) {
        // dao.actualizar(...)
    }

    @Override
    public void eliminar(long id) {
        legajoDao.eliminar(id);
    }

    @Override
    public Legajo getById(long id) {
        return legajoDao.leer(id);
    }

    @Override
    public List<Legajo> getAll() {
        return legajoDao.leerTodos();
    }
}
