package com.ems.app;

import com.ems.model.Employee;
import com.ems.repository.EmployeeRepository;
import com.ems.util.EmployeeFileUtil;

import java.nio.file.Path;
import java.util.List;

/**
 * Singleton Pattern: guarantees the initial employee data set is loaded
 * from disk exactly once per application run, no matter how many times
 * getInstance()/loadInitialData() is invoked.
 */
public final class DataLoader {

    private static DataLoader instance;

    private boolean alreadyLoaded = false;

    // Private constructor prevents external instantiation.
    private DataLoader() {
    }

    public static synchronized DataLoader getInstance() {
        if (instance == null) {
            instance = new DataLoader();
        }
        return instance;
    }

    /**
     * Loads employees from the given file into the repository.
     * Subsequent calls are no-ops, preserving the "load only once" guarantee.
     */
    public void loadInitialData(EmployeeRepository repository, Path dataFile) {
        if (alreadyLoaded) {
            System.out.println("[INFO] Initial data already loaded — skipping reload.");
            return;
        }

        List<Employee> loaded = EmployeeFileUtil.readEmployees(dataFile);
        repository.saveAll(loaded);
        alreadyLoaded = true;
        System.out.println("[INFO] Loaded " + loaded.size() + " employee record(s) from " + dataFile);
    }

    public boolean isAlreadyLoaded() {
        return alreadyLoaded;
    }
}
