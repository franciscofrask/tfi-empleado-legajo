package dao.impl;

import dao.LegajoDao;
import entities.Legajo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LegajoDaoImpl implements LegajoDao {

    @Override
    public Legajo crear(Legajo l, Connection conn) throws Exception {
        // Este crea DEBE recibir empleado_id correcto desde Service
        throw new UnsupportedOperationException("Usá crearConEmpleadoId(...) en su lugar.");
    }

    public Legajo crearConEmpleadoId(Legajo l, long empleadoId, Connection conn) throws Exception {
        String sql = "INSERT INTO legajo (numero, categoria, fecha_alta, eliminado, empleado_id) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, l.getNumero());
            ps.setString(2, l.getCategoria());
            ps.setDate(3, Date.valueOf(l.getFechaAlta()));
            ps.setBoolean(4, false);
            ps.setLong(5, empleadoId);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) l.setId(rs.getLong(1));
            }
        }
        return l;
    }

    @Override
    public Legajo leer(long id, Connection conn) throws Exception {
        String sql = "SELECT id,numero,categoria,fecha_alta,eliminado,empleado_id FROM legajo WHERE id=? AND eliminado=0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    @Override
    public List<Legajo> leerTodos(Connection conn) throws Exception {
        String sql = "SELECT id,numero,categoria,fecha_alta,eliminado,empleado_id FROM legajo WHERE eliminado=0";
        List<Legajo> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        }
        return out;
    }

    @Override
    public Legajo actualizar(Legajo l, Connection conn) throws Exception {
        String sql = "UPDATE legajo SET numero=?, categoria=?, fecha_alta=? WHERE id=? AND eliminado=0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, l.getNumero());
            ps.setString(2, l.getCategoria());
            ps.setDate(3, Date.valueOf(l.getFechaAlta()));
            ps.setLong(4, l.getId());
            ps.executeUpdate();
        }
        return l;
    }

    @Override
    public boolean eliminar(long id, Connection conn) throws Exception {
        String sql = "UPDATE legajo SET eliminado=1 WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Legajo> buscarPorNumero(String numero, Connection conn) throws Exception {
        String sql = "SELECT id,numero,categoria,fecha_alta,eliminado,empleado_id FROM legajo WHERE numero=? AND eliminado=0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public Optional<Legajo> buscarPorEmpleadoId(long empleadoId, Connection conn) throws Exception {
        String sql = "SELECT id,numero,categoria,fecha_alta,eliminado,empleado_id FROM legajo WHERE empleado_id=? AND eliminado=0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, empleadoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    private Legajo map(ResultSet rs) throws Exception {
        Legajo l = new Legajo();
        l.setId(rs.getLong("id"));
        l.setNumero(rs.getString("numero"));
        l.setCategoria(rs.getString("categoria"));
        l.setFechaAlta(rs.getDate("fecha_alta").toLocalDate());
        l.setEliminado(rs.getBoolean("eliminado"));
        return l;
    }
}
