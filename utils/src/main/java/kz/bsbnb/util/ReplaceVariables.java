package kz.bsbnb.util;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
/**
 * Created by ruslan on 10.12.16.
 */
public class ReplaceVariables {

    private final Map map;
    private String fileName;

    public ReplaceVariables(Map map) {
        this.map = map;
    }

    public File readAndReplaceDocFile(String filePath) throws FileNotFoundException, XDocReportException, IOException {
        File file = new File(filePath);
        this.fileName = file.getName();

        InputStream in = new FileInputStream(file);

        IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Freemarker);

        File fileOutput = new File("/opt/voting/files/" + fileName);

        try (FileOutputStream out = new FileOutputStream(fileOutput)) {
            report.process(map, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileOutput;
    }
}
