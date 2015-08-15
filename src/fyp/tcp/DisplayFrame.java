package fyp.tcp;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;


public class DisplayFrame extends JFrame{
    public BufferedImage image;
    public JLabel label;
    public DisplayFrame() throws IOException{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        this.setSize(1440 / 2, 1920 / 2);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        label = new JLabel();
        label.setSize(1440 / 2, 1920 / 2);
        this.getContentPane().add(label);
        this.setVisible(true);
    }

    public void set(byte[] b) {
        InputStream is = new ByteArrayInputStream(b);
        try {
            image = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        label.setIcon(new ImageIcon(image));

    }

    public void set(Mat mat) {
        this.setSize(mat.width() / 2, mat.height() / 2);
        label.setSize(mat.width() / 2, mat.height() / 2);
//        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);

//        MatOfByte mob = new MatOfByte();
//        CascadeClassifier cc = new CascadeClassifier("haarcascade_frontalface_alt.xml");
//        MatOfRect mor = new MatOfRect();
//        cc.detectMultiScale(mat, mor);
//        for (Rect rect : mor.toArray()) {
//            // System.out.println("ttt");
//            Core.rectangle(mat, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255,0));
//        }
//        Highgui.imencode(".bmp", mat, mob);

        image = convertMatToBufferedImageThroughMatOfByte(mat);

        label.setIcon(new ImageIcon(image));
    }

    public BufferedImage convertMatToBufferedImageThroughMatOfByte(Mat m) {
        MatOfByte mob = new MatOfByte();
        Highgui.imencode(".png", m, mob);
        byte[] b = mob.toArray();
        InputStream is = new ByteArrayInputStream(b);
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bi;
    }


    public BufferedImage convertMatToBufferedImage(Mat m){
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_4BYTE_ABGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte [] b = new byte[bufferSize];
        m.get(0,0,b);
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }
}
