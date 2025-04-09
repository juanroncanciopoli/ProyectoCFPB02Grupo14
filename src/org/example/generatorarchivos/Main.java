package org.example.generatorarchivos;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int option;
        do {
            showMenu();
            option = readOption();

            try {
                executeOption(option);
            } catch (IOException e) {
                System.err.println("Error de entrada/salida: " + e.getMessage());
            }
        } while (option != 6);

        System.out.println("Programa finalizado.");
    }

    private static void showMenu() {
        System.out.println("\n=== GENERADOR DE ARCHIVOS ===");
        System.out.println("1. Generar archivo de vendedores");
        System.out.println("2. Generar archivo de productos");
        System.out.println("3. Generar archivos de vendedores y productos");
        System.out.println("4. Generar reporte de ventas");
        System.out.println("5. Generar todos los archivos");
        System.out.println("6. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static int readOption() {
        while (!scanner.hasNextInt()) {
            System.out.print("Por favor, ingrese un número válido: ");
            scanner.next(); // Descartar entrada inválida
        }
        int option = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer
        return option;
    }

    private static void executeOption(int option) throws IOException {
        switch (option) {
            case 1:
                GenerateInfoFiles.createVendorsInfoFile(10, "vendedores.csv");
                System.out.println("Archivo vendedores generado correctamente!");
                break;
            case 2:
                GenerateInfoFiles.createProductsFile(15, "productos.csv");
                System.out.println("Archivo productos generado correctamente!");

                break;
            case 3:
                GenerateInfoFiles.createVendorsInfoFile(10, "vendedores.csv");
                GenerateInfoFiles.createProductsFile(15, "productos.csv");
                GenerateInfoFiles.createSalesFile(20, "ventas.csv", GenerateInfoFiles.getSalesmanIds(), GenerateInfoFiles.getProductIds());
                System.out.println("Archivos de vendedores y productos generados correctamente!");


                break;
            case 4:
                GenerateInfoFiles.createSalesReport("ventas.csv", "vendedores.csv", "productos.csv", "reporte_vendedores.csv", "reporte_productos.csv");
                System.out.println("Archivo de reporte de ventas generado correctamente!");

                break;
            case 5:
                GenerateInfoFiles.createVendorsInfoFile(10, "vendedores.csv");
                GenerateInfoFiles.createProductsFile(15, "productos.csv");
                // Asumimos que el número de ventas es 20 para este ejemplo
                GenerateInfoFiles.createSalesFile(20, "ventas.csv", GenerateInfoFiles.getSalesmanIds(), GenerateInfoFiles.getProductIds());
                GenerateInfoFiles.createSalesReport("ventas.csv", "vendedores.csv", "productos.csv", "reporte_vendedores.csv", "reporte_productos.csv");
                break;
            case 6:
                System.out.println("Saliendo del sistema...");
                break;
            default:
                System.out.println("Opción no válida. Intente nuevamente.");
        }
    }
}
