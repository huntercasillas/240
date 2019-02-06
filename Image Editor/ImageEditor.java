/*  Hunter Casillas
 CS240 ImageEditor
 */

import java.util.Scanner;
import java.io.*;

public class ImageEditor {
    
    public static void main (String[] args) {
        if (args.length > 4 || args.length < 3) {
            System.out.println("Error: You have inputted an invalid amount of arguments. Please follow the correct usage.");
            System.out.println("Usage: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
	    	System.exit(0);
        } else if (args[2].compareTo("grayscale") == 1 || args[2].compareTo("invert") == 1 || args[2].compareTo("emboss") == 1 || args[2].compareTo("motionblur") == 1) {
            System.out.println("Error: Please be sure to follow the correct usage when running this program.");
            System.out.println("Usage: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
            System.exit(0);
        } else {
            try {
                Scanner inputScanner = new Scanner (new BufferedInputStream(new FileInputStream(args[0])));
                inputScanner.useDelimiter("((#[^\\n]*\\n)|(\\s+))+");
            
                Image myImage = new Image();
                int width = 0;
                int height = 0;
                inputScanner.next();
                width = inputScanner.nextInt();
                height = inputScanner.nextInt();
                int maxVal = inputScanner.nextInt();
            
                Pixel[][] myPixels = new Pixel[height][width];
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        Pixel newPixel = new Pixel();
                        newPixel.setRed(inputScanner.nextInt());
                        newPixel.setGreen(inputScanner.nextInt());
                        newPixel.setBlue(inputScanner.nextInt());
                        myPixels[i][j] = newPixel;
                    }
                }
                myImage.setPixelMap(myPixels);
                inputScanner.close();
                Image newImage = new Image();
                Pixel[][] myNewPixels = new Pixel[height][width];
                
                switch (args[2]) {
                    
                case "invert":
                        if (args.length != 3) {
                            System.out.println("Error: Please be sure to follow the correct usage when running this program.");
                            System.out.println("Usage: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
                            System.exit(0);
                        } else {
                            for (int i = 0; i < height; i++) {
                                for (int j = 0; j < width; j++) {
                                    Pixel newPixel = new Pixel();
                                    newPixel.setRed(maxVal - myImage.getPixelMap()[i][j].getRed());
                                    newPixel.setGreen(maxVal - myImage.getPixelMap()[i][j].getGreen());
                                    newPixel.setBlue(maxVal - myImage.getPixelMap()[i][j].getBlue());
                                    myNewPixels[i][j] = newPixel;
                                }
                            }
                            newImage.setPixelMap(myNewPixels);
                            break;
                        }
                    
                case "grayscale":
                        if (args.length != 3) {
                            System.out.println("Error: Please be sure to follow the correct usage when running this program.");
                            System.out.println("Usage: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
                            System.exit(0);
                        } else {
                            for (int i = 0; i < height; i++) {
                                for (int j = 0; j < width; j++) {
                                    Pixel newPixel = new Pixel();
                                    int newValue = (myImage.getPixelMap()[i][j].getRed() + myImage.getPixelMap()[i][j].getGreen() + myImage.getPixelMap()[i][j].getBlue())/3;
                                    newPixel.setRed(newValue);
                                    newPixel.setGreen(newValue);
                                    newPixel.setBlue(newValue);
                                    myNewPixels[i][j] = newPixel;
                                }
                            }
                            newImage.setPixelMap(myNewPixels);
                            break;
                        }
                        
                case "emboss":
                        if (args.length != 3) {
                            System.out.println("Error: Please be sure to follow the correct usage when running this program.");
                            System.out.println("Usage: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
                            System.exit(0);
                        } else {
                            final int borderCaseVal = 128;
                            for (int i = 0; i < height; i++) {
                                for (int j = 0; j < width; j++) {
                                    if ((i == 0) || (j == 0)) {
                                        Pixel newPixel = new Pixel();
                                        newPixel.setBlue(borderCaseVal);
                                        newPixel.setGreen(borderCaseVal);
                                        newPixel.setRed(borderCaseVal);
                                        myNewPixels[i][j] = newPixel;
                                    } else {
                                        Pixel newPixel = new Pixel();
                                        int redDiff = myImage.getPixelMap()[i][j].getRed() - myImage.getPixelMap()[i-1][j-1].getRed();
                                        int greenDiff = myImage.getPixelMap()[i][j].getGreen() - myImage.getPixelMap()[i-1][j-1].getGreen();
                                        int blueDiff = myImage.getPixelMap()[i][j].getBlue() - myImage.getPixelMap()[i-1][j-1].getBlue();
                                        int maxDifference = Math.max(Math.abs(redDiff), Math.max(Math.abs(greenDiff), Math.abs(blueDiff)));
                                        int scaleValue;
                                        if (maxDifference == Math.abs(redDiff)) {
                                            maxDifference = redDiff;
                                        } else if (maxDifference == Math.abs(greenDiff)) {
                                            maxDifference = greenDiff;
                                        } else {
                                            maxDifference = blueDiff;
                                        }
                                        scaleValue = borderCaseVal + maxDifference;
                                        if (scaleValue < 0) {
                                            scaleValue = 0;
                                        }
                                        if (scaleValue > 255) {
                                            scaleValue = 255;
                                        }
                                
                                        newPixel.setRed(scaleValue);
                                        newPixel.setGreen(scaleValue);
                                        newPixel.setBlue(scaleValue);
                                        myNewPixels[i][j] = newPixel;
                                    }
                                }
                            }
                            newImage.setPixelMap(myNewPixels);
                            break;
                        }
                    
                case "motionblur":
                        if (args.length != 4) {
                            System.out.println("Error: Please be sure to follow the correct usage when running this program.");
                            System.out.println("Usage: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
                            System.exit(0);
                        } else {
                            int blurLength = Integer.parseInt(args[3]);
                            if (blurLength <= 0) {
                                System.out.println("Error: Please be sure your motionblur length is an integer greater than 0.");
                                System.out.println("Usage: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
                                System.exit(0);
                            }
                            for (int i = 0; i < height; i++) {
                                for (int j = 0; j < width; j++) {
                                    Pixel newPixel = new Pixel();
                                    int sumRed = 0;
                                    int sumGreen = 0;
                                    int sumBlue = 0;
                                    int length = 0;
                                    for (int k = j; (k < width) && (k < j + blurLength); k++, length++) {
                                        sumRed +=  myImage.getPixelMap()[i][k].getRed();
                                        sumGreen += myImage.getPixelMap()[i][k].getGreen();
                                        sumBlue += myImage.getPixelMap()[i][k].getBlue();
                                    }
                                    
                                    int avgRed = sumRed / length;
                                    int avgGreen = sumGreen / length;
                                    int avgBlue = sumBlue / length;
                                    newPixel.setRed(avgRed);
                                    newPixel.setGreen(avgGreen);
                                    newPixel.setBlue(avgBlue);
                                    myNewPixels[i][j] = newPixel;
                                }
                            }
                            newImage.setPixelMap(myNewPixels);
                            break;
                        }
                    }
                
                    StringBuilder myBuilder = new StringBuilder();
                    PrintWriter myWriter = new PrintWriter(new BufferedWriter(new FileWriter(args[1])));
                    myBuilder.append("P3\n");
                    myBuilder.append(width);
                    myBuilder.append(" ");
                    myBuilder.append(height);
                    myBuilder.append("\n");
                    myBuilder.append(maxVal);
                    myBuilder.append("\n");
            
                    for (int i = 0; i < height; i++) {
                        for (int j = 0; j < width; j++) {
                            myBuilder.append(newImage.getPixelMap()[i][j].getRed());
                            myBuilder.append(" ");
                            myBuilder.append(newImage.getPixelMap()[i][j].getGreen());
                            myBuilder.append(" ");
                            myBuilder.append(newImage.getPixelMap()[i][j].getBlue());
                            myBuilder.append("  ");
                        }
                        myBuilder.append("\n");
                    }
                myWriter.write(myBuilder.toString());
                myWriter.close();
            }
            
            catch (Exception myException) {
                System.out.println("Error: Please be sure to follow the correct usage when running this program.");
                System.out.println("Usage: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
                System.exit(0);
            }
        }
    }
}
