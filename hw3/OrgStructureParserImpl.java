package hw3;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class OrgStructureParserImpl implements OrgStructureParser {

    public void fillEmployees(Map<Long, Employee> idToEmployee, File csvFile) {
        try (Scanner scanner = new Scanner(csvFile)) {
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                Employee employee = new Employee();
                String[] employeeData = scanner.nextLine().split(";"); // id;boss_id;name;position
                employee.setId(Long.valueOf(employeeData[0]));
                if (!Objects.equals(employeeData[1], "")) {
                    employee.setBossId(Long.valueOf(employeeData[1]));
                }
                employee.setName(employeeData[2]);
                employee.setPosition(employeeData[3]);
                idToEmployee.put(employee.getId(), employee);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void fillBosses(Map<Long, Employee> idToEmployee, File csvFile) {
        try (Scanner scanner = new Scanner(csvFile)) {
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String[] employeeData = scanner.nextLine().split(";"); // id;boss_id;name;position
                Long idCurrentEmployee = Long.parseLong(employeeData[0]);
                if (!Objects.equals(employeeData[1], "")) {
                    Long idBoss = Long.parseLong(employeeData[1]);
                    idToEmployee.get(idCurrentEmployee).setBoss(idToEmployee.get(idBoss));
                    idToEmployee.get(idBoss).getSubordinate().add(idToEmployee.get(idCurrentEmployee));
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Employee parseStructure(File csvFile) {
        Employee boss = null;
        Map<Long, Employee> idToEmployee = new HashMap<>();
        fillEmployees(idToEmployee, csvFile);
        fillBosses(idToEmployee, csvFile);
        for (Long id : idToEmployee.keySet()) {
            if (idToEmployee.get(id).getBossId() == null) {
                boss = idToEmployee.get(id);
            }
        }
        return boss;
    }
}
