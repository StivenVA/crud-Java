package org.project.views.console;

import org.project.components.CameraComponent;
import org.project.entity.Employee;
import org.project.interfaces.Observer;
import org.project.util.InputStreamConverter;
import org.project.util.dbconfig.DataBase;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ConsoleView implements Runnable, Observer {
    private Scanner in;
    private DataBase dataBase;
    private CameraComponent cameraComponent;
    private Thread thread;
    private Employee employeeForUpdate;
    private boolean isUpdate;

    public ConsoleView() {
        in = new Scanner(System.in);
        dataBase = new DataBase("mysql");
        cameraComponent = CameraComponent.getCameraComponent();
        cameraComponent.registryObserver(this);
        thread = new Thread(cameraComponent);
    }


    @Override
    public void run() {
        int option;
        System.out.println("Welcome to the console view");
        do {
            System.out.println();
            System.out.println("1. Create user");
            System.out.println("2. Update user");
            System.out.println("3. Delete user");
            System.out.println("4. Exit");

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
                    deleteUser();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
        while (option >3 || option < 1);
    }

    public void createUser() {

        try {
            dataBase.insert(createEmployee());
            System.out.println("User created succesfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateUser(){
        employeeForUpdate = new Employee();
        ResultSet resultSet;
        boolean exist;
        isUpdate = true;
        in.nextLine();
        try {
            do{
                System.out.println("Enter the user id: ");
                String id = in.nextLine();
                resultSet = dataBase.searchInformation(id);

                exist = resultSet.next();
                if (!exist)
                    System.out.println("\nPlease enter a valid id");

            }while (!exist);

            employeeForUpdate.setUpdateImage(resultSet.getBinaryStream("image"));
            employeeForUpdate.setId(resultSet.getString("id"));
            employeeForUpdate.setName(resultSet.getString("name"));
            employeeForUpdate.setLastName(resultSet.getString("last_name"));
            employeeForUpdate.setEmail(resultSet.getString("email"));
            employeeForUpdate.setDirection(resultSet.getString("direction"));
            employeeForUpdate.setPhone(resultSet.getString("phone"));
            employeeForUpdate.setBirthdate(resultSet.getDate("birthdate"));

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
                employeeForUpdate.setName(in.nextLine());
                update();
                break;
            case 2:
                System.out.println("Enter the new last name: ");
                employeeForUpdate.setLastName(in.nextLine());
                update();
                break;
            case 3:
                System.out.println("Enter the new email: ");
                employeeForUpdate.setEmail(in.nextLine());
                update();
                break;
            case 4:
                System.out.println("Enter the new direction: ");
                employeeForUpdate.setDirection(in.nextLine());
                update();
                break;
            case 5:
                System.out.println("Enter the new phone: ");
                employeeForUpdate.setPhone(in.nextLine());
                update();
                break;
            case 6:
                System.out.println("Enter the new image: ");
                thread.start();
                break;
            case 7:
                System.out.println("Enter the new birthdate: ");
                employeeForUpdate.setBirthdate(Date.valueOf(in.nextLine()));
                update();
                break;
            case 8:
                isUpdate = true;
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

    public void update(){
        try {
            dataBase.updateInformation(employeeForUpdate);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Employee createEmployee(){
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

        return employee;
    }

    public void deleteUser(){
        System.out.println("Enter the user id: ");
        try {
            dataBase.deleteInformation(in.nextLine());
            System.out.println("User deleted succesfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notify(File imageFile) {
        if (!isUpdate) return;

        try {
            if(employeeForUpdate.getId()!=null){
                employeeForUpdate.setImage(imageFile);

                dataBase.updateInformation(employeeForUpdate);
                imageFile.delete();
                System.out.println("User updated succesfully");
                isUpdate = false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
