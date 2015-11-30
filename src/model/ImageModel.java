package model;

import Jama.Matrix;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageModel {
    private int imageWidth;
    private int imageHight;
    private int n;
    private int m;
    private int p;
    private int e;
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
//            Scanner in = new Scanner(System.in);
//            System.out.println("Enter hight of rectangle(n):");
//            n = in.nextInt();
//            System.out.println("Enter width of rectangle(m):");
//            m = in.nextInt();
//            System.out.println("Enter number of neuron for second layer(p):");
//            p = in.nextInt();
//            System.out.println("Enter error degree(e):");
//            e = in.nextInt();
//            in.close();
            n = 5; m = 5; p = 4; e = 8000;
            createRectangleList();
            createWeightMatrix();
            startWork();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage createOutputImage(){
        BufferedImage answer = new BufferedImage(imageHight, imageWidth, BufferedImage.TYPE_INT_RGB);
        for (RectangleModel currentRectangle : rectangleModelList){
            Matrix X = currentRectangle.getX();
            Matrix Y = X.times(W);
            Matrix X_ = Y.times(W_);
            int xx = currentRectangle.getStartX();
            int yy = currentRectangle.getStartY();
            int l=0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    int r = (int)convertRGBToOutput(X_.get(0, l++));
                    int g = (int)convertRGBToOutput(X_.get(0, l++));
                    int b = (int)convertRGBToOutput(X_.get(0, l++));
                    Color currentColor = new Color(r, g, b);
                    if (xx+i<imageHight && yy+j<imageWidth){
                        answer.setRGB(xx+i, yy+j, currentColor.getRGB());
                    }
                }
            }
        }
        return answer;
    }

    private double convertRGBToOutput(double rgb){
        double ans = 255*(rgb+1)/2;
        if (ans<0) ans=0;
        if (ans>255) ans=255;
        return ans;
    }
    private void startWork(){
        int t = 1;
        double E=1000000;
        while (E>e){
            int l = rectangleModelList.size();
            double alpha;
            double alpha_;
            E=0;
            for (int i = 0; i < l; i++) {
                Matrix X = rectangleModelList.get(i).getX();
                Matrix Y = X.times(W);
                Matrix X_ = Y.times(W_);
                Matrix deltaX = X_.minus(X);
                alpha=1/calculateVectorSum(X);
                alpha_=1/calculateVectorSum(Y);
                W = W.minus(X.transpose().times(alpha).times(deltaX).times(W_.transpose()));
                W_ = W_.minus(Y.transpose().times(alpha_).times(deltaX));
                normalizeMatrix(W);
                normalizeMatrix(W_);
                E += getErrorDegree(X, X_);
            }
            System.out.println("Iteration: "+t+"; Error: "+E);
            t++;
        }
    }

    private double getErrorDegree(Matrix X, Matrix X_){
        double e=0;
        for (int i = 0; i < X.getColumnDimension(); i++) {
            e += Math.pow(X_.get(0, i) - X.get(0, i), 2);
        }
        return e;
    }
    private void normalizeMatrix(Matrix matrix){
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            double sum = 0;
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                sum +=Math.pow(matrix.get(i, j), 2);
            }
            sum = Math.sqrt(sum);

            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                matrix.set(i, j, matrix.get(i, j) / sum);
            }
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

    public double calculateVectorSum(Matrix currentMatrix){
        if (currentMatrix.getRowDimension()>1) return 0;
        double sum=0;
        for (int i = 0; i < currentMatrix.getColumnDimension(); i++) {
            sum+=Math.pow(currentMatrix.get(0, i), 2);
        }
        return sum;
    }
}
