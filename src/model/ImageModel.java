package model;

import Jama.Matrix;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ImageModel {
    private int imageWidth;
    private int imageHight;
    private int n;
    private int m;
    private int p;
    private double e;
    private BufferedImage inputImage;
    private List<RectangleModel> rectangleModelList;
    private Matrix W;
    private Matrix W_;

    public ImageModel(String imageName){
        try {
            File f = new File(imageName);
            inputImage = ImageIO.read(f);
            imageWidth = inputImage.getWidth();
            imageHight = inputImage.getHeight();
            Scanner in = new Scanner(System.in);
            System.out.println("Enter hight of rectangle(n):");
            n = in.nextInt();
            System.out.println("Enter width of rectangle(m):");
            m = in.nextInt();
            System.out.println("Enter number of neuron for second layer(p):");
            p = in.nextInt();
//            System.out.println("Enter error degree(e):");
//            e = in.nextInt();
            in.close();
            createRectangleList();
            createWeightMatrix();
            startWork();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startWork(){
        int t=1;
        double E=10000;
        while (E>e){
            int l = rectangleModelList.size();
            double sumY;
            for (int i = 0; i < l; i++) {
                Matrix X = rectangleModelList.get(i).getX();
                Matrix Y = X.times(W);
                Matrix X_ = Y.times(W_);
                Matrix deltaX = X_.minus(X);

            }


            t++;
        }
    }
    private void createWeightMatrix(){
        double bufferW[][] = new double[n*m*3][p];
        for (int i = 0; i < n*m*3; i++) {
            for (int j = 0; j < p; j++) {
                bufferW[i][j] = Math.random()*2 - 1;
            }
        }
        W = new Matrix(bufferW);
        W_ = W.transpose();
//        printMatrix(W);
//        System.out.println("------------------");
//        printMatrix(W_);
    }

    private void createRectangleList(){
        rectangleModelList = new ArrayList<>();
        RectangleModel bufferRectangle;
        int x = 0;
        while (x < imageWidth){
            int y = 0;
            while (y < imageHight){
                bufferRectangle = new RectangleModel(x, y, n, m);
                for (int i = x; i < x + n; i++){
                    for (int j = y; j < y + m; j++) {
                        if (i < imageWidth && j < imageHight){
                            Color colorPixel = new Color(inputImage.getRGB(i, j));
                            bufferRectangle.addElement(convertColor(colorPixel.getRed()));
                            bufferRectangle.addElement(convertColor(colorPixel.getBlue()));
                            bufferRectangle.addElement(convertColor(colorPixel.getGreen()));

                        } else {
                            bufferRectangle.addElement(-1);
                            bufferRectangle.addElement(-1);
                            bufferRectangle.addElement(-1);
                        }
                    }
                }
                bufferRectangle.createMatrixX();
                rectangleModelList.add(bufferRectangle);
                y += m;
            }
            x += n;
        }
    }

    private double convertColor(double color){
        return 2*color/255-1;
    }

    public BufferedImage getImage() {
        return inputImage;
    }

    public void setImage(BufferedImage inputImage) {
        this.inputImage = inputImage;
    }

    private void printMatrix(Matrix matrix){
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                System.out.print(matrix.get(i, j) + " ");
            }
            System.out.println();
        }
    }
}
