package service;

import config.DatabaseConnection;
import dao.EmpleadoDao;
import dao.LegajoDao;
import dao.impl.EmpleadoDaoImpl;
import dao.impl.LegajoDaoImpl;
import entities.Empleado;
import entities.Legajo;

import java.sql.Connection;
import java.time.LocalDate;

public class EmpleadoService implements GenericService<Empleado> {
    private final EmpleadoDao empleadoDao = new EmpleadoDaoImpl();
    private final LegajoDao legajoDao = new LegajoDaoImpl();

    @Override
    public Empleado insertar(Empleado e) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                validarEmpleado(e, conn);
                Empleado creado = empleadoDao.crear(e, conn);
                conn.commit();
                return creado;
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public Empleado crearEmpleadoConLegajo(Empleado e, Legajo l) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                validarEmpleado(e, conn);
                validarLegajo(l, conn);

                // 1) crear empleado
                Empleado creado = empleadoDao.crear(e, conn);

                // 2) asegurar 1→1
                if (legajoDao.buscarPorEmpleadoId(creado.getId(), conn).isPresent())
                    throw new IllegalStateException("El empleado ya posee legajo.");

                // 3) crear legajo con empleado_id
                ((LegajoDaoImpl)legajoDao).crearConEmpleadoId(l, creado.getId(), conn);

                conn.commit();
                creado.setDetalle(l);
                return creado;
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    @Override
    public Empleado actualizar(Empleado e) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                if (e.getId() == null) throw new IllegalArgumentException("id requerido");
                validarEmpleado(e, conn);
                Empleado upd = empleadoDao.actualizar(e, conn);
                conn.commit();
                return upd;
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    @Override
    public boolean eliminar(long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // baja lógica de empleado (si querés, marcá también el legajo aquí)
                boolean ok = empleadoDao.eliminar(id, conn);
                conn.commit();
                return ok;
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    @Override
    public Empleado getById(long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            Empleado e = empleadoDao.leer(id, conn);
            if (e == null) return null;
            legajoDao.buscarPorEmpleadoId(id, conn).ifPresent(e::setDetalle);
            return e;
        }
    }

    @Override
    public java.util.List<Empleado> getAll() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return empleadoDao.leerTodos(conn);
        }
    }

    private void validarEmpleado(Empleado e, Connection conn) throws Exception {
        if (e == null) throw new IllegalArgumentException("Empleado requerido");
        if (e.getDni() == null || e.getDni().isBlank()) throw new IllegalArgumentException("DNI requerido");
        if (e.getNombre() == null || e.getNombre().isBlank()) throw new IllegalArgumentException("Nombre requerido");
        if (e.getApellido() == null || e.getApellido().isBlank()) throw new IllegalArgumentException("Apellido requerido");

        var existente = empleadoDao.buscarPorDni(e.getDni(), conn);
        if (existente.isPresent() && (e.getId() == null || !existente.get().getId().equals(e.getId()))) {
            throw new IllegalStateException("DNI ya registrado");
        }
    }

    private void validarLegajo(Legajo l, Connection conn) throws Exception {
        if (l == null) throw new IllegalArgumentException("Legajo requerido");
        if (l.getNumero() == null || l.getNumero().isBlank()) throw new IllegalArgumentException("Número requerido");
        if (l.getCategoria() == null || l.getCategoria().isBlank()) throw new IllegalArgumentException("Categoría requerida");
        if (l.getFechaAlta() == null) l.setFechaAlta(LocalDate.now());

        var existeNum = legajoDao.buscarPorNumero(l.getNumero(), conn);
        if (existeNum.isPresent() && (l.getId() == null || !existeNum.get().getId().equals(l.getId()))) {
            throw new IllegalStateException("Número de legajo ya registrado");
        }
    }
}
