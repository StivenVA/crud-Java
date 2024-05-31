package org.project.desktop.entity;

import org.project.desktop.dbconfig.repository.EmployeesRepository;
import org.project.desktop.interfaces.repository.EmployeeRepository;

import java.io.File;
import java.util.Date;
import java.util.List;

public class Employee {

    private String id, name, lastName, email, direction, phone;
    private Date birthdate;

    private File image;
    private File video;

    private static final EmployeeRepository employeeRepository = new EmployeesRepository("mysql");

    public static void saveVideoLocal(File video){
        ((EmployeesRepository) employeeRepository).saveVideoLocal(video);
    }

    public static void save(Employee employee){
        try {
            employeeRepository.save(employee);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void delete(String id){
        try {
            employeeRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void update(Employee employee){
        try {
            employeeRepository.update(employee);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Employee findById(String id){
        try {
            return employeeRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Employee> findAll(){
        try {
            return employeeRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthdate() {
        return this.birthdate;
    }

    public void setImage(File image){
        this.image = image;
    }

    public File getImage(){
        return this.image;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public File getVideo() {
        return video;
    }

    public void setVideo(File video) {
        this.video = video;
    }
}
