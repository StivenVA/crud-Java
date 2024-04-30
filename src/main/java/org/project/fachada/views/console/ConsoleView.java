package org.project.fachada.views.console;

import org.project.util.EmployeeMapper;
import org.project.util.components.CameraComponent;
import org.project.dto.EmployeeDTO;
import org.project.interfaces.Observer;
import org.project.config.dbconfig.repository.EmployeesRepository;

import java.io.File;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ConsoleView implements Runnable, Observer {
    private final Scanner in;
    private final EmployeesRepository employeesRepository;
    private final Thread thread;
    private EmployeeDTO employeeDTOForUpdate;
    private boolean isUpdate;

    public ConsoleView() {
        in = new Scanner(System.in);
        employeesRepository = new EmployeesRepository("mysql");
        CameraComponent cameraComponent = CameraComponent.getCameraComponent();
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

    private void createUser() {

        try {
            employeesRepository.save(EmployeeMapper.toEmployee(createEmployee()));
            System.out.println("User created succesfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void updateUser(){
        employeeDTOForUpdate = new EmployeeDTO();
        ResultSet resultSet;
        boolean exist;
        isUpdate = true;
        in.nextLine();
        try {
            do{
                System.out.println("Enter the user id: ");
                String id = in.nextLine();
                resultSet = employeesRepository.findById(id);

                exist = resultSet.next();
                if (!exist)
                    System.out.println("\nPlease enter a valid id");

            }while (!exist);

            employeeDTOForUpdate.setUpdateImage(resultSet.getBinaryStream("image"));
            employeeDTOForUpdate.setId(resultSet.getString("id"));
            employeeDTOForUpdate.setName(resultSet.getString("name"));
            employeeDTOForUpdate.setLastName(resultSet.getString("last_name"));
            employeeDTOForUpdate.setEmail(resultSet.getString("email"));
            employeeDTOForUpdate.setDirection(resultSet.getString("direction"));
            employeeDTOForUpdate.setPhone(resultSet.getString("phone"));
            employeeDTOForUpdate.setBirthdate(resultSet.getDate("birthdate"));

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
                employeeDTOForUpdate.setName(in.nextLine());
                update();
                break;
            case 2:
                System.out.println("Enter the new last name: ");
                employeeDTOForUpdate.setLastName(in.nextLine());
                update();
                break;
            case 3:
                System.out.println("Enter the new email: ");
                employeeDTOForUpdate.setEmail(in.nextLine());
                update();
                break;
            case 4:
                System.out.println("Enter the new direction: ");
                employeeDTOForUpdate.setDirection(in.nextLine());
                update();
                break;
            case 5:
                System.out.println("Enter the new phone: ");
                employeeDTOForUpdate.setPhone(in.nextLine());
                update();
                break;
            case 6:
                System.out.println("Enter the new image: ");
                thread.start();
                break;
            case 7:
                System.out.println("Enter the new birthdate: ");
                employeeDTOForUpdate.setBirthdate(Date.valueOf(in.nextLine()));
                update();
                break;
            case 8:
                isUpdate = true;
                System.out.println("Enter the new name: ");
                employeeDTOForUpdate.setName(in.nextLine());
                System.out.println("Enter the new last name: ");
                employeeDTOForUpdate.setLastName(in.nextLine());
                System.out.println("Enter the new email: ");
                employeeDTOForUpdate.setEmail(in.nextLine());
                System.out.println("Enter the new direction: ");
                employeeDTOForUpdate.setDirection(in.nextLine());
                System.out.println("Enter the new phone: ");
                employeeDTOForUpdate.setPhone(in.nextLine());
                System.out.println("Enter the new birthdate: ");
                employeeDTOForUpdate.setBirthdate(Date.valueOf(in.nextLine()));
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
            employeesRepository.update(EmployeeMapper.toEmployee(employeeDTOForUpdate));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public EmployeeDTO createEmployee(){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        in.nextLine();
        System.out.println("Enter the user id: ");
        employeeDTO.setId(in.nextLine());
        System.out.println("Enter the user name: ");
        employeeDTO.setName(in.nextLine());
        System.out.println("Enter the user last name: ");
        employeeDTO.setLastName(in.nextLine());
        System.out.println("Enter the user email: ");
        employeeDTO.setEmail(in.nextLine());
        System.out.println("Enter the user direction: ");
        employeeDTO.setDirection(in.nextLine());
        System.out.println("Enter the user phone: ");
        employeeDTO.setPhone(in.nextLine());
        System.out.println("Enter the user birthdate: ");
        employeeDTO.setBirthdate(Date.valueOf(in.nextLine()));
        employeeDTO.setImage(new File("src/main/resources/white.png"));

        return employeeDTO;
    }

    public void deleteUser(){
        System.out.println("Enter the user id: ");
        try {
            employeesRepository.deleteById(in.nextLine());
            System.out.println("User deleted succesfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notify(File imageFile) {
        if (!isUpdate) return;

        try {
            if(employeeDTOForUpdate.getId()!=null){
                employeeDTOForUpdate.setImage(imageFile);

                employeesRepository.update(EmployeeMapper.toEmployee(employeeDTOForUpdate));
                System.out.println("User updated succesfully");
                isUpdate = false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void iniciar(){
        Thread threadConsole = new Thread(this);
        threadConsole.start();
    }

}
