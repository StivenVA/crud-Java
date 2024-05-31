package org.project.desktop.views.console;

import org.project.desktop.fachada.ApplicationFachada;
import org.project.desktop.interfaces.observer.Observable;
import org.project.util.components.CameraComponent;
import org.project.desktop.dto.EmployeeDTO;
import org.project.desktop.interfaces.observer.Observer;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleView implements Runnable, Observer, Observable {
    private final Scanner in;
    private final Thread thread;
    private EmployeeDTO employeeDTOForUpdate;
    private boolean isUpdate;
    private final ApplicationFachada applicationFachada;
    private final List<Observer> observers;

    public ConsoleView() {
        in = new Scanner(System.in);
        CameraComponent cameraComponent = CameraComponent.getCameraComponent();
        cameraComponent.registryObserver(this);
        thread = new Thread(cameraComponent);
        applicationFachada = ApplicationFachada.getInstance();
        observers = new ArrayList<>();
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
        while (option!=4);
    }

    private void createUser() {
        employeeDTOForUpdate = createEmployee();
        applicationFachada.saveEmployee(employeeDTOForUpdate);
        notifyObservers();
        System.out.println("User created succesfully");
    }

    private void updateUser(){
        employeeDTOForUpdate = new EmployeeDTO();
        boolean exist;
        isUpdate = true;
        in.nextLine();
            do{
                System.out.println("Enter the user id: ");
                String id = in.nextLine();
                employeeDTOForUpdate = applicationFachada.findById(id);

                exist = employeeDTOForUpdate != null;
                if (!exist)
                    System.out.println("\nPlease enter a valid id");

            }while (!exist);

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
                in.nextLine();
                employeeDTOForUpdate.setName(in.nextLine());
                update();
                break;
            case 2:
                System.out.println("Enter the new last name: ");
                in.nextLine();
                employeeDTOForUpdate.setLastName(in.nextLine());
                update();
                break;
            case 3:
                System.out.println("Enter the new email: ");
                in.nextLine();
                employeeDTOForUpdate.setEmail(in.nextLine());
                update();
                break;
            case 4:
                System.out.println("Enter the new direction: ");
                in.nextLine();
                employeeDTOForUpdate.setDirection(in.nextLine());
                update();
                break;
            case 5:
                System.out.println("Enter the new phone: ");
                in.nextLine();
                employeeDTOForUpdate.setPhone(in.nextLine());
                update();
                break;
            case 6:
                System.out.println("Enter the new image: ");
                thread.start();
                break;
            case 7:
                System.out.println("Enter the new birthdate: ");
                in.nextLine();
                employeeDTOForUpdate.setBirthdate(Date.valueOf(in.nextLine()));
                update();
                break;
            case 8:
                isUpdate = true;
                System.out.println("Enter the new name: ");
                in.nextLine();
                employeeDTOForUpdate.setName(in.nextLine());
                System.out.println("Enter the new last name: ");
                in.nextLine();
                employeeDTOForUpdate.setLastName(in.nextLine());
                System.out.println("Enter the new email: ");
                in.nextLine();
                employeeDTOForUpdate.setEmail(in.nextLine());
                System.out.println("Enter the new direction: ");
                in.nextLine();
                employeeDTOForUpdate.setDirection(in.nextLine());
                System.out.println("Enter the new phone: ");
                in.nextLine();
                employeeDTOForUpdate.setPhone(in.nextLine());
                System.out.println("Enter the new birthdate: ");
                in.nextLine();
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
        applicationFachada.updateEmployee(employeeDTOForUpdate);
        notifyObservers();
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
        in.nextLine();
        String id = in.nextLine();
        employeeDTOForUpdate = new EmployeeDTO();
        employeeDTOForUpdate.setId(id);
        notifyObservers();
        applicationFachada.deleteEmployee(id);
        System.out.println("User deleted succesfully");

    }

    @Override
    public void notify(EmployeeDTO  employeeDTO) {
        if (!isUpdate) return;

        if (employeeDTOForUpdate.getId() == null) {
            return;
        }
        employeeDTOForUpdate.setImage(employeeDTO.getImage());

        applicationFachada.updateEmployee(employeeDTOForUpdate);
        notifyObservers();

        System.out.println("User updated succesfully");
        isUpdate = false;
    }

    public void iniciar(){
        Thread threadConsole = new Thread(this);
        threadConsole.start();
    }

    @Override
    public void registryObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(observer -> observer.notify(employeeDTOForUpdate));
    }
}
