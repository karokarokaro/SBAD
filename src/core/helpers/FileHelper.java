package core.helpers;

import core.Logger;
import core.WebConfig;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.*;

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
    private static String generateEXTFileName(String extension) {
        String res;
        do {
            res = generateFileName() + "." + extension;
        } while (new File(WebConfig.UPLOAD_DIR + res).exists());
        return res;
    }
    private static String generateFileName() {
        Random rand = new Random();
        int length = 15;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            boolean isChar = rand.nextBoolean();
            if (isChar) {
                sb.append((char)('a'+rand.nextInt(26)));
            } else {
                sb.append(String.valueOf(rand.nextInt(10)));
            }
        }
        return sb.toString();
    }
    public static Map<String, String> processMultipartRequest(HttpServletRequest request) throws Exception {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        String[] exts = {"jpg", "jpeg", "gif", "png"};
        List<String> extensions = Arrays.asList(exts);
        Map<String, String> res = new HashMap<String, String>();
        if (isMultipart) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setRepository(new File(WebConfig.UPLOAD_DIR));
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(request);
            for (FileItem item: items) {

                res.put(item.getFieldName(), item.getString());
                if (!item.isFormField()) {
                    String fileName = item.getName();
                    if (!fileName.contains(".")) continue;
                    String ext = fileName.substring(fileName.lastIndexOf(".")+1);
                    if (!extensions.contains(ext)) continue;
                    String newName = generateEXTFileName(ext);
                    File file = new File(WebConfig.UPLOAD_DIR + newName);
                    try {
                        item.write(file);
                        res.put(item.getFieldName(), "/upload/" + newName);
                    } catch (Exception e) {
                        Logger.error(e);
                    }
                }
            }
        }
        return res;
    }

}
