package gui;

import entity.Employee;
import util.DataBase;

import javax.crypto.ExemptionMechanism;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class HomeScreen extends JFrame{

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
    private JTabbedPane homePane;
    private JPanel addPane;
    private JComboBox comboBox1;
    private JPanel consultPane;
    private JButton imageButton1;

    Employee employee = new Employee();
    private DataBase dataBase;


    public HomeScreen() throws IOException {

        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        dataBase = new DataBase();

        setContentPane(mainPanel);

        addEmployeeBtn.addActionListener(e -> {
                try {
                    if (!birthdateTxt.getText().isBlank() && !directionTxt.getText().isBlank() && !emailTxt.getText().isBlank() && !idTxt.getText().isBlank() && !phoneTxt.getText().isBlank() && !lastNameTxt.getText().isBlank() && !nameTxt.getText().isBlank() && selectedImage!= null){

                        employee.setBirthdate(Date.valueOf(birthdateTxt.getText()));
                        employee.setDirection(directionTxt.getText());
                        employee.setEmail(emailTxt.getText());
                        employee.setId(idTxt.getText());
                        employee.setPhone(phoneTxt.getText());
                        employee.setLastName(lastNameTxt.getText());
                        employee.setName(nameTxt.getText());
                        employee.setImage(selectedImage);

                        dataBase.insert(employee);

                        birthdateTxt.setText("");
                        directionTxt.setText("");
                        emailTxt.setText("");
                        idTxt.setText("");
                        phoneTxt.setText("");
                        lastNameTxt.setText("");
                        nameTxt.setText("");
                        imageLabel.setIcon(null);
                    }

                    else System.out.println("Complete todos los campos");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        });


        identificationTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

                identificationTextField.addActionListener(event->{
                    try {
                        ResultSet resultSet = dataBase.searchInformation(identificationTextField.getText());

                        if (resultSet.next()){
                            showId.setText("ID: "+resultSet.getInt("id"));
                            showName.setText(resultSet.getString("name"));
                            showLastName.setText(resultSet.getString("last_name"));
                            showEmail.setText(resultSet.getString("email"));
                            showDirection.setText(resultSet.getString("direction"));
                            showPhone.setText(resultSet.getString("phone"));
                            showBirthdate.setText("Fecha de nacimiento: " + resultSet.getDate("birthdate"));
                            Blob blob = resultSet.getBlob("image")==null ? null : resultSet.getBlob("image");

                            updateImage = blob;

                            if (blob!=null){
                                InputStream inputStream = blob.getBinaryStream();

                                byte[] bytes = inputStream.readAllBytes();

                                Image image = ImageIO.read(new ByteArrayInputStream(bytes));
                                image = image.getScaledInstance(100, 150, Image.SCALE_SMOOTH);

                                ImageIcon imageIcon = new ImageIcon(image);

                                showImage.setIcon(imageIcon);
                            }


                            updateButton.setEnabled(true);
                            deleteButton.setEnabled(true);
                        }
                        else System.out.println("User not found");

                    } catch (SQLException | IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

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

        updateButton.addActionListener(e ->  {
            try{
                Employee employee = new Employee();

                employee.setEmail(showEmail.getText());
                employee.setDirection(showDirection.getText());
                employee.setName(showName.getText());
                employee.setLastName(showLastName.getText());
                employee.setPhone(showPhone.getText());
                employee.setId(identificationTextField.getText());
                employee.setImage(null);
                if(!updatedImage) employee.setUpdateImage(updateImage);
                else employee.setImage(selectedImage);

                if (dataBase.updateInformation(employee)){
                    showBirthdate.setText("");
                    showDirection.setText("");
                    showEmail.setText("");
                    showId.setText("");
                    showPhone.setText("");
                    showLastName.setText("");
                    showName.setText("");
                    identificationTextField.setText("");

                    showModal("Usuario actualizado correctamente",false);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        deleteButton.addActionListener(e ->  {
            showModal("¿Esta seguro que desea eliminar este empleado?",true);

        });
        chargeImageButton.addActionListener(e -> {
            selectImage(imageLabel);
        });
        imageButton1.addActionListener(e -> {
            selectImage(showImage);
        });
    }

    private void selectImage(JLabel labelImage){
        JFileChooser jFileChooser = new JFileChooser();

        int result = jFileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            this.selectedImage = jFileChooser.getSelectedFile();
            BufferedImage originalImage = null;
            try {
                originalImage = ImageIO.read(this.selectedImage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Image scaledImage = originalImage.getScaledInstance(100, 150, Image.SCALE_SMOOTH);

            ImageIcon imageIcon = new ImageIcon(scaledImage);

            labelImage.setIcon(imageIcon);
            updatedImage = true;
        }
        else updatedImage = false;
    }

    private void showModal(String string,boolean delete){
        JDialog jDialog = new JDialog();
        JLabel jLabel = new JLabel(string);

        JButton acceptButton = new JButton("Aceptar");
        JButton cancelButton = new JButton("Cancelar");

        if (delete)
        acceptButton.addActionListener(e1 -> {
                try {

                    dataBase.deleteInformation(identificationTextField.getText());
                    showBirthdate.setText("");
                    showDirection.setText("");
                    showEmail.setText("");
                    showId.setText("");
                    showPhone.setText("");
                    showLastName.setText("");
                    showName.setText("");
                    identificationTextField.setText("");

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            jDialog.dispose();
        });

        else{
            acceptButton.setVisible(false);
            cancelButton.setText("Aceptar");
        }

        cancelButton.addActionListener(e1 -> {
            jDialog.dispose();
        });

        JPanel jPanel = new JPanel();

        jPanel.add(acceptButton);
        jPanel.add(cancelButton);

        jDialog.getContentPane().setLayout(new BorderLayout());
        jDialog.getContentPane().add(jLabel, BorderLayout.CENTER);
        jDialog.getContentPane().add(jPanel, BorderLayout.SOUTH);

        jDialog.setSize(325, 150);
        jDialog.setLocationRelativeTo(this);
        jDialog.setVisible(true);
    }

    private void updateState(){
        if (identificationTextField.getText().isEmpty()){
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }
        else {
            updateButton.setEnabled(true);
            updateButton.setEnabled(true);
        }
    }
}


