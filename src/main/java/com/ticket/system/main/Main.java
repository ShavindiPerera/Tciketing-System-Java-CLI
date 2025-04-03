package com.ticket.system.main;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;
import java.util.logging.*;


import com.ticket.system.configuration.Configuration;


/**
 * Main class for the Ticketing System application.
 * Handles system setup, user interaction, and simulation of ticket vending
 */

public class Main {
    // Logger instance for logging events and errors
    private static final Logger logger = Logger.getLogger(Main.class.getName());


    // Configuration object to store system settings
    private static  Configuration config = new Configuration();

    // Ticket pool shared between vendors and customers
    private static TicketPool ticketPool;




    /**
     * Entry point for the application.
     * Sets up the logger and initiates the menu and simulation.
     */
    public static void main(String[] args) {

        setupLogger();
        menu();
        simulation();
    }

    /**
     * Configures the logger to log messages to both the console and a file.
     */
    private static void setupLogger() {

        // Remove default handlers to avoid duplicate logging
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }


        // Add a file handler to log messages to a file
        try {
            FileHandler fileHandler = new FileHandler("application.log", true); // Appends to the log file
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Failed to set up file logging: " + e.getMessage());
        }

        // Set the logger level to capture all messages
        logger.setLevel(Level.ALL);
    }

    /**
     * Displays a menu for creating or loading configuration settings.
     * Handles user input for configuring the system.
     */
    private static void menu() {
        Scanner scanner = new Scanner(System.in);


        System.out.println("Welcome to the Ticketing System!");
        System.out.println("Do you want to create a new configuration?");
        System.out.println("-------------------------------------------------------");
        System.out.println("Enter \"YES\" to create new Configurations for the system ");
        System.out.println("Enter \"NO\" to load existing Configurations for the system ");
        System.out.println("-------------------------------------------------------");


        boolean validInput = false;

        while (!validInput) {
            System.out.print("Enter your choice : ");
            try {
                String input = scanner.nextLine().trim().toLowerCase();
                if (input.equals("yes")) {
                    logger.info("User chose to create new configuration.");
                    System.out.println("Starting fresh configuration setup.");
                    configureSystem();
                    saveConfiguration();
                    validInput = true;

                } else if (input.equals("no")) {
                    logger.info("User chose to load existing configuration.");
                    System.out.println("Loading existing system configuration");
                    loadConfiguration();

                    validInput = true;

                } else {
                    throw new IllegalArgumentException("Invalid input. Please enter only \"YES\" or \"NO\".");
                }
            } catch (IllegalArgumentException e) {
                logger.warning("Invalid input: " + e.getMessage());
                System.out.println(e.getMessage());

            }
        }
    }

    /**
     * Validates integer input from the user within a specific range.
     * @param scanner Scanner instance for user input
     * @param prompt Prompt message for the input
     * @param min Minimum acceptable value
     * @param max Maximum acceptable value
     * @return Validated integer value
     */
    private static int inputValidation(Scanner scanner,String prompt,  int min, int max) {
        int value;
        while (true) {
            System.out.print(prompt);
            try {
                value = Integer.parseInt(scanner.nextLine());
                if (value >= min && value <= max) {
                    break;
                } else {
                    logger.warning("Invalid range input: " + value);
                    System.out.println("Please enter a value between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                logger.warning("Invalid number format: " + e.getMessage());
                System.out.println("Please enter numbers only!.");
            }

        }
        return value;
    }

    /**
     * Configures the system by prompting the user.
     */
    private static void configureSystem() {
        Scanner scanner = new Scanner(System.in);

        config.setTotalTickets(inputValidation(scanner, "Enter total number of tickets:",1, Integer.MAX_VALUE));
        config.setTicketReleaseRate(inputValidation(scanner, "Enter ticket release rate:",10, 10000));
        config.setCustomerRetrievalRate(inputValidation(scanner, "Enter customer retrieval rate:", 10, 10000));
        config.setMaxTicketCapacity(inputValidation(scanner, "Enter maximum ticket capacity:", config.getTotalTickets(),Integer.MAX_VALUE ));


    }



    /**
     * Saves the current configuration to a JSON file.
     */
    private static void saveConfiguration() {
        try (Writer writer = new FileWriter("config.json")) {
            Gson gson = new Gson();
            gson.toJson(config, writer);
            logger.info("Configuration saved successfully.");
            System.out.println("New Configuration saved successfully.");
        } catch (IOException e) {
            logger.severe("Configuration not saved: " + e.getMessage());
            System.out.println("Configuration not saved " + e.getMessage());
        }
    }

    /**
     * Loads the configuration from a JSON file.
     * @return Configuration object loaded from the file
     */
    public static Configuration loadConfiguration() {
        Gson gson = new Gson();
        Configuration config = null; // To store the loaded configuration
        try (FileReader reader = new FileReader("config.json")) {
            config = gson.fromJson(reader, Configuration.class);
            logger.info("Configuration loaded successfully.");
            System.out.println("Configuration successfully loaded.");
            Main.ticketPool = new TicketPool(config.getMaxTicketCapacity());
        } catch (IOException e) {
            logger.severe("Failed to load configuration: " + e.getMessage());
            System.err.println("Failed to load configuration: " + e.getMessage());
            e.printStackTrace();
        }
        return config; // Return the configuration object for further use
    }



    /**
     * Starts the simulation loop, handling user commands to start or stop the system.
     */
    private static void simulation() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter:  'start' to begin and 'stop' to exit.");

        while (true) {
            System.out.print("Your choice: ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "start":
                    logger.info("Starting the ticket handling process.");
                    ticketHandling();

                    break;

                case "stop":
                    logger.info("Stopping the system.");
                    System.out.println("System Stopping...");
                    saveConfiguration();
                    System.exit(0);
                    break;
                default:
                    logger.warning("Unknown command entered: " + command);
                    System.out.println("Unknown command. Please try 'start' or 'stop'");
            }
        }
    }



    /**
     * Handles the ticket vending and customer ticket retrieval process using threads.
     */
    private static void ticketHandling() {

        // Initialize the ticket pool with the maximum ticket capacity
       TicketPool ticketPool = new TicketPool(config.getMaxTicketCapacity());

        // Create and start vendor threads
       Vendor [] vendors = new Vendor[100];
       for (int i = 1; i < vendors.length; i++){
           vendors[i]  = new Vendor(ticketPool, config.getTotalTickets(), config.getTicketReleaseRate());
           Thread vendorThread = new Thread(vendors[i], "Vendor " + i);
           vendorThread.start();
           logger.info("Vendor " + i + " thread started.");
        }

       // Create and start customer threads
       Customer [] customers = new Customer[100];
       for( int i = 1; i < customers.length; i++){
           customers[i] = new Customer(ticketPool, config.getCustomerRetrievalRate(), 10);
           Thread customerThread = new Thread(customers[i], "Customer " +i);
           customerThread.start();
           logger.info("Customer " + i + " thread started.");
        }
    }
}
