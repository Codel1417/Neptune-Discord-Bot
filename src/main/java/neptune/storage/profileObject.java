package neptune.storage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.awt.image.BufferedImage;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class profileObject{
    String Bio;
    String Language;
    String Timezone;
    BufferedImage icon;
}