package dao.impl;

import dao.EmpleadoDao;
import entities.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmpleadoDaoImpl implements EmpleadoDao {

    @Override
    public Empleado crear(Empleado e, Connection conn) throws Exception {
        String sql = "INSERT INTO empleado (dni, nombre, apellido, eliminado) VALUES (?,?,?,0)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getDni());
            ps.setString(2, e.getNombre());
            ps.setString(3, e.getApellido());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) e.setId(rs.getLong(1));
            }
        }
        return e;
    }

    @Override
    public Empleado leer(long id, Connection conn) throws Exception {
        String sql = "SELECT id,dni,nombre,apellido,eliminado FROM empleado WHERE id=? AND eliminado=0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    @Override
    public List<Empleado> leerTodos(Connection conn) throws Exception {
        String sql = "SELECT id,dni,nombre,apellido,eliminado FROM empleado WHERE eliminado=0";
        List<Empleado> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        }
        return out;
    }

    @Override
    public Empleado actualizar(Empleado e, Connection conn) throws Exception {
        String sql = "UPDATE empleado SET dni=?, nombre=?, apellido=? WHERE id=? AND eliminado=0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getDni());
            ps.setString(2, e.getNombre());
            ps.setString(3, e.getApellido());
            ps.setLong(4, e.getId());
            ps.executeUpdate();
        }
        return e;
    }

    @Override
    public boolean eliminar(long id, Connection conn) throws Exception {
        String sql = "UPDATE empleado SET eliminado=1 WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Empleado> buscarPorDni(String dni, Connection conn) throws Exception {
        String sql = "SELECT id,dni,nombre,apellido,eliminado FROM empleado WHERE dni=? AND eliminado=0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    private Empleado map(ResultSet rs) throws Exception {
        Empleado e = new Empleado();
        e.setId(rs.getLong("id"));
        e.setDni(rs.getString("dni"));
        e.setNombre(rs.getString("nombre"));
        e.setApellido(rs.getString("apellido"));
        e.setEliminado(rs.getBoolean("eliminado"));
        e.setDetalle(null); // se completa en Service si hace falta
        return e;
    }
}

