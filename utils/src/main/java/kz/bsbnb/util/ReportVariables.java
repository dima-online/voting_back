package kz.bsbnb.util;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.FieldExtractor;
import fr.opensagres.xdocreport.template.FieldsExtractor;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * Created by ruslan on 10.12.16.
 */
public class ReportVariables {

    Set<String> existingVariables = new HashSet<>();

    public void scanForVariables(String filePath) throws IOException, XDocReportException {
        File file = new File(filePath);
        InputStream in = new FileInputStream(file);
        System.out.println(file.getAbsoluteFile());
        IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Freemarker);

        FieldsExtractor fieldsExtractor = new FieldsExtractor();

        report.extractFields(fieldsExtractor, report.getTemplateEngine());
        List fields = fieldsExtractor.getFields();

        System.out.println("Variables size: " + fields.size());

        for (Object field : fields) {
            FieldExtractor e = (FieldExtractor) field;
            existingVariables.add(e.getName());
            System.out.println("Variable: " + e.getName());
        }
    }

//    public Map fillMapWithValues(ReportParamLocal reportParam, Product product) {
//
//        Map map = new HashMap();
//        for (String key : existingVariables) {
//            String param = reportParam.getParam(product, key);
//            if (param != null) {
//                map.put(key, param);
//            }
//        }
//        return map;
//    }
}