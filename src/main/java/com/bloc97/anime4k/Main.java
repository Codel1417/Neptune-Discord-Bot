/*
    MIT License

    Copyright (c) 2019 bloc97

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
 */
package com.bloc97.anime4k;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author bloc97
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    static ImageKernel kernel = new ImageKernel();
    
    public static void main(String[] args) throws IOException {
        
        if (args.length < 2) {
            System.out.println("Error: Please specify input and output png files.");
            return;
        }
        
        String inputFile = args[0];
        String outputFile = args[1];
        
        BufferedImage img = ImageIO.read(new File(inputFile));
        img = copyType(img);
        
        float scale = 2f;
        
        if (args.length >= 3) {
            scale = Float.parseFloat(args[2]);
        }
        
        float pushStrength = scale / 6f;
        float pushGradStrength = scale / 2f;
        
        if (args.length >= 4) {
            pushGradStrength = Float.parseFloat(args[3]);
        }
        if (args.length >= 5) {
            pushStrength = Float.parseFloat(args[4]);
        }
                
        img = scale(img, (int)(img.getWidth() * scale), (int)(img.getHeight() * scale));
        
        kernel.setPushStrength(pushStrength);
        kernel.setPushGradStrength(pushGradStrength);
        kernel.setBufferedImage(img);
        kernel.process();
        kernel.updateBufferedImage();
        
        ImageIO.write(img, "png", new File(outputFile));
        
        
    }
    static BufferedImage copyType(BufferedImage bi) {
        BufferedImage newImage = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);
        newImage.getGraphics().drawImage(bi, 0, 0, null);
        return newImage;
    }
    static BufferedImage scale(BufferedImage bi, int width, int height) {
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D)newImage.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(bi, 0, 0, width, height, null);
        return newImage;
    }
    
}
