package org.example.generatorarchivos;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class GenerateInfoFiles {

    private static final String[] FIRST_NAMES = {"Juan", "María", "Pedro", "Ana", "Luis", "Laura"};
    private static final String[] LAST_NAMES = {"Pérez", "Gómez", "Rodríguez", "López", "Martínez", "García"};
    private static final String[] PRODUCT_NAMES = {"Camiseta", "Pantalón", "Zapatos", "Chaqueta", "Gorra", "Calcetines"};

    private static List<Long> salesmanIds = new ArrayList<>();
    private static List<Long> productIds = new ArrayList<>();

    public static void createVendorsInfoFile(int salesmanCount, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            Random random = new Random();
            for (int i = 0; i < salesmanCount; i++) {
                long id = 1000 + i;
                String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
                String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
                writer.write(id + ";" + firstName + " " + lastName);
                writer.newLine();
                salesmanIds.add(id);
            }
        }
    }

    public static void createProductsFile(int productsCount, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            Random random = new Random();
            for (int i = 0; i < productsCount; i++) {
                long id = 2000 + i;
                String productName = PRODUCT_NAMES[random.nextInt(PRODUCT_NAMES.length)] + i;
                double price = 10 + (100 - 10) * random.nextDouble();
                writer.write(id + ";" + productName + ";" + String.format(Locale.US, "%.2f", price));
                writer.newLine();
                productIds.add(id);
            }
        }
    }

    public static void createSalesFile(int salesCount, String fileName, List<Long> salesmanIds, List<Long> productIds) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            Random random = new Random();
            for (int i = 0; i < salesCount; i++) {
                long salesmanId = salesmanIds.get(random.nextInt(salesmanIds.size()));
                long productId = productIds.get(random.nextInt(productIds.size()));
                int quantity = 1 + random.nextInt(10);
                writer.write(salesmanId + ";" + productId + ";" + quantity);
                writer.newLine();
            }
        }
    }

    public static void createSalesReport(String salesFile, String vendorsFile, String productsFile, String vendorReportFile, String productReportFile) throws IOException {
        Map<Long, String> vendorNames = Files.lines(Paths.get(vendorsFile))
                .map(line -> line.split(";"))
                .collect(Collectors.toMap(parts -> Long.parseLong(parts[0]), parts -> parts[1]));

        Map<Long, double[]> productData = Files.lines(Paths.get(productsFile))
                .map(line -> line.split(";"))
                .collect(Collectors.toMap(parts -> Long.parseLong(parts[0]),
                        parts -> new double[]{Double.parseDouble(parts[2]), 0})); // [price, quantitySold]

        Map<Long, Double> salesByVendor = new HashMap<>();

        Files.lines(Paths.get(salesFile)).forEach(line -> {
            String[] parts = line.split(";");
            long vendorId = Long.parseLong(parts[0]);
            long productId = Long.parseLong(parts[1]);
            int quantity = Integer.parseInt(parts[2]);

            double price = productData.get(productId)[0];
            productData.get(productId)[1] += quantity;

            salesByVendor.put(vendorId, salesByVendor.getOrDefault(vendorId, 0.0) + quantity * price);
        });

        List<Map.Entry<Long, Double>> sortedSalesByVendor = new ArrayList<>(salesByVendor.entrySet());
        sortedSalesByVendor.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(vendorReportFile))) {
            for (Map.Entry<Long, Double> entry : sortedSalesByVendor) {
                String name = vendorNames.get(entry.getKey());
                writer.write(name + ";" + String.format(Locale.US, "%.2f", entry.getValue()));
                writer.newLine();
            }
        }

        List<Map.Entry<Long, double[]>> sortedProducts = new ArrayList<>(productData.entrySet());
        sortedProducts.sort((e1, e2) -> Double.compare(e2.getValue()[1], e1.getValue()[1]));

        Map<Long, String> productNames = Files.lines(Paths.get(productsFile))
                .map(line -> line.split(";"))
                .collect(Collectors.toMap(parts -> Long.parseLong(parts[0]), parts -> parts[1]));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(productReportFile))) {
            for (Map.Entry<Long, double[]> entry : sortedProducts) {
                long productId = entry.getKey();
                String name = productNames.get(productId);
                double price = entry.getValue()[0];
                writer.write(name + ";" + String.format(Locale.US, "%.2f", price));
                writer.newLine();
            }
        }
    }

    public static void createSalesVendorFile(int randomSalesCount, String name, long id, String fileName, List<Long> productIds) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            Random random = new Random();
            for (int i = 0; i < randomSalesCount; i++) {
                long productId = productIds.get(random.nextInt(productIds.size()));
                int quantity = 1 + random.nextInt(10);
                writer.write(id + ";" + productId + ";" + quantity);
                writer.newLine();
            }
        }
    }
    public static List<Long> getSalesmanIds() {
        return salesmanIds;
    }

    public static List<Long> getProductIds() {
        return productIds;
    }

}
