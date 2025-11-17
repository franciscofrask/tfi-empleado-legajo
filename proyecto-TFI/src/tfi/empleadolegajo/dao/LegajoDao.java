package tfi.empleadolegajo.dao;

import tfi.empleadolegajo.entities.Legajo;

public interface LegajoDao extends GenericDao<Legajo> {

    Legajo buscarPorNroLegajo(String nroLegajo);

    Legajo buscarPorEmpleadoId(long empleadoId);
}
