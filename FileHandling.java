package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * File Handling Utility Program
 *
 * This program demonstrates comprehensive file operations including:
 * - Reading text files
 * - Writing text files
 * - Modifying existing files
 * - Error handling and validation
 *
 * @author File Handling Utility
 * @version 1.0
 */
public class FileHandling {
    private static final String DEFAULT_DIRECTORY = "files/";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== FILE HANDLING UTILITY ===");
        System.out.println("Demonstrating file operations in Java\n");

        // Create directory if it doesn't exist
        createDirectory(DEFAULT_DIRECTORY);

        // Main menu loop
        while (true) {
            displayMenu();
            int choice = getMenuChoice();

            switch (choice) {
                case 1:
                    demonstrateFileCreation();
                    break;
                case 2:
                    demonstrateFileReading();
                    break;
                case 3:
                    demonstrateFileModification();
                    break;
                case 4:
                    demonstrateFileAppending();
                    break;
                case 5:
                    listFiles();
                    break;
                case 6:
                    deleteFile();
                    break;
                case 7:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    /**
     * Display the main menu options
     */
    private static void displayMenu() {
        System.out.println("\n=== MENU ===");
        System.out.println("1. Create and Write to File");
        System.out.println("2. Read File Contents");
        System.out.println("3. Modify File Contents");
        System.out.println("4. Append to File");
        System.out.println("5. List All Files");
        System.out.println("6. Delete File");
        System.out.println("7. Exit");
        System.out.print("Enter your choice (1-7): ");
    }

    /**
     * Get user's menu choice with validation
     * @return valid menu choice
     */
    private static int getMenuChoice() {
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            return choice;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Create directory if it doesn't exist
     * @param dirPath directory path to create
     */
    private static void createDirectory(String dirPath) {
        try {
            Path path = Paths.get(dirPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("Created directory: " + dirPath);
            }
        } catch (IOException e) {
            System.err.println("Error creating directory: " + e.getMessage());
        }
    }

    /**
     * Demonstrate file creation and writing
     */
    private static void demonstrateFileCreation() {
        System.out.println("\n=== FILE CREATION DEMO ===");

        System.out.print("Enter filename: ");
        String filename = scanner.nextLine();

        if (filename.trim().isEmpty()) {
            System.out.println("Invalid filename.");
            return;
        }

        String filepath = DEFAULT_DIRECTORY + filename;

        System.out.println("Enter content (type 'END' on a new line to finish):");
        StringBuilder content = new StringBuilder();
        String line;

        while (!(line = scanner.nextLine()).equals("END")) {
            content.append(line).append("\n");
        }

        writeToFile(filepath, content.toString(), false);
    }

    /**
     * Write content to a file
     * @param filepath path to the file
     * @param content content to write
     * @param append whether to append or overwrite
     */
    private static void writeToFile(String filepath, String content, boolean append) {
        try {
            Path path = Paths.get(filepath);

            if (append) {
                Files.write(path, content.getBytes(), StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND);
                System.out.println("Successfully appended to file: " + filepath);
            } else {
                Files.write(path, content.getBytes(), StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING);
                System.out.println("Successfully created/updated file: " + filepath);
            }

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Demonstrate file reading
     */
    private static void demonstrateFileReading() {
        System.out.println("\n=== FILE READING DEMO ===");

        System.out.print("Enter filename to read: ");
        String filename = scanner.nextLine();
        String filepath = DEFAULT_DIRECTORY + filename;

        readFile(filepath);
    }

    /**
     * Read and display file contents
     * @param filepath path to the file to read
     */
    private static void readFile(String filepath) {
        try {
            Path path = Paths.get(filepath);

            if (!Files.exists(path)) {
                System.out.println("File not found: " + filepath);
                return;
            }

            List<String> lines = Files.readAllLines(path);

            System.out.println("\n--- FILE CONTENTS ---");
            System.out.println("File: " + filepath);
            System.out.println("Lines: " + lines.size());
            System.out.println("--- START ---");

            for (int i = 0; i < lines.size(); i++) {
                System.out.printf("%3d: %s%n", i + 1, lines.get(i));
            }

            System.out.println("--- END ---");

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Demonstrate file modification
     */
    private static void demonstrateFileModification() {
        System.out.println("\n=== FILE MODIFICATION DEMO ===");

        System.out.print("Enter filename to modify: ");
        String filename = scanner.nextLine();
        String filepath = DEFAULT_DIRECTORY + filename;

        if (!Files.exists(Paths.get(filepath))) {
            System.out.println("File not found: " + filepath);
            return;
        }

        // First, show current contents
        System.out.println("\nCurrent file contents:");
        readFile(filepath);

        System.out.println("\nModification options:");
        System.out.println("1. Replace specific line");
        System.out.println("2. Find and replace text");
        System.out.println("3. Insert line at position");
        System.out.println("4. Delete line");
        System.out.print("Choose option (1-4): ");

        int choice = getMenuChoice();

        switch (choice) {
            case 1:
                replaceLineInFile(filepath);
                break;
            case 2:
                findAndReplaceInFile(filepath);
                break;
            case 3:
                insertLineInFile(filepath);
                break;
            case 4:
                deleteLineFromFile(filepath);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    /**
     * Replace a specific line in the file
     * @param filepath path to the file
     */
    private static void replaceLineInFile(String filepath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filepath));

            System.out.print("Enter line number to replace (1-" + lines.size() + "): ");
            int lineNumber = Integer.parseInt(scanner.nextLine()) - 1;

            if (lineNumber < 0 || lineNumber >= lines.size()) {
                System.out.println("Invalid line number.");
                return;
            }

            System.out.println("Current line: " + lines.get(lineNumber));
            System.out.print("Enter new content: ");
            String newContent = scanner.nextLine();

            lines.set(lineNumber, newContent);

            Files.write(Paths.get(filepath), lines);
            System.out.println("Line replaced successfully!");

        } catch (IOException | NumberFormatException e) {
            System.err.println("Error modifying file: " + e.getMessage());
        }
    }

    /**
     * Find and replace text in the file
     * @param filepath path to the file
     */
    private static void findAndReplaceInFile(String filepath) {
        try {
            String content = Files.readString(Paths.get(filepath));

            System.out.print("Enter text to find: ");
            String findText = scanner.nextLine();

            System.out.print("Enter replacement text: ");
            String replaceText = scanner.nextLine();

            String modifiedContent = content.replace(findText, replaceText);

            Files.write(Paths.get(filepath), modifiedContent.getBytes());
            System.out.println("Find and replace completed!");

        } catch (IOException e) {
            System.err.println("Error modifying file: " + e.getMessage());
        }
    }

    /**
     * Insert a line at specific position
     * @param filepath path to the file
     */
    private static void insertLineInFile(String filepath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filepath));

            System.out.print("Enter position to insert (1-" + (lines.size() + 1) + "): ");
            int position = Integer.parseInt(scanner.nextLine()) - 1;

            if (position < 0 || position > lines.size()) {
                System.out.println("Invalid position.");
                return;
            }

            System.out.print("Enter text to insert: ");
            String newLine = scanner.nextLine();

            lines.add(position, newLine);

            Files.write(Paths.get(filepath), lines);
            System.out.println("Line inserted successfully!");

        } catch (IOException | NumberFormatException e) {
            System.err.println("Error modifying file: " + e.getMessage());
        }
    }

    /**
     * Delete a line from the file
     * @param filepath path to the file
     */
    private static void deleteLineFromFile(String filepath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filepath));

            System.out.print("Enter line number to delete (1-" + lines.size() + "): ");
            int lineNumber = Integer.parseInt(scanner.nextLine()) - 1;

            if (lineNumber < 0 || lineNumber >= lines.size()) {
                System.out.println("Invalid line number.");
                return;
            }

            System.out.println("Deleting line: " + lines.get(lineNumber));
            lines.remove(lineNumber);

            Files.write(Paths.get(filepath), lines);
            System.out.println("Line deleted successfully!");

        } catch (IOException | NumberFormatException e) {
            System.err.println("Error modifying file: " + e.getMessage());
        }
    }

