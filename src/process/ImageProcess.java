package process;

import model.ImageModel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageProcess {
    private BufferedImage inputImage;
    private BufferedImage outputImage;
    private ImageModel imageModel;

    public ImageProcess(String imageName){
        try {
            imageModel = new ImageModel(imageName);
            /*int width = inputImage.getWidth();
            int height = inputImage.getHeight();
            outputImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

            Graphics g = outputImage.getGraphics();
            g.drawImage(inputImage, 0, 0, null);
            g.dispose();
            ImageIO.write(outputImage, "jpg", new File("2.jpg"));*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
