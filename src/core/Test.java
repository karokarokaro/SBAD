package core;

import core.cache.ObjectCache;
import core.cache.ObjectTypeCache;
import core.database.Attributes;
import core.database.DBObject;
import core.entity.CampTypes;
import core.helpers.DBHelper;
import core.helpers.TempHelper;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        try{
            StringBuffer db = new StringBuffer();
            File dir = new File("1");
            for (File file: dir.listFiles()) {
                Scanner sc = new Scanner(file);
                String s = "";
                while (sc.hasNext()) {
                    if (s.contains("ссылке: ")) {
                        break;
                    }
                    s = sc.nextLine();
                }
                if (s.contains("ссылке: ")) {
                    db.append(s.substring(s.indexOf("ссылке: ")+7).trim() + ",");
                }
            }
            System.out.println(db.toString());
        } catch (Exception e) {
            System.out.println("exeption!\n");
        }
    }
}