    /**
     * Demonstrate file appending
     */
    private static void demonstrateFileAppending() {
        System.out.println("\n=== FILE APPENDING DEMO ===");

        System.out.print("Enter filename to append to: ");
        String filename = scanner.nextLine();
        String filepath = DEFAULT_DIRECTORY + filename;

        if (!Files.exists(Paths.get(filepath))) {
            System.out.println("File not found. Creating new file.");
        }

        System.out.println("Enter content to append (type 'END' on a new line to finish):");
        StringBuilder content = new StringBuilder();
        String line;

        while (!(line = scanner.nextLine()).equals("END")) {
            content.append(line).append("\n");
        }

        writeToFile(filepath, content.toString(), true);
    }

    /**
     * List all files in the directory
     */
    private static void listFiles() {
        System.out.println("\n=== FILE LIST ===");

        try {
            Path directory = Paths.get(DEFAULT_DIRECTORY);

            if (!Files.exists(directory)) {
                System.out.println("Directory not found: " + DEFAULT_DIRECTORY);
                return;
            }

            List<Path> files = Files.list(directory)
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());

            if (files.isEmpty()) {
                System.out.println("No files found in " + DEFAULT_DIRECTORY);
                return;
            }

            System.out.println("Files in " + DEFAULT_DIRECTORY + ":");
            for (int i = 0; i < files.size(); i++) {
                Path file = files.get(i);
                long size = Files.size(file);
                String modified = Files.getLastModifiedTime(file).toString();

                System.out.printf("%3d. %-20s (Size: %d bytes, Modified: %s)%n",
                        i + 1, file.getFileName(), size, modified);
            }

        } catch (IOException e) {
            System.err.println("Error listing files: " + e.getMessage());
        }
    }

    /**
     * Delete a file
     */
    private static void deleteFile() {
        System.out.println("\n=== FILE DELETION ===");

        System.out.print("Enter filename to delete: ");
        String filename = scanner.nextLine();
        String filepath = DEFAULT_DIRECTORY + filename;

        try {
            Path path = Paths.get(filepath);

            if (!Files.exists(path)) {
                System.out.println("File not found: " + filepath);
                return;
            }

            System.out.print("Are you sure you want to delete '" + filename + "'? (y/N): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("y") || confirmation.equalsIgnoreCase("yes")) {
                Files.delete(path);
                System.out.println("File deleted successfully: " + filepath);
            } else {
                System.out.println("File deletion cancelled.");
            }

        } catch (IOException e) {
            System.err.println("Error deleting file: " + e.getMessage());
        }
    }
}
