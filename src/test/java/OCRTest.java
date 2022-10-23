import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OCRTest {

    @Test
    void testOCR() throws IOException, TesseractException {
        URL is = getClass().getClassLoader().getResource("tess_test_img.png");
        assert is != null;
        BufferedImage img = ImageIO.read(is);
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("tessdata");
        String text = tesseract.doOCR(img);
        // https://github.com/nguyenq/tess4j/blob/master/src/test/java/net/sourceforge/tess4j/Tesseract1Test.java
        String expResult = "The (quick) [brown] {fox} jumps!\nOver the $43,456.78 <lazy> #90 dog";
        assertEquals(expResult, text.substring(0, expResult.length()));
    }
}
