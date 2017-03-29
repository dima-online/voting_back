package kz.bsbnb.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruslan on 10.12.16.
 */
public class WordUtil {

    public static String fill(Map<String, String> map, Long votingId, String docxPath) {
//        String docxPath = "/opt/voting/test/test.docx";
//        String dotxPath = "/opt/voting/test/test.dotx";

        String filePath = docxPath;
        Date now = new Date();
        String fileName = now.getTime() +votingId+ ".docx";

        ReportVariables reportVariables = new ReportVariables();

        try {
            reportVariables.scanForVariables(filePath);

            ReplaceVariables replaceVariables = new ReplaceVariables(map);
            replaceVariables.readAndReplaceDocFile(filePath, fileName);
            return fileName;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
