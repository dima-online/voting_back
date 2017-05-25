package kz.bsbnb.util;

import kz.gov.pki.kalkan.asn1.pkcs.PKCSObjectIdentifiers;
import kz.gov.pki.kalkan.jce.provider.KalkanProvider;
import kz.gov.pki.kalkan.xmldsig.KncaXS;
import org.apache.xml.security.encryption.XMLCipherParameters;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by ruslan on 28.11.16.
 */
public class CryptUtil {

    public static void initCrypt() {
        Provider provider = new KalkanProvider();
        Security.addProvider(provider);
        KncaXS.loadXMLSecurity();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        try {
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            KeyStore store = KeyStore.getInstance("PKCS12", provider.getName());
            Enumeration<String> als = store.aliases();
            String alias = null;
            while (als.hasMoreElements()) {
                alias = als.nextElement();
            }
            final PrivateKey privateKey = (PrivateKey) store.getKey(alias, "123456".toCharArray());
            final X509Certificate x509Certificate = (X509Certificate) store.getCertificate(alias);
            String sigAlgOid = x509Certificate.getSigAlgOID();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isAuthCert(X509Certificate cert) {
        try {
            if (cert.getExtendedKeyUsage()!=null) {
                for (String exKeyUsage: cert.getExtendedKeyUsage()) {
                    if (exKeyUsage.equals("1.3.6.1.5.5.7.3.2")) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String signXML(String xmlString, final String container, String password) {

        String result = null;
        String contr;
        if (container == null) {
            contr = "/opt/voting/test/test.p12";
        } else {
            contr = container;
        }
        try {
//            String xmlString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
//                    + "<root>"
//                    + "<person id=\"someid\">"
//                    + "<name>Стеве Жобс</name>"
//                    + "<iin>123456789012</iin>"
//                    + "</person>"
//                    + "</root>";

            Provider provider = new KalkanProvider();
            Security.addProvider(provider);
//          загружаем конфигурацию либо магической функцией
            KncaXS.loadXMLSecurity();
//            либо многословно так
//            System.setProperty("org.apache.xml.security.resource.config", "/kz/gov/pki/kalkan/xmldsig/pkigovkz.xml");
//            org.apache.xml.security.Init.init();
//            org.apache.xml.security.algorithms.JCEMapper.setProviderId(KalkanProvider.PROVIDER_NAME);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            final Document doc = documentBuilder.parse(new ByteArrayInputStream(xmlString.getBytes("UTF-8")));
            final String signMethod;
            final String digestMethod;
            KeyStore store = KeyStore.getInstance("PKCS12", provider.getName());
            InputStream inputStream;
            inputStream = AccessController.doPrivileged(new PrivilegedExceptionAction<FileInputStream>() {
                @Override
                public FileInputStream run() throws Exception {
                    FileInputStream fis = new FileInputStream(contr);
                    return fis;
                }
            });
            store.load(inputStream, password.toCharArray());
            Enumeration<String> als = store.aliases();
            String alias = null;
            while (als.hasMoreElements()) {
                alias = als.nextElement();
            }

            final PrivateKey privateKey = (PrivateKey) store.getKey(alias, password.toCharArray());
            final X509Certificate x509Certificate = (X509Certificate) store.getCertificate(alias);
            String sigAlgOid = x509Certificate.getSigAlgOID();
            if (sigAlgOid.equals(PKCSObjectIdentifiers.sha1WithRSAEncryption.getId())) {
                signMethod = Constants.MoreAlgorithmsSpecNS + "rsa-sha1";
                digestMethod = Constants.MoreAlgorithmsSpecNS + "sha1";
            } else if (sigAlgOid.equals(PKCSObjectIdentifiers.sha256WithRSAEncryption.getId())) {
                signMethod = Constants.MoreAlgorithmsSpecNS + "rsa-sha256";
                digestMethod = XMLCipherParameters.SHA256;
            } else {
                signMethod = Constants.MoreAlgorithmsSpecNS + "gost34310-gost34311";
                digestMethod = Constants.MoreAlgorithmsSpecNS + "gost34311";
            }

            XMLSignature sig = new XMLSignature(doc, "", signMethod);

            if (doc.getFirstChild() != null) {
                doc.getFirstChild().appendChild(sig.getElement());
                Transforms transforms = new Transforms(doc);
                transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
                transforms.addTransform(XMLCipherParameters.N14C_XML_CMMNTS);
                sig.addDocument("", transforms, digestMethod);
                sig.addKeyInfo(x509Certificate);
                sig.sign(privateKey);
                StringWriter os = new StringWriter();
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer trans = tf.newTransformer();
                trans.transform(new DOMSource(doc), new StreamResult(os));
                os.close();
                result = os.toString();
            }

            System.err.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static VerifyIIN verifyXml(String xmlString) {
        VerifyIIN result = new VerifyIIN(false, "", "");
        //System.out.println(xmlString);
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document doc = documentBuilder.parse(new ByteArrayInputStream(xmlString.getBytes("UTF-8")));

            Element sigElement = null;
            Element rootEl = (Element) doc.getFirstChild();

            NodeList list = rootEl.getElementsByTagName("ds:Signature");
            int length = list.getLength();
            for (int i = 0; i < length; i++) {
                Node sigNode = list.item(length - 1);
                sigElement = (Element) sigNode;
                if (sigElement == null) {
                    System.err.println("Bad signature: Element 'ds:Reference' is not found in XML document");
                }
                XMLSignature signature = new XMLSignature(sigElement, "");
                KeyInfo ki = signature.getKeyInfo();
                System.out.println(ki.containsX509Data());
                X509Certificate cert = ki.getX509Certificate();
                if (cert != null) {
                    String dn = cert.getSubjectDN().toString();
                    result.setVerify(signature.checkSignatureValue(cert));
                    result.setAuth(isAuthCert(cert));
                    System.out.println("dn=" + dn);
                    List<String> ls = new ArrayList<String>(Arrays.asList(dn.split(",")));
                    for (String next : ls) {
                        if (next.trim().startsWith("SERIALNUMBER")) {
                            result.setIin(next.substring(17));
                            System.out.println(result.getIin());
                        }
                        if (next.trim().startsWith("OU=BIN")) {
                            result.setBin(next.substring(7));
                            System.out.println(result.getBin());
                        }
                    }
                    rootEl.removeChild(sigElement);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("VERIFICATION RESULT IS: " + result.isVerify());
        return result;
    }

    private static Node getNode(Node node, String valueCode) {
        Node result = null;
        if (node != null) {
            if (node.getLocalName() != null && node.getLocalName().equals(valueCode)) {
                return node;
            } else {
                if (node.hasChildNodes()) {
                    for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                        Node next = node.getChildNodes().item(i);
                        result = getNode(next, valueCode);
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static String getValue(String xmlString, String valueCode) {
        String result = "";
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document doc = documentBuilder.parse(new ByteArrayInputStream(xmlString.getBytes("UTF-8")));

            Element rootEl = (Element) doc.getFirstChild();
            NodeList allChild = rootEl.getChildNodes();
            for (int i = 0; i < allChild.getLength(); i++) {
                Node node = getNode(allChild.item(i), valueCode);
                if (node != null && node.getChildNodes().getLength() > 0) {
                    result = node.getChildNodes().item(0).getNodeValue();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("RESULT IS: " + result);
        return result;
    }

    public static class VerifyIIN {
        private boolean verify;
        private String iin;
        private String bin;
        private boolean isAuth;

        public VerifyIIN(boolean verify, String iin, String bin) {
            this.verify = verify;
            this.iin = iin;
            this.bin = bin;
            this.isAuth = false;
        }

        public String getBin() {
            return bin;
        }

        public void setBin(String bin) {
            this.bin = bin;
        }

        public boolean isVerify() {
            return verify;
        }

        public void setVerify(boolean verify) {
            this.verify = verify;
        }

        public String getIin() {
            return iin;
        }

        public void setIin(String iin) {
            this.iin = iin;
        }

        public boolean isAuth() {
            return isAuth;
        }

        public void setAuth(boolean auth) {
            isAuth = auth;
        }
    }
}
