package tfi.empleadolegajo.dao.impl;

import tfi.empleadolegajo.config.DatabaseConnection;
import tfi.empleadolegajo.dao.LegajoDao;
import tfi.empleadolegajo.entities.Legajo;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LegajoDaoImpl implements LegajoDao {

    private static final String INSERT_SQL = """
            INSERT INTO legajo (nro_legajo, categoria, estado, fecha_alta, observaciones, empleado_id)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    private static final String SELECT_BY_ID_SQL = """
            SELECT id, eliminado, nro_legajo, categoria, estado,
                   fecha_alta, observaciones, empleado_id,
                   created_at, updated_at
            FROM legajo
            WHERE id = ?
            """;

    private static final String SELECT_ALL_SQL = """
            SELECT id, eliminado, nro_legajo, categoria, estado,
                   fecha_alta, observaciones, empleado_id,
                   created_at, updated_at
            FROM legajo
            """;

    private static final String UPDATE_SQL = """
            UPDATE legajo
            SET nro_legajo = ?, categoria = ?, estado = ?, fecha_alta = ?,
                observaciones = ?, empleado_id = ?, updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;

    private static final String DELETE_LOGICO_SQL = """
            UPDATE legajo
            SET eliminado = TRUE, updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;

    private static final String SELECT_BY_NRO_SQL = """
            SELECT id, eliminado, nro_legajo, categoria, estado,
                   fecha_alta, observaciones, empleado_id,
                   created_at, updated_at
            FROM legajo
            WHERE nro_legajo = ?
            """;

    private static final String SELECT_BY_EMPLEADO_ID = """
            SELECT id, eliminado, nro_legajo, categoria, estado,
                   fecha_alta, observaciones, empleado_id,
                   created_at, updated_at
            FROM legajo
            WHERE empleado_id = ?
            """;

    // Métodos de GenericDao<Legajo>

    @Override
    public void crear(Legajo l) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, l.getNroLegajo());
            ps.setString(2, l.getCategoria());
            ps.setString(3, l.getEstado());

            if (l.getFechaAlta() != null) {
                ps.setDate(4, Date.valueOf(l.getFechaAlta()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            ps.setString(5, l.getObservaciones());
            ps.setLong(6, l.getEmpleadoId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    l.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear legajo", e);
        }
    }

    @Override
    public Legajo leer(long id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearLegajo(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al leer legajo por ID", e);
        }
        return null;
    }

    @Override
    public List<Legajo> leerTodos() {
        List<Legajo> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearLegajo(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al leer todos los legajos", e);
        }

        return lista;
    }

    @Override
    public void actualizar(Legajo l) {
        if (l.getId() == null) {
            throw new IllegalArgumentException("El legajo debe tener ID para actualizar");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, l.getNroLegajo());
            ps.setString(2, l.getCategoria());
            ps.setString(3, l.getEstado());

            if (l.getFechaAlta() != null) {
                ps.setDate(4, Date.valueOf(l.getFechaAlta()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            ps.setString(5, l.getObservaciones());
            ps.setLong(6, l.getEmpleadoId());
            ps.setLong(7, l.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar legajo", e);
        }
    }

    @Override
    public void eliminar(long id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_LOGICO_SQL)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar (baja lógica) legajo", e);
        }
    }

    // Métodos adicionales del DAO

    @Override
    public Legajo buscarPorNroLegajo(String nroLegajo) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_NRO_SQL)) {

            ps.setString(1, nroLegajo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearLegajo(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar legajo por nro", e);
        }
        return null;
    }

    @Override
    public Legajo buscarPorEmpleadoId(long empleadoId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_EMPLEADO_ID)) {

            ps.setLong(1, empleadoId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearLegajo(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar legajo por empleado id", e);
        }
        return null;
    }

    // Método de mapeo ResultSet → Legajo
    private Legajo mapearLegajo(ResultSet rs) throws SQLException {
        Legajo l = new Legajo();

        l.setId(rs.getLong("id"));
        l.setEliminado(rs.getBoolean("eliminado"));
        l.setNroLegajo(rs.getString("nro_legajo"));
        l.setCategoria(rs.getString("categoria"));
        l.setEstado(rs.getString("estado"));

        Date fechaAlta = rs.getDate("fecha_alta");
        if (fechaAlta != null) {
            l.setFechaAlta(fechaAlta.toLocalDate());
        }

        l.setObservaciones(rs.getString("observaciones"));
        l.setEmpleadoId(rs.getLong("empleado_id"));

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) {
            l.setCreatedAt(created.toLocalDateTime());
        }

        Timestamp updated = rs.getTimestamp("updated_at");
        if (updated != null) {
            l.setUpdatedAt(updated.toLocalDateTime());
        }

        return l;
    }
}
