package org.project.gui;

import org.project.entity.Employee;
import org.project.util.DataBase;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class HomeScreen extends JFrame {

    private boolean updatedImage;
    private File selectedImage;
    private Blob updateImage;
    private JPanel mainPanel;
    private JTextField idTxt;
    private JTextField nameTxt;
    private JTextField lastNameTxt;
    private JTextField emailTxt;
    private JTextField directionTxt;
    private JTextField phoneTxt;
    private JTextField birthdateTxt;
    private JButton addEmployeeBtn;
    private JButton chargeImageButton;
    private JTextField identificationTextField;
    private JButton updateButton;
    private JTextField showName;
    private JTextField showLastName;
    private JTextField showEmail;
    private JTextField showPhone;
    private JTextField showDirection;

    private JLabel showBirthdate;
    private JLabel showId;
    private JButton deleteButton;
    private JLabel imageLabel;
    private JLabel showImage;

    private JButton uploadImageUpdate;
    private DataBase dataBase;

    public HomeScreen() throws IOException {

        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        uploadImageUpdate.setEnabled(false);

        dataBase = new DataBase();
        setContentPane(mainPanel);

        addEmployeeBtn.addActionListener(e -> addUser());
        updateButton.addActionListener(e -> updateInformationEmployee());
        deleteButton.addActionListener(e -> showModal("¿Esta seguro que desea eliminar este empleado?", true));
        chargeImageButton.addActionListener(e -> selectImage(imageLabel));
        uploadImageUpdate.addActionListener(e -> selectImage(showImage));

        identificationTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                identificationTextField.addActionListener(event -> searchInformation());
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateState();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateState();
            }
        });
    }

    private void searchInformation(){
        try {
            ResultSet resultSet = dataBase.searchInformation(identificationTextField.getText());

            if (resultSet.next()) {
                setInformationInConsultFieldsAfterSearch(resultSet);
                updateState();
            } else showModal("Usuario no encontrado", false);

        } catch (SQLException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void setInformationInConsultFieldsAfterSearch(ResultSet resultSet) throws SQLException, IOException {
        showId.setText("ID: " + resultSet.getInt("id"));
        showName.setText(resultSet.getString("name"));
        showLastName.setText(resultSet.getString("last_name"));
        showEmail.setText(resultSet.getString("email"));
        showDirection.setText(resultSet.getString("direction"));
        showPhone.setText(resultSet.getString("phone"));
        showBirthdate.setText("Fecha de nacimiento: " + resultSet.getDate("birthdate"));
        Blob blob = resultSet.getBlob("image");
        setImageIcon(convertBlobToImage(blob),showImage);
    }

    private Image convertBlobToImage(Blob blob) throws IOException,SQLException{
        updateImage = blob;

        InputStream inputStream = blob.getBinaryStream();

        byte[] bytes = inputStream.readAllBytes();

        Image image = ImageIO.read(new ByteArrayInputStream(bytes));

        return image;
    }

    private void selectImage(JLabel labelImage) {
        JFileChooser jFileChooser = new JFileChooser();

        int result = jFileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            BufferedImage originalImage;
            try {
                this.selectedImage = jFileChooser.getSelectedFile();
                originalImage = ImageIO.read(selectedImage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            setImageIcon(originalImage, labelImage);
            updatedImage = true;
        } else updatedImage = false;
    }

    private <T extends Image> void setImageIcon(T image, JLabel imageLabel) {
        Image scaledImage = image.getScaledInstance(100, 150, Image.SCALE_SMOOTH);

        ImageIcon imageIcon = new ImageIcon(scaledImage);

        imageLabel.setIcon(imageIcon);
    }

    private void showModal(String message, boolean delete) {
        JDialog jDialog = new JDialog();
        JLabel jLabel = new JLabel(message);

        JPanel jPanel = getjPanelForModal(delete, jDialog);

        jDialog.getContentPane().setLayout(new BorderLayout());
        jDialog.getContentPane().add(jLabel, BorderLayout.CENTER);
        jDialog.getContentPane().add(jPanel, BorderLayout.SOUTH);

        jDialog.setSize(325, 150);
        jDialog.setLocationRelativeTo(this);
        jDialog.setVisible(true);
    }

    private JPanel getjPanelForModal(boolean delete, JDialog jDialog) {
        JButton acceptButton = new JButton("Aceptar");
        JButton cancelButton = new JButton("Cancelar");

        if (delete)
            acceptButton.addActionListener(e -> {
                deleteInformationEmployee();
                jDialog.dispose();
            });

        else {
            acceptButton.setVisible(false);
            cancelButton.setText("Aceptar");
        }

        cancelButton.addActionListener(e1 -> jDialog.dispose());

        JPanel jPanel = new JPanel();

        jPanel.add(acceptButton);
        jPanel.add(cancelButton);
        return jPanel;
    }

    private void updateState() {
        if (identificationTextField.getText().isEmpty()) {
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
            uploadImageUpdate.setEnabled(false);
            setConsultPaneBlankFields();
        } else {
            updateButton.setEnabled(true);
            deleteButton.setEnabled(true);
            uploadImageUpdate.setEnabled(true);
        }
    }

    private void updateInformationEmployee() {
        try {
            if (dataBase.updateInformation(updateEmployee()))
                showModal("Usuario actualizado correctamente", false);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addUser() {
        try {
            if (fieldsAddNoAreBlank()) {
                dataBase.insert(createEmployee());
                setAddPaneBlankFields();
            } else System.out.println("Complete todos los campos");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Employee createEmployee() {
        Employee employee = new Employee();

        employee.setBirthdate(Date.valueOf(birthdateTxt.getText()));
        employee.setDirection(directionTxt.getText());
        employee.setEmail(emailTxt.getText());
        employee.setId(idTxt.getText());
        employee.setPhone(phoneTxt.getText());
        employee.setLastName(lastNameTxt.getText());
        employee.setName(nameTxt.getText());
        employee.setImage(selectedImage);

        return employee;
    }


    private void setAddPaneBlankFields() {
        birthdateTxt.setText("");
        directionTxt.setText("");
        emailTxt.setText("");
        idTxt.setText("");
        phoneTxt.setText("");
        lastNameTxt.setText("");
        nameTxt.setText("");
        imageLabel.setIcon(null);

    }

    private Employee updateEmployee() {
        Employee employee = new Employee();

        employee.setEmail(showEmail.getText());
        employee.setDirection(showDirection.getText());
        employee.setName(showName.getText());
        employee.setLastName(showLastName.getText());
        employee.setPhone(showPhone.getText());
        employee.setId(identificationTextField.getText());
        employee.setImage(null);
        if (!updatedImage) employee.setUpdateImage(updateImage);
        else employee.setImage(selectedImage);

        return employee;
    }

    private void deleteInformationEmployee() {
        try {

            dataBase.deleteInformation(identificationTextField.getText());
            setConsultPaneBlankFields();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void setConsultPaneBlankFields() {
        showBirthdate.setText("");
        showDirection.setText("");
        showEmail.setText("");
        showId.setText("");
        showPhone.setText("");
        showLastName.setText("");
        showName.setText("");
        identificationTextField.setText("");
        showImage.setIcon(null);
    }

    private boolean fieldsAddNoAreBlank() {
        return !birthdateTxt.getText().isBlank() && !directionTxt.getText().isBlank() && !emailTxt.getText().isBlank() && !idTxt.getText().isBlank() && !phoneTxt.getText().isBlank() && !lastNameTxt.getText().isBlank() && !nameTxt.getText().isBlank() && selectedImage != null;
    }

}


