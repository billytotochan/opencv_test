package fyp.tcp;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {

    private int port;
    private static final int filesize = 6022386;
    private boolean running;
    private ByteArrayOutputStream result;
    private byte[] imageByteArray;
    private ServerSocket serverSocket;
    private Socket receiveSocket;
    private DatagramSocket socket;
//    private DisplayFrame myframe;

    private Thread receiveThread;

    public Server(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(port);
        System.out.println("ByteArraySize\tWidth\tHeight");
        running = true;
        while (true) {
            receiveSocket = serverSocket.accept();
            if (receiveSocket != null) {
                break;
            }
        }
//        myframe = new DisplayFrame();
//        VideoCapture vc = new VideoCapture();
//        Mat m = new Mat(5,5, 0);
//        vc.retrieve(m);
//        myframe.set(m);
        receiveTCP();
    }

    public void receiveTCP() throws IOException {
        new Thread() {
            public void run() {

                BufferedReader br = null;
                DataInputStream dis = null;
                try {
                    br = new BufferedReader(new InputStreamReader(receiveSocket.getInputStream()));
                    dis = new DataInputStream(receiveSocket.getInputStream());

                    while(true){
//                        long currentTime = System.currentTimeMillis();

//                        long byteArraySize = dis.readLong();
//                        int width = dis.readInt();
//                        int height = dis.readInt();

//                        System.out.println(byteArraySize + "\t" + 480 + "\t" + 848);

//                         if (byteArraySize > 0) {
//                        if (byteArraySize > 0 && byteArraySize < 10000000 && width < 8000 && height < 8000) {
                            imageByteArray = new byte[(int) 1628160];
//                             dis.readFully(imageByteArray, 0, (int) byteArraySize);
                             dis.readFully(imageByteArray, 0, 1628160);
//                            System.out.println(imageByteArray);

                            Mat mat = convertByteArrayToMat(imageByteArray, 480, 848);
                            try {
//                                myframe.set(imageByteArray);
                                Highgui.imwrite(System.currentTimeMillis()+".png",mat);
//                                myframe.set(mat);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //new GenImage(imageByteArray.clone(), byteArraySize);

                            imageByteArray = null;
//                         }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }.start();

    }

    public static void main(String[] args) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        int port = 8080;
        new Server(port);
    }

    public static Mat convertByteArrayToMat(byte[] imageByteArray, int width, int height) {
        Mat mat = new Mat(width, height, CvType.CV_8UC4);
        mat.put(0, 0, imageByteArray);
        return mat;
    }

//    class GenImage {
//
//        public GenImage(byte[] b, int length) {
//            new Thread() {
//                public void run() {
//                    try {
//                        FileOutputStream fileOut = new FileOutputStream("image\\" + System.currentTimeMillis() + ".jpg");
//                        BufferedOutputStream fileBuffer = new BufferedOutputStream(fileOut);
//                        fileBuffer.write(b, 0, length);
//                        fileBuffer.flush();
//                        fileBuffer.close();
//                        fileOut.close();
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            }.start();
//        }
//
//    }

}