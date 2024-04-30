package org.project.util.components;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.project.interfaces.Observable;
import org.project.interfaces.Observer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CameraComponent extends  JFrame implements Runnable, Observable {

    private final JFrame cameraFrame = new JFrame();
    private JLabel imageCaptureLabel;
    private VideoCapture capture;
    private static CameraComponent cameraComponent;
    private File selectedImage;
    private List<Observer> observers;

    private CameraComponent(){
        startCamera();
        observers = new ArrayList<>();
    }

    public static CameraComponent getCameraComponent(){
        if(cameraComponent == null){
            cameraComponent = new CameraComponent();
        }
        return cameraComponent;
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

    }

    private void initializeCamera(){
        capture = new VideoCapture(0);
        if (!capture.isOpened()) {
            System.out.println("Error al abrir la cámara");
        } else {
            System.out.println("Cámara abierta correctamente");
        }
    }

    private void closeCamera(){
        Thread.currentThread().interrupt();
        capture.close();
        if (selectedImage != null){
            notifyObservers();
        }
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
            saveTakenPhoto(image);
            closeCamera();
            capturedFrame.dispose();
            cameraFrame.dispose();
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


    @Override
    public void run() {
        capture = new VideoCapture(0);
        cameraFrame.setVisible(true);
        while (true) {
            Mat frame = new Mat();
            try {
                if (capture.read(frame)) {
                    // Convertir el fotograma de OpenCV a una imagen de Swing
                    BufferedImage image = matToBufferedImage(frame);
                    // Mostrar la imagen en el JLabel
                    imageCaptureLabel.setIcon(new ImageIcon(image));
                }
            }catch (Exception ignored){
            }
        }
    }


    @Override
    public void registryObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {

        for (Observer observer : observers){
            observer.notify(selectedImage);
        }

    }
}
