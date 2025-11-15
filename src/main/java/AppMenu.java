package main;

import entities.Empleado;
import entities.Legajo;
import service.EmpleadoService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class AppMenu {
    private final Scanner sc = new Scanner(System.in);
    private final EmpleadoService empleadoService = new EmpleadoService();

    public void start() {
        String op;
        do {
            System.out.println("""
                    ===== MENÚ EMPLEADO–LEGAJO =====
                    [1] Crear empleado
                    [2] Crear empleado + legajo (transaccional)
                    [3] Listar empleados
                    [4] Buscar empleado por ID
                    [5] Actualizar empleado
                    [6] Eliminar (baja lógica) empleado
                    [0] Salir
                    """);
            System.out.print("Opción: ");
            op = sc.nextLine().trim().toUpperCase();
            try {
                switch (op) {
                    case "1" -> crearEmpleado();
                    case "2" -> crearEmpleadoConLegajo();
                    case "3" -> listarEmpleados();
                    case "4" -> buscarPorId();
                    case "5" -> actualizarEmpleado();
                    case "6" -> eliminarEmpleado();
                    case "0" -> System.out.println("Saliendo...");
                    default -> System.out.println("Opción inválida");
                }
            } catch (Exception e) {
                System.err.println("ERROR: " + e.getMessage());
            }
        } while (!op.equals("0"));
    }

    private void crearEmpleado() throws Exception {
        Empleado e = new Empleado();
        System.out.print("DNI: "); e.setDni(sc.nextLine().trim());
        System.out.print("Nombre: "); e.setNombre(sc.nextLine().trim());
        System.out.print("Apellido: "); e.setApellido(sc.nextLine().trim());
        e.setEliminado(false);
        Empleado creado = empleadoService.insertar(e);
        System.out.println("Creado: " + creado);
    }

    private void crearEmpleadoConLegajo() throws Exception {
        Empleado e = new Empleado();
        System.out.print("DNI: "); e.setDni(sc.nextLine().trim());
        System.out.print("Nombre: "); e.setNombre(sc.nextLine().trim());
        System.out.print("Apellido: "); e.setApellido(sc.nextLine().trim());
        e.setEliminado(false);

        Legajo l = new Legajo();
        System.out.print("Número de legajo: "); l.setNumero(sc.nextLine().trim());
        System.out.print("Categoría: "); l.setCategoria(sc.nextLine().trim());
        l.setFechaAlta(LocalDate.now());
        l.setEliminado(false);

        Empleado creado = empleadoService.crearEmpleadoConLegajo(e, l);
        System.out.println("Creado con legajo: " + creado);
    }

    private void listarEmpleados() throws Exception {
        List<Empleado> lista = empleadoService.getAll();
        if (lista.isEmpty()) System.out.println("Sin empleados.");
        else lista.forEach(System.out::println);
    }

    private void buscarPorId() throws Exception {
        System.out.print("ID: ");
        long id = Long.parseLong(sc.nextLine());
        Empleado e = empleadoService.getById(id);
        System.out.println(e != null ? e : "No encontrado.");
    }

    private void actualizarEmpleado() throws Exception {
        System.out.print("ID a actualizar: ");
        long id = Long.parseLong(sc.nextLine());
        Empleado actual = empleadoService.getById(id);
        if (actual == null) { System.out.println("No existe."); return; }

        System.out.print("Nuevo DNI ("+actual.getDni()+"): ");
        String dni = sc.nextLine().trim();
        System.out.print("Nuevo Nombre ("+actual.getNombre()+"): ");
        String nom = sc.nextLine().trim();
        System.out.print("Nuevo Apellido ("+actual.getApellido()+"): ");
        String ape = sc.nextLine().trim();

        if (!dni.isBlank()) actual.setDni(dni);
        if (!nom.isBlank()) actual.setNombre(nom);
        if (!ape.isBlank()) actual.setApellido(ape);

        Empleado upd = empleadoService.actualizar(actual);
        System.out.println("Actualizado: " + upd);
    }

    private void eliminarEmpleado() throws Exception {
        System.out.print("ID a eliminar: ");
        long id = Long.parseLong(sc.nextLine());
        boolean ok = empleadoService.eliminar(id);
        System.out.println(ok ? "Baja lógica exitosa." : "No se pudo eliminar.");
    }
}
