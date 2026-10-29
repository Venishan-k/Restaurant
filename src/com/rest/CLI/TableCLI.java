package com.rest.CLI;
import com.rest.models.Table;
import com.rest.service.TableService;
import java.util.List;
import java.util.Scanner;

public class TableCLI{
    private final TableService service;
    private final Scanner scanner;
    private final boolean ownsScanner;

    public TableCLI(TableService service){
        this(service, new Scanner(System.in), true);
    }

    public TableCLI(TableService service, Scanner scanner){
        this(service, scanner, false);
    }

    private TableCLI(TableService service, Scanner scanner, boolean ownsScanner){
        this.service = service;
        this.scanner = scanner;
        this.ownsScanner = ownsScanner;
    }

    public void start(){
        boolean running = true;
        while (running) {
            System.out.println("\n=== RESTAURANT TABLE MANAGEMENT ===");
            System.out.println("1. View All Tables");
            System.out.println("0. Return / Exit");

            System.out.print("Enter choice: ");
            String input = scanner.nextLine().trim();

            if("1".equals(input)){
                List<Table> tables = service.getAllTables();
                System.out.println("\n=== ALL TABLES ===");

                for (Table table : tables) {
                    System.out.println("Table ID: " + table.getId()
                            + ", Capacity: " + table.getcapacity()
                            + ", Status: " + table.getStatus());
                }
            }
            else if("0".equals(input)){
                System.out.println("Returning...");
                running = false;
            }
            else{
                System.out.println("Invalid choice.");
            }
        }
        if (ownsScanner) {
            scanner.close();
        }
    }
}