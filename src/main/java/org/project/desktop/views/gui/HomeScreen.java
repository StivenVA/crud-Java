package org.project.desktop.views.gui;

import org.project.desktop.fachada.ApplicationFachada;
import org.project.util.components.VideoPlayerSwing;
import org.project.util.components.VideoRecorderGUIWithIntegratedPreview;
import org.project.desktop.interfaces.observer.Observable;
import org.project.util.components.CameraComponent;
import org.project.desktop.dto.EmployeeDTO;
import org.project.desktop.interfaces.observer.Observer;
import org.project.util.InputStreamConverter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

public class HomeScreen extends JFrame implements Observer, Observable {

    private boolean updatedImage;
    private File selectedImage;
    private File captureVideo;
    private InputStream updateImage;
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
    private JButton takePhoto;
    private JButton capturarVideoButton;
    private JButton verVideoUsuarioButton;
    private boolean isRecording;
    private final CameraComponent cameraComponent;
    private boolean isCameraOn;
    private final ApplicationFachada applicationFachada;
    private final java.util.List<Observer> observers;
    private EmployeeDTO employeeDTOForNotify;
    private final VideoRecorderGUIWithIntegratedPreview videoRecorder;

    public HomeScreen(){
        videoRecorder = new VideoRecorderGUIWithIntegratedPreview();

        cameraComponent = CameraComponent.getCameraComponent();
        cameraComponent.registryObserver(this);
        videoRecorder.registryObserver(this);

        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        uploadImageUpdate.setEnabled(false);
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

        takePhoto.addActionListener(e->startCapture());

        applicationFachada = ApplicationFachada.getInstance();
        observers = new ArrayList<>();
        capturarVideoButton.addActionListener(e -> {
            if (!idTxt.getText().isBlank()){
                videoRecorder.iniciarCapturaVideo(idTxt.getText());
                isRecording = true;
            }
            else showModal("Por favor escriba primero el id del usuario",false);

        });

        verVideoUsuarioButton.addActionListener(e->{
            if(captureVideo!=null){
                new VideoPlayerSwing(InputStreamConverter.convertFileToInputStream(captureVideo));
            }
            else showModal("No hay video para mostrar",false);
        });
    }

    private void startCapture() {
        isCameraOn = true;
        Thread cameraThread = new Thread(cameraComponent);
        cameraThread.start();
    }

