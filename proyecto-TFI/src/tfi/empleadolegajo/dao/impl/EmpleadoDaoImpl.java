package tfi.empleadolegajo.dao.impl;

import tfi.empleadolegajo.config.DatabaseConnection;
import tfi.empleadolegajo.dao.EmpleadoDao;
import tfi.empleadolegajo.entities.Empleado;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDaoImpl implements EmpleadoDao {

    private static final String INSERT_SQL = """
            INSERT INTO empleado (nombre, apellido, dni, email, fecha_ingreso, area)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    private static final String SELECT_BY_ID_SQL = """
            SELECT id, eliminado, nombre, apellido, dni, email,
                   fecha_ingreso, area, created_at, updated_at
            FROM empleado
            WHERE id = ?
            """;

    private static final String SELECT_ALL_SQL = """
            SELECT id, eliminado, nombre, apellido, dni, email,
                   fecha_ingreso, area, created_at, updated_at
            FROM empleado
            """;

    private static final String UPDATE_SQL = """
            UPDATE empleado
            SET nombre = ?, apellido = ?, dni = ?, email = ?,
                fecha_ingreso = ?, area = ?, updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;

    private static final String DELETE_LOGICO_SQL = """
            UPDATE empleado
            SET eliminado = TRUE, updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;

    private static final String SELECT_BY_DNI_SQL = """
            SELECT id, eliminado, nombre, apellido, dni, email,
                   fecha_ingreso, area, created_at, updated_at
            FROM empleado
            WHERE dni = ?
            """;

    private static final String SELECT_NO_ELIMINADOS_SQL = """
            SELECT id, eliminado, nombre, apellido, dni, email,
                   fecha_ingreso, area, created_at, updated_at
            FROM empleado
            WHERE eliminado = FALSE
            """;

    // Métodos de GenericDao<Empleado>

    @Override
    public void crear(Empleado entidad) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellido());
            ps.setString(3, entidad.getDni());
            ps.setString(4, entidad.getEmail());

            LocalDate fechaIngreso = entidad.getFechaIngreso();
            if (fechaIngreso != null) {
                ps.setDate(5, Date.valueOf(fechaIngreso));
            } else {
                ps.setNull(5, Types.DATE);
            }

            ps.setString(6, entidad.getArea());

            ps.executeUpdate();

            // obtener id generado
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear empleado", e);
        }
    }

    @Override
    public Empleado leer(long id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearEmpleado(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al leer empleado por id", e);
        }
        return null;
    }

    @Override
    public List<Empleado> leerTodos() {
        List<Empleado> empleados = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                empleados.add(mapearEmpleado(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al leer todos los empleados", e);
        }

        return empleados;
    }

    @Override
    public void actualizar(Empleado entidad) {
        if (entidad.getId() == null) {
            throw new IllegalArgumentException("El empleado debe tener id para ser actualizado");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellido());
            ps.setString(3, entidad.getDni());
            ps.setString(4, entidad.getEmail());

            LocalDate fechaIngreso = entidad.getFechaIngreso();
            if (fechaIngreso != null) {
                ps.setDate(5, Date.valueOf(fechaIngreso));
            } else {
                ps.setNull(5, Types.DATE);
            }

            ps.setString(6, entidad.getArea());
            ps.setLong(7, entidad.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar empleado", e);
        }
    }

    @Override
    public void eliminar(long id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_LOGICO_SQL)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar (baja lógica) empleado", e);
        }
    }

    // Métodos extra definidos en EmpleadoDao

    @Override
    public Empleado buscarPorDni(String dni) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_DNI_SQL)) {

            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearEmpleado(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar empleado por DNI", e);
        }
        return null;
    }

    @Override
    public List<Empleado> buscarNoEliminados() {
        List<Empleado> empleados = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_NO_ELIMINADOS_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                empleados.add(mapearEmpleado(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar empleados no eliminados", e);
        }

        return empleados;
    }

    // Método helper para mapear ResultSet -> Empleado
    private Empleado mapearEmpleado(ResultSet rs) throws SQLException {
        Empleado e = new Empleado();

        e.setId(rs.getLong("id"));
        e.setEliminado(rs.getBoolean("eliminado"));
        e.setNombre(rs.getString("nombre"));
        e.setApellido(rs.getString("apellido"));
        e.setDni(rs.getString("dni"));
        e.setEmail(rs.getString("email"));

        Date fechaIngreso = rs.getDate("fecha_ingreso");
        if (fechaIngreso != null) {
            e.setFechaIngreso(fechaIngreso.toLocalDate());
        }

        e.setArea(rs.getString("area"));

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) {
            e.setCreatedAt(created.toLocalDateTime());
        }

        Timestamp updated = rs.getTimestamp("updated_at");
        if (updated != null) {
            e.setUpdatedAt(updated.toLocalDateTime());
        }

        return e;
    }
}
