package model;

import Jama.Matrix;

import java.util.ArrayList;
import java.util.List;

public class RectangleModel {
    private int hight;
    private int width;
    private int startX;
    private int startY;
    private List<Double> vectorX;
    private Matrix X;

    public RectangleModel(int startX, int startY, int width, int hight){
        vectorX = new ArrayList<>();
        setStartX(startX);
        setStartY(startY);
        setWidth(width);
        setHight(hight);
    }

    public void createMatrixX(){
        double bufferX[][] = new double[1][vectorX.size()];
        for (int i = 0; i < vectorX.size(); i++){
            bufferX[0][i] = vectorX.get(i);
        }
        X = new Matrix(bufferX);
    }

    public void addElement(double newElement){
        vectorX.add(newElement);
    }

    public int getHight() {
        return hight;
    }

    public void setHight(int hight) {
        this.hight = hight;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public List<Double> getVectorX() {
        return vectorX;
    }

    public void setVectorX(List<Double> vectorX) {
        this.vectorX = vectorX;
    }

    private void printSize(){
        System.out.println(startX + " " + startY + " " + width + " " + hight);
    }

    public Matrix getX() {
        return X;
    }

    public void setX(Matrix x) {
        X = x;
    }
}
