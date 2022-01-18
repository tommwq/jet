package com.tommwq.jet.database;

import static org.junit.Assert.assertEquals; 
import org.junit.Test;

public class SQLiteHelperTest {

        public static class Employee {
                String name;
                int age;
                boolean vacation;
                double salary;
                Float bonus;
        }

        SQLiteHelper helper = new SQLiteHelper(null);

        @Test
        public void createTableSQL() {
                String expect = "CREATE TABLE Employee (name TEXT,age INTEGER,vacation INTEGER,salary REAL,bonus REAL)";
                assertEquals(expect, helper.createTableSQL(Employee.class));
        }

        @Test
        public void selectSQL() {
                String expect = "SELECT name,age,vacation,salary,bonus FROM Employee";
                assertEquals(expect, helper.selectSQL(Employee.class));
        }

        @Test
        public void insertSQL() {
                String expect = "INSERT INTO Employee (name,age,vacation,salary,bonus) VALUES (\"John\",25,1,0.2,null)";

                Employee employee = new Employee();
                employee.name = "John";
                employee.age = 25;
                employee.vacation = true;
                employee.salary = 10000.0;
                employee.salary = 0.2;
                
                assertEquals(expect, helper.insertSQL(employee));
        }
}
