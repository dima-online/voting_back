package kz.bsbnb.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruslan on 10.12.16.
 */
public class WordUtil {

    public static void fill() {
        String docxPath = "/opt/voting/test/test.docx";
//        String dotxPath = "/opt/voting/test/test.dotx";

        String filePath = docxPath;

        ReportVariables reportVariables = new ReportVariables();

        try {
            reportVariables.scanForVariables(filePath);

            Map<String, String> map = new HashMap<>();
            map.put("test", "Hello World");

            ReplaceVariables replaceVariables = new ReplaceVariables(map);
            replaceVariables.readAndReplaceDocFile(filePath);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