    private void searchInformation(){

            EmployeeDTO employeeDTO = applicationFachada.findById(identificationTextField.getText());
            if (employeeDTO != null) {
                try {
                    setInformationInConsultFieldsAfterSearch(employeeDTO);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                updateState();
            } else showModal("Usuario no encontrado", false);

    }

    private void setInformationInConsultFieldsAfterSearch(EmployeeDTO employeeDTO) throws IOException {
        showId.setText("ID: " + employeeDTO.getId());
        showName.setText(employeeDTO.getName());
        showLastName.setText(employeeDTO.getLastName());
        showEmail.setText(employeeDTO.getEmail());
        showDirection.setText(employeeDTO.getDirection());
        showPhone.setText(employeeDTO.getPhone());
        showBirthdate.setText("Fecha de nacimiento: " + employeeDTO.getBirthdate());
        captureVideo = employeeDTO.getVideo();
        InputStream inputStream = InputStreamConverter.convertFileToInputStream(employeeDTO.getImage());
        setImageIcon(InputStreamConverter.convertBinaryStreamToImage(inputStream),showImage);
        updateImage = inputStream;
    }


    private void selectImage(JLabel labelImage) {
        JFileChooser jFileChooser = new JFileChooser();

        int result = jFileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            BufferedImage originalImage;
            try {
                this.selectedImage = jFileChooser.getSelectedFile();
               originalImage = convertFileToBufferedImage(selectedImage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            setImageIcon(originalImage, labelImage);
            updatedImage = true;
        } else updatedImage = false;
    }

    private BufferedImage convertFileToBufferedImage(File selectedImage) throws IOException {
        return  ImageIO.read(selectedImage);
    }

    private <T extends Image> void setImageIcon(T image, JLabel imageLabel) {
        try {
            Image scaledImage = image.getScaledInstance(250, 150, Image.SCALE_SMOOTH);

            ImageIcon imageIcon = new ImageIcon(scaledImage);

            imageLabel.setIcon(imageIcon);
        }catch (NullPointerException e){

        }

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
            employeeDTOForNotify = createUpdatedEmployee();
            applicationFachada.updateEmployee(employeeDTOForNotify);
            notifyObservers();
            showModal("Usuario actualizado correctamente", false);
    }

    private void addUser() {
            if (fieldsAddNoAreBlank()) {
                if (isRecording){
                    showModal("Por favor detenga la grabación de video",false);
                    return;
                }
                employeeDTOForNotify = createEmployee();

                if(employeeDTOForNotify.getVideo()!=null){
                    employeeDTOForNotify.setVideo(applicationFachada.saveVideoLocal(employeeDTOForNotify.getVideo()));
                }

                System.out.println(employeeDTOForNotify.getImage());
                System.out.println(employeeDTOForNotify.getVideo());
                applicationFachada.saveEmployee(employeeDTOForNotify);
                notifyObservers();
                setAddPaneBlankFields();
                employeeDTOForNotify = null;
            } else showModal("Complete todos los campos",false);
    }

    private EmployeeDTO createEmployee() {
        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setBirthdate(Date.valueOf(birthdateTxt.getText()));
        employeeDTO.setDirection(directionTxt.getText());
        employeeDTO.setEmail(emailTxt.getText());
        employeeDTO.setId(idTxt.getText());
        employeeDTO.setPhone(phoneTxt.getText());
        employeeDTO.setLastName(lastNameTxt.getText());
        employeeDTO.setName(nameTxt.getText());
        employeeDTO.setImage(selectedImage);

        if (captureVideo!=null){
            employeeDTO.setVideo(captureVideo);
            captureVideo= null;
        }


        return employeeDTO;
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

    private EmployeeDTO createUpdatedEmployee() {
        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setEmail(showEmail.getText());
        employeeDTO.setDirection(showDirection.getText());
        employeeDTO.setName(showName.getText());
        employeeDTO.setLastName(showLastName.getText());
        employeeDTO.setPhone(showPhone.getText());
        employeeDTO.setId(identificationTextField.getText());
        employeeDTO.setImage(null);
        if (!updatedImage) employeeDTO.setUpdateImage(updateImage);
        else employeeDTO.setImage(selectedImage);

        return employeeDTO;
    }

    private void deleteInformationEmployee() {
            employeeDTOForNotify = new EmployeeDTO();
            employeeDTOForNotify.setId(identificationTextField.getText());
            applicationFachada.deleteEmployee(identificationTextField.getText());
            notifyObservers();
            setConsultPaneBlankFields();
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
        return !birthdateTxt.getText().isBlank()
                && !directionTxt.getText().isBlank()
                && !emailTxt.getText().isBlank()
                && !idTxt.getText().isBlank()
                && !phoneTxt.getText().isBlank()
                && !lastNameTxt.getText().isBlank()
                && !nameTxt.getText().isBlank()
                && selectedImage != null;
    }

    @Override
    public void notify(EmployeeDTO employeeDTO) {
        if (isCameraOn){
            this.selectedImage = employeeDTO.getImage();

            try {
                BufferedImage image = convertFileToBufferedImage(selectedImage);
                setImageIcon(image,imageLabel);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            isCameraOn = false;
        }
        else if(isRecording){
            captureVideo = employeeDTO.getVideo();
            isRecording = false;
        }
    }

    public void iniciar(){
        setTitle("Welcome");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 1000);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void registryObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.notify(employeeDTOForNotify);
        }
    }
}


