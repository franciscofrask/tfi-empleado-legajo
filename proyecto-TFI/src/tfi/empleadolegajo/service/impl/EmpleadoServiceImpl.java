package tfi.empleadolegajo.service.impl;

import tfi.empleadolegajo.config.DatabaseConnection;
import tfi.empleadolegajo.dao.EmpleadoDao;
import tfi.empleadolegajo.dao.LegajoDao;
import tfi.empleadolegajo.dao.impl.EmpleadoDaoImpl;
import tfi.empleadolegajo.dao.impl.LegajoDaoImpl;
import tfi.empleadolegajo.entities.Empleado;
import tfi.empleadolegajo.entities.Legajo;
import tfi.empleadolegajo.service.EmpleadoService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoDao empleadoDao = new EmpleadoDaoImpl();
    private final LegajoDao legajoDao = new LegajoDaoImpl();

    // Metodos simples del GenericService

    @Override
    public void insertar(Empleado entidad) {
        // Validar DNI unico
        if (empleadoDao.buscarPorDni(entidad.getDni()) != null) {
            throw new RuntimeException("Ya existe un empleado con ese DNI");
        }
        empleadoDao.crear(entidad);
    }

    @Override
    public void actualizar(Empleado entidad) {
        empleadoDao.actualizar(entidad);
    }

    @Override
    public void eliminar(long id) {
        empleadoDao.eliminar(id);
    }

    @Override
    public Empleado getById(long id) {
        return empleadoDao.leer(id);
    }

    @Override
    public List<Empleado> getAll() {
        return empleadoDao.leerTodos();
    }

    // MÉTODOS CON TRANSACCIÓN

    @Override
    public void crearEmpleadoConLegajo(Empleado empleado, Legajo legajo) {

        // Validaciones previas
        if (empleadoDao.buscarPorDni(empleado.getDni()) != null) {
            throw new RuntimeException("DNI ya registrado");
        }

        if (legajoDao.buscarPorNroLegajo(legajo.getNroLegajo()) != null) {
            throw new RuntimeException("Numero de legajo ya registrado");
        }

        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1 Crear empleado
            empleadoDao.crear(empleado);

            // 2 Asignar el ID del empleado al legajo
            legajo.setEmpleadoId(empleado.getId());

            // 3 Crear legajo
            legajoDao.crear(legajo);

            conn.commit(); 
            System.out.println("Empleado y legajo creados correctamente.");

        } catch (Exception e) {

            try {
                if (conn != null) conn.rollback(); 
            } catch (SQLException ex) {
                throw new RuntimeException("Error al hacer rollback", ex);
            }

            throw new RuntimeException("Error en transacción: " + e.getMessage(), e);

        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error al cerrar conexión", e);
            }
        }
    }

    @Override
    public void eliminarEmpleadoYLegajo(long empleadoId) {

        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1 Eliminar legajo 
            Legajo l = legajoDao.buscarPorEmpleadoId(empleadoId);
            if (l != null) {
                legajoDao.eliminar(l.getId());
            }

            // 2 Eliminar empleado 
            empleadoDao.eliminar(empleadoId);

            conn.commit();
            System.out.println("Empleado y legajo eliminados correctamente.");

        } catch (Exception e) {

            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Error al hacer rollback", ex);
            }

            throw new RuntimeException("Error al eliminar con transacción", e);

        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error al cerrar conexión", e);
            }
        }
    }
}
