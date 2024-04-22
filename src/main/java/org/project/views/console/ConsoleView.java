package org.project.views.console;

import org.project.components.CameraComponent;
import org.project.entity.Employee;
import org.project.interfaces.Observer;
import org.project.util.dbconfig.DataBase;

import java.io.File;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ConsoleView implements Runnable, Observer {
    private Scanner in;
    private DataBase dataBase;
    private CameraComponent cameraComponent;
    private Thread thread;
    private File imageFile;
    private String idForUpdate;
    private Employee employeeForUpdate;

    public ConsoleView() {
        in = new Scanner(System.in);
        dataBase = new DataBase("mysql");
        cameraComponent = CameraComponent.getCameraComponent();
        cameraComponent.registryObserver(this);
        thread = new Thread(cameraComponent);
    }

    public void createUser() {
        Employee employee = new Employee();
        in.nextLine();
        System.out.println("Enter the user id: ");
        employee.setId(in.nextLine());
        System.out.println("Enter the user name: ");
        employee.setName(in.nextLine());
        System.out.println("Enter the user last name: ");
        employee.setLastName(in.nextLine());
        System.out.println("Enter the user email: ");
        employee.setEmail(in.nextLine());
        System.out.println("Enter the user direction: ");
        employee.setDirection(in.nextLine());
        System.out.println("Enter the user phone: ");
        employee.setPhone(in.nextLine());
        System.out.println("Enter the user birthdate: ");
        employee.setBirthdate(Date.valueOf(in.nextLine()));
        employee.setImage(new File("src/main/resources/white.png"));

        try {
            dataBase.insert(employee);
            System.out.println("User created succesfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateUser(){
        Employee employee = new Employee();
        System.out.println("Enter the user id: ");
        in.nextLine();
        employee.setId(in.nextLine());
        idForUpdate = employee.getId();

        System.out.println("Select what you want to update: ");

        System.out.println("1. Name");
        System.out.println("2. Last Name");
        System.out.println("3. Email");
        System.out.println("4. Direction");
        System.out.println("5. Phone");
        System.out.println("6. Image");
        System.out.println("7. Birthdate");
        System.out.println("8. All");
        System.out.println("9. Exit");
        System.out.println("Enter the option: ");

        int option = in.nextInt();
        switch (option) {
            case 1:
                System.out.println("Enter the new name: ");
                employee.setName(in.nextLine());
                break;
            case 2:
                System.out.println("Enter the new last name: ");
                employee.setLastName(in.nextLine());
                break;
            case 3:
                System.out.println("Enter the new email: ");
                employee.setEmail(in.nextLine());
                break;
            case 4:
                System.out.println("Enter the new direction: ");
                employee.setDirection(in.nextLine());
                break;
            case 5:
                System.out.println("Enter the new phone: ");
                employee.setPhone(in.nextLine());
                break;
            case 6:
                System.out.println("Enter the new image: ");
                thread.start();
                break;
            case 7:
                System.out.println("Enter the new birthdate: ");
                employee.setBirthdate(Date.valueOf(in.nextLine()));
                break;
            case 8:
                System.out.println("Enter the new name: ");
                employeeForUpdate.setName(in.nextLine());
                System.out.println("Enter the new last name: ");
                employeeForUpdate.setLastName(in.nextLine());
                System.out.println("Enter the new email: ");
                employeeForUpdate.setEmail(in.nextLine());
                System.out.println("Enter the new direction: ");
                employeeForUpdate.setDirection(in.nextLine());
                System.out.println("Enter the new phone: ");
                employeeForUpdate.setPhone(in.nextLine());
                System.out.println("Enter the new birthdate: ");
                employeeForUpdate.setBirthdate(Date.valueOf(in.nextLine()));
                System.out.println("Enter the new image: ");
                thread.start();
                break;
            case 9:
                break;
            default:
                System.out.println("Invalid option");
                break;
        }
    }

    @Override
    public void run() {
        int option;
        System.out.println("Welcome to the console view");
        do {
            System.out.println();
            System.out.println("1. Create user");
            System.out.println("2. Update user");
            System.out.println("3. Exit");

            System.out.println();
            System.out.println("Enter the option: ");
            option = in.nextInt();

            switch (option) {
                case 1:
                    createUser();
                    break;
                case 2:
                    updateUser();
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
        while (option != 3);
    }

    @Override
    public void notify(File imageFile) {
        Employee employee = null;
        ResultSet resultSet;
        try {
            resultSet = dataBase.searchInformation(idForUpdate);

            if(resultSet.next()){

                if (employeeForUpdate!=null){
                    employee = employeeForUpdate;
                    employee.setImage(imageFile);
                }else {

                    employee = new Employee();
                    employee.setId(resultSet.getString("id"));
                    employee.setName(resultSet.getString("name"));
                    employee.setLastName(resultSet.getString("last_name"));
                    employee.setEmail(resultSet.getString("email"));
                    employee.setDirection(resultSet.getString("direction"));
                    employee.setPhone(resultSet.getString("phone"));
                    employee.setBirthdate(resultSet.getDate("birthdate"));
                    employee.setImage(imageFile);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        employee.setId(idForUpdate);
        employee.setImage(imageFile);
        try {
            dataBase.updateInformation(employee);
            System.out.println("User updated succesfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
