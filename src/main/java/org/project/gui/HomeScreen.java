package org.project.gui;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.project.entity.Employee;
import org.project.util.DataBase;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;

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
    private JButton takePhoto;
    private DataBase dataBase;

    private JLabel imageCaptureLabel;
    private VideoCapture capture;

    private  JDialog cameraFrame;

    private boolean createdImage;
    public HomeScreen() throws IOException {
        cameraFrame = new JDialog();

        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        uploadImageUpdate.setEnabled(false);

        dataBase = new DataBase();
        setContentPane(mainPanel);

        addEmployeeBtn.addActionListener(e -> addUser());
        updateButton.addActionListener(e -> updateInformationEmployee());
        deleteButton.addActionListener(e -> showModal("¿Esta seguro que desea eliminar este empleado?", true));
        chargeImageButton.addActionListener(e -> {
            selectImage(imageLabel);
            System.out.println(imageLabel.getIcon() == null);
            System.out.println(imageLabel.getIcon().toString());
        });
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

        takePhoto.addActionListener(e->startCamera());
        cameraFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                closeCamera();

            }
        });
    }

    private void startCamera(){
        initializeCamera();
        configureCamera();
    }

    private void configureCamera(){

        JPanel panel = new JPanel();

        cameraFrame.setTitle("Captura de Cámara");
        cameraFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        cameraFrame.setSize(640, 480);
        JButton captureButton = new JButton("Capturar");
        JButton exitButton = new JButton("salir");

        imageCaptureLabel = new JLabel();
        cameraFrame.setLocationRelativeTo(this);
        captureButton.addActionListener(e -> captureImage());
        exitButton.addActionListener(e->{
            cameraFrame.dispose();
            closeCamera();
        });

        panel.add(captureButton);
        panel.add(exitButton);

        cameraFrame.getContentPane().setLayout(new BorderLayout());
        cameraFrame.getContentPane().add(imageCaptureLabel, BorderLayout.CENTER);
        cameraFrame.getContentPane().add(panel,BorderLayout.SOUTH);

        cameraFrame.setVisible(true);
    }

    private void initializeCamera(){
        capture = new VideoCapture(0);
        if (!capture.isOpened()) {
            System.out.println("Error al abrir la cámara");
        } else {
            System.out.println("Cámara abierta correctamente");
            // Comenzar la captura de la cámara
            startCapture();
        }
    }

    private void startCapture() {
        new Thread(() -> {
            while (true) {
                Mat frame = new Mat();
                try {
                    if (capture.read(frame)) {
                        // Convertir el fotograma de OpenCV a una imagen de Swing
                        BufferedImage image = matToBufferedImage(frame);
                        // Mostrar la imagen en el JLabel
                        imageCaptureLabel.setIcon(new ImageIcon(image));
                }
                }catch (Exception ignored){}
            }
        }).start();
    }

    private void closeCamera(){
            Thread.currentThread().interrupt();
            capture.close();
    }

    private void captureImage() {
        Mat frame = new Mat();
        if (capture.read(frame)) {

            BufferedImage image = matToBufferedImage(frame);

            JDialog capturedFrame = new JDialog();
            capturedFrame.setTitle("Imagen capturada");
            capturedFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            capturedFrame.setSize(image.getWidth(), image.getHeight());

            JPanel panel = getjPanelForCapturePhoto(image, capturedFrame);

            JLabel capturedImageLabel = new JLabel(new ImageIcon(image));
            capturedFrame.setLayout(new BorderLayout());
            capturedFrame.getContentPane().add(capturedImageLabel,BorderLayout.CENTER);
            capturedFrame.getContentPane().add(panel,BorderLayout.SOUTH);
            capturedFrame.setVisible(true);
        }
    }

    private JPanel getjPanelForCapturePhoto(BufferedImage image, JDialog capturedFrame) {
        JButton reTryPhoto = new JButton("Volver a tomar la foto");
        JButton acceptPhoto = new JButton("Guardar foto");

        acceptPhoto.addActionListener(e->{
            setImageIcon(image,this.imageLabel);
            saveTakenPhoto(image);
            createdImage = true;
            capturedFrame.dispose();
            cameraFrame.dispose();
            System.out.println(this.imageLabel.getIcon() == null);
        });
        reTryPhoto.addActionListener(e-> capturedFrame.dispose());

        JPanel panel = new JPanel();
        panel.add(reTryPhoto);
        panel.add(acceptPhoto);
        return panel;
    }

    private void saveTakenPhoto(BufferedImage photo){
        File outputImageFile = new File("img"+ LocalDate.now()+".jpg");

        try {
            ImageIO.write(photo,"jpg",outputImageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.selectedImage = outputImageFile;
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        int width = mat.cols();
        int height = mat.rows();
        int channels = mat.channels();
        byte[] data = new byte[width * height * channels];
        mat.data().get(data);

        // Convertir el formato de los canales de BGR a RGB
        byte[] newData = new byte[width * height * channels];
        for (int i = 0; i < width * height; i++) {
            newData[i * 3] = data[i * 3 + 2];     // R
            newData[i * 3 + 1] = data[i * 3 + 1]; // G
            newData[i * 3 + 2] = data[i * 3];     // B
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        image.getRaster().setDataElements(0, 0, width, height, newData);
        return image;
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

        return ImageIO.read(new ByteArrayInputStream(bytes));
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
            createdImage = false;
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
                if (createdImage) this.selectedImage.delete();
            } else showModal("Complete todos los campos",false);
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
        return !birthdateTxt.getText().isBlank()
                && !directionTxt.getText().isBlank()
                && !emailTxt.getText().isBlank()
                && !idTxt.getText().isBlank()
                && !phoneTxt.getText().isBlank()
                && !lastNameTxt.getText().isBlank()
                && !nameTxt.getText().isBlank()
                && selectedImage != null;
    }

}


