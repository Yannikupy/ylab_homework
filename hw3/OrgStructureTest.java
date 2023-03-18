package hw3;

import java.io.File;
import java.io.IOException;

public class OrgStructureTest {

    public static void main(String[] args) {
        OrgStructureParser orgStructureParser = new OrgStructureParserImpl();
        try {
            System.out.println(orgStructureParser.parseStructure(new File("hw3/employee.csv")).getName());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
