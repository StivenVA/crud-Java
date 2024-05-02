package org.project.util.components;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.opencv.video.Video;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.InputStream;

public class VideoPlayerSwing extends JPanel {

    private static final int FRAME_WIDTH = 640;
    private static final int FRAME_HEIGHT = 480;

    private FFmpegFrameGrabber grabber;
    private CanvasFrame canvasFrame;

    public VideoPlayerSwing(InputStream fileVideo) {

        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setLayout(new BorderLayout());

        JButton exitButton = new JButton("Salir");
        exitButton.addActionListener(e->close() );

        canvasFrame = new CanvasFrame("Video Player", CanvasFrame.getDefaultGamma() / 2.2);
        canvasFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        canvasFrame.add(exitButton, BorderLayout.SOUTH);

        try {
            grabber = new FFmpegFrameGrabber(fileVideo);
            grabber.start();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }

        Timer timer = new Timer(33, e -> {
            try {
                canvasFrame.showImage(grabber.grab());
            } catch (FrameGrabber.Exception | NullPointerException ignored) {
            }
        });
        timer.start();
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void close() {
        if (grabber != null) {
            try {
                grabber.stop();
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }
        if (canvasFrame != null) {
            canvasFrame.dispose();
        }
    }

}
