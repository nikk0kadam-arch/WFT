package com.ems.util;

import com.ems.exception.InvalidEmployeeDataException;
import com.ems.factory.EmployeeFactory;
import com.ems.model.Employee;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all file I/O for employee records using the NIO.2 API
 * (java.nio.file.Files / Path), demonstrating both bulk reads and
 * bulk writes plus basic exception handling around I/O.
 *
 * File format (pipe-delimited, one employee per line):
 *   TYPE|id|name|age|department|joiningDate(ISO-8601)|amount1|amount2
 */
public class EmployeeFileUtil {

    private EmployeeFileUtil() {
    }

    public static List<Employee> readEmployees(Path filePath) {
        List<Employee> result = new ArrayList<>();

        if (!Files.exists(filePath)) {
            System.out.println("[INFO] Data file not found at " + filePath + " — starting with an empty repository.");
            return result;
        }

        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            for (String rawLine : lines) {
                String line = rawLine.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // skip blank lines / comments
                }
                try {
                    result.add(parseLine(line));
                } catch (Exception parseError) {
                    System.err.println("[WARN] Skipping malformed line: \"" + line + "\" (" + parseError.getMessage() + ")");
                }
            }
        } catch (IOException e) {
            throw new InvalidEmployeeDataException("Failed to read employee file: " + filePath, e);
        }

        return result;
    }

    private static Employee parseLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 8) {
            throw new InvalidEmployeeDataException("Expected 8 fields, found " + parts.length);
        }

        String type = parts[0].trim();
        int id = Integer.parseInt(parts[1].trim());
        String name = parts[2].trim();
        int age = Integer.parseInt(parts[3].trim());
        String department = parts[4].trim();
        LocalDate joiningDate = LocalDate.parse(parts[5].trim());
        double amount1 = Double.parseDouble(parts[6].trim());
        double amount2 = Double.parseDouble(parts[7].trim());

        return EmployeeFactory.createEmployee(type, id, name, age, department, joiningDate, amount1, amount2);
    }

    public static void writeEmployees(Path filePath, List<Employee> employees) {
        List<String> lines = new ArrayList<>();
        lines.add("# type|id|name|age|department|joiningDate|amount1|amount2");
        for (Employee e : employees) {
            lines.add(e.toFileLine());
        }

        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            Files.write(filePath, lines, StandardCharsets.UTF_8);
            System.out.println("[INFO] Wrote " + employees.size() + " employee record(s) to " + filePath);
        } catch (IOException e) {
            throw new InvalidEmployeeDataException("Failed to write employee file: " + filePath, e);
        }
    }
}
