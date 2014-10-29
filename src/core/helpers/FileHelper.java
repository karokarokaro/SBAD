package core.helpers;

import core.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: KARO
 * Date: 14.09.13
 * Time: 20:38
 * To change this template use File | Settings | File Templates.
 */
public class FileHelper {
    public static String saveImageFromNet(String url, String fileName, String format) {
        try {
            BufferedImage img = ImageIO.read(new java.net.URL(url));
            File file = new File("/img/"+fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            ImageIO.write(img, format, file);
            return "/img/"+fileName;
        } catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    public static String saveImageFromNet(String url, String fileName) {
        return saveImageFromNet(url, fileName, "jpg");
    }
}
