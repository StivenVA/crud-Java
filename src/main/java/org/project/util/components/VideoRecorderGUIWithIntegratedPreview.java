package org.project.util.components;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;
import org.project.dto.EmployeeDTO;
import org.project.fachada.ApplicationFachada;
import org.project.interfaces.observer.Observable;
import org.project.interfaces.observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoRecorderGUIWithIntegratedPreview implements Observable { // Ruta del archivo de salida
    private static final int CAPTURE_WIDTH = 640;
    private static final int CAPTURE_HEIGHT = 480;
    private static final int FRAMES_PER_SECOND = 20;

    private static volatile boolean recording = false;
    private static File videoFile;
    private final List<Observer> observers;

    public VideoRecorderGUIWithIntegratedPreview(){
        this.observers = new ArrayList<>();
    }

    public void iniciarCapturaVideo(String idUsuario) {

        JFrame frame = new JFrame("Video Recorder");
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel para mostrar el video
        JPanel videoPanel = new JPanel(new BorderLayout());
        frame.add(videoPanel, BorderLayout.CENTER);

        // Botón de grabación
        JButton recordButton = new JButton("Grabar");
        JButton exitButton = new JButton("Salir");
        recordButton.addActionListener(e -> {
            recording = !recording;
            recordButton.setText(recording ? "Detener" : "Grabar");
            if (recording) {
                startRecording(videoPanel,idUsuario);
            } else {
                stopRecording();
                notifyObservers();
            }
        });

        exitButton.addActionListener(e -> {
            stopRecording();
            notifyObservers();
            frame.dispose();
        });

        JPanel panel = new JPanel();
        panel.add(recordButton);
        panel.add(exitButton);

        // Añadir botón a la ventana
        frame.add(panel, BorderLayout.SOUTH);

        // Manejar cierre de la ventana
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopRecording();
                notifyObservers();
                frame.dispose();
            }
        });

        // Mostrar ventana
        frame.setVisible(true);
    }

    private static void startRecording(JPanel videoPanel, String idUsuario) {
        new Thread(() -> {
            FrameGrabber grabber = new OpenCVFrameGrabber(0);
            grabber.setImageWidth(CAPTURE_WIDTH);
            grabber.setImageHeight(CAPTURE_HEIGHT);

            try {
                grabber.start();

                // Crear el archivo de salida y asignarlo a la variable
                videoFile = new File(System.getProperty("user.home")+"\\video_usuario_"+idUsuario+".mp4");

                FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(videoFile, CAPTURE_WIDTH, CAPTURE_HEIGHT);
                recorder.setFormat("mp4");
                recorder.setFrameRate(FRAMES_PER_SECOND);
                recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4);
                recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
                recorder.start();

                while (recording) {
                    Frame frame = grabber.grab();
                    recorder.record(frame);
                    showFrame(frame, videoPanel);
                    // Esperar 1000 / framesPerSecond milisegundos antes de capturar el próximo fotograma
                    Thread.sleep(1000 / FRAMES_PER_SECOND);
                }

                recorder.stop();
                grabber.stop();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void stopRecording() {
        recording = false;

    }

    private static void showFrame(Frame frame, JPanel panel) {
        if (frame != null) {
            Graphics g = panel.getGraphics();
            if (g != null) {
                g.drawImage(convertToImage(frame), 0, 0, panel.getWidth(), panel.getHeight(), null);
                g.dispose();
            }
        }
    }

    private static Image convertToImage(Frame frame) {
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.convert(frame);
        return bufferedImage;
    }

    @Override
    public void registryObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        EmployeeDTO  employeeDTO = new EmployeeDTO();
        employeeDTO.setVideo(videoFile);

        for (Observer observer : observers) {
            observer.notify(employeeDTO);
        }
    }
}
