package tfi.empleadolegajo.app;

import tfi.empleadolegajo.entities.Empleado;
import tfi.empleadolegajo.entities.Legajo;
import tfi.empleadolegajo.service.impl.EmpleadoServiceImpl;

import java.time.LocalDate;

public class TestServicios {

    public static void main(String[] args) {

        EmpleadoServiceImpl service = new EmpleadoServiceImpl();

        System.out.println("===== TEST 1: Crear empleado + legajo OK =====");
        Empleado emp1 = crearEmpleadoDummy("44556677", "Juan", "Prueba", "juan.prueba@test.com", "Sistemas");
        Legajo leg1 = crearLegajoDummy("L-900", "SR", "ACTIVO", "Legajo de prueba 1");

        try {
            service.crearEmpleadoConLegajo(emp1, leg1);
            System.out.println("OK: Empleado creado con ID: " + emp1.getId());
            System.out.println("     Legajo asociado numero: " + leg1.getNroLegajo());
        } catch (RuntimeException e) {
            System.out.println("Error en TEST 1: " + e.getMessage());
        }

        System.out.println("\n===== TEST 2: DNI repetido (debe fallar) =====");
        Empleado emp2 = crearEmpleadoDummy("44556677", "Pedro", "Duplicado", "pedro.dup@test.com", "RRHH");
        Legajo leg2 = crearLegajoDummy("L-901", "JR", "ACTIVO", "Legajo 2");

        try {
            service.crearEmpleadoConLegajo(emp2, leg2);
            System.out.println("ERROR: Se creo un empleado con DNI repetido.");
        } catch (RuntimeException e) {
            System.out.println("OK: Se detecto DNI repetido: " + e.getMessage());
        }

        System.out.println("\n===== TEST 3: Numero de legajo repetido (debe fallar) =====");
        Empleado emp3 = crearEmpleadoDummy("55667788", "Luis", "LegajoRep", "luis.leg@test.com", "Administracion");
        Legajo leg3 = crearLegajoDummy("L-900", "JR", "ACTIVO", "Legajo repetido");

        try {
            service.crearEmpleadoConLegajo(emp3, leg3);
            System.out.println("ERROR: Se creo un legajo con numero repetido.");
        } catch (RuntimeException e) {
            System.out.println("OK: Se detecto numero de legajo repetido: " + e.getMessage());
        }

        System.out.println("\n===== TEST 4: Eliminar empleado + legajo =====");
        if (emp1.getId() != null) {
            try {
                service.eliminarEmpleadoYLegajo(emp1.getId());
                System.out.println("OK: Empleado y legajo eliminados logicamente.");
            } catch (RuntimeException e) {
                System.out.println("Error al eliminar empleado y legajo: " + e.getMessage());
            }
        } else {
            System.out.println("No se pudo probar eliminacion porque emp1 no tiene ID.");
        }

        System.out.println("\n===== FIN DE PRUEBAS =====");
    }

    // ----------------- Helpers -----------------

    private static Empleado crearEmpleadoDummy(String dni, String nombre, String apellido,
                                               String email, String area) {
        Empleado e = new Empleado();
        e.setDni(dni);
        e.setNombre(nombre);
        e.setApellido(apellido);
        e.setEmail(email);
        e.setArea(area);
        e.setFechaIngreso(LocalDate.now());
        e.setEliminado(false);
        return e;
    }

    private static Legajo crearLegajoDummy(String nroLegajo, String categoria,
                                           String estado, String observaciones) {
        Legajo l = new Legajo();
        l.setNroLegajo(nroLegajo);
        l.setCategoria(categoria);
        l.setEstado(estado);
        l.setFechaAlta(LocalDate.now());
        l.setObservaciones(observaciones);
        l.setEliminado(false);
        return l;
    }
}
