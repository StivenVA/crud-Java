package org.project.desktop.dto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Date;

public class EmployeeDTO {
    private InputStream updateImage;
    private String id, name, lastName, email, direction, phone;
    private Date birthdate;
    private File image;
    private File video;

    public EmployeeDTO(){

    }

    public void setUpdateImage(InputStream updateImage){
        this.updateImage = updateImage;
    }

    public InputStream getUpdateImage(){
        return updateImage;
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

    public void setVideo(File video){
        this.video = video;
    }

    public File getVideo(){
        return this.video;
    }
}
