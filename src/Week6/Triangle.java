/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Week6;

/**
 *
 * @author Edmundo Dela Cruz
 */
public class Triangle extends Polygon {
    public Triangle(double width, double height) {
        super(width, height);
    }

    @Override
    public double calculateArea() {
        return 0.5 * width * height;
    }

}