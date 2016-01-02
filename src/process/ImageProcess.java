package process;

import model.ImageModel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageProcess {
    private BufferedImage outputImage;
    private ImageModel imageModel;

    public ImageProcess(String imageName){
        try {
            imageModel = new ImageModel(imageName);
            outputImage = imageModel.createOutputImage();
//            ImageIO.write(outputImage, "jpg", new File("output.jpg"));
            ImageIO.write(outputImage, "png", new File("output.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
