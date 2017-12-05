package kz.bsbnb.digisign.processor.impl;

import kz.bsbnb.digisign.processor.DigisignProcessor;
import kz.bsbnb.digisign.util.DigiSignException;
import kz.bsbnb.digisign.util.DigisignConfigProperties;
import kz.bsbnb.digisign.util.SignatureInfo;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

//import kz.gov.pki.kalkan.jce.provider.KalkanProvider;
//import kz.gov.pki.kalkan.jce.provider.cms.*;

/**
 * Created by Roman.Rychkov on 28.10.2016.
 */
@Service
public class DigisignProcessorImpl implements DigisignProcessor {

    @Autowired
    private DigisignConfigProperties digisignConfigProperties;

    @Value(value = "classpath:GOSTKNCA_284e3708647f6fc17f35688604d589b3547e689f.p12")
    private Resource resource;

    private boolean isSelfSigned(X509Certificate cert)
            throws CertificateException, NoSuchAlgorithmException,
            NoSuchProviderException {
        try {
            PublicKey key = cert.getPublicKey();
            cert.verify(key);
            return true;
        } catch (SignatureException sigEx) {
            return false;
        } catch (InvalidKeyException keyEx) {
            return false;
        }
    }


    private X509Certificate loadCert(String src) throws IOException {
        InputStream in = null;
        X509Certificate cert = null;
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            in = new FileInputStream(src);
            cert = (X509Certificate) certFactory.generateCertificate(in);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (in != null) in.close();
        }
        return cert;
    }

    private X509Certificate downloadCert(String urlString) {
        try {
            URL url = new URL(urlString);
            InputStream in = url.openStream();
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(in);
            return cert;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }

    }

    private boolean verifyTrustChain(X509Certificate cert) throws DigiSignException {
        try {
            if (isSelfSigned(cert)) {
                throw new DigiSignException(
                        "The certificate is self-signed.");
            }
            Set<X509Certificate> additionalCerts = new HashSet<>();

//            String rootCert = "http://root.gov.kz/root_cer/root_rsa.crt";
//            String middleCert = "http://root.gov.kz/root_cer/pki_rsa.crt";

            String rootCert = new StringBuffer(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getParentFile().getPath()).append("/resources/main/root_rsa.crt").toString();
            String middleCert = new StringBuffer(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getParentFile().getPath()).append("/resources/main/pki_rsa.crt").toString();


            additionalCerts.add(loadCert(rootCert));
            additionalCerts.add(loadCert(middleCert));
            additionalCerts.add(cert);

            Set<X509Certificate> trustedRootCerts = new HashSet<X509Certificate>();
            Set<X509Certificate> intermediateCerts = new HashSet<X509Certificate>();
            for (X509Certificate additionalCert : additionalCerts) {
                if (isSelfSigned(additionalCert)) {
                    trustedRootCerts.add(additionalCert);
                } else {
                    intermediateCerts.add(additionalCert);
                }
            }


            X509CertSelector selector = new X509CertSelector();
            selector.setCertificate(cert);

            Set<TrustAnchor> trustAnchors = new HashSet<TrustAnchor>();
            for (X509Certificate trustedRootCert : trustedRootCerts) {
                trustAnchors.add(new TrustAnchor(trustedRootCert, null));
            }

            PKIXBuilderParameters pkixParams =
                    new PKIXBuilderParameters(trustAnchors, selector);

            pkixParams.setRevocationEnabled(false);

            CertStore intermediateCertStore = CertStore.getInstance("Collection",
                    new CollectionCertStoreParameters(intermediateCerts));
//            CertStore intermediateCertStore = CertStore.getInstance("Collection",
//                    new CollectionCertStoreParameters(intermediateCerts), Constants.KALKAN_PROVIDER_NAME);
            pkixParams.addCertStore(intermediateCertStore);

//            CertPathBuilder builder = CertPathBuilder.getInstance("PKIX", Constants.KALKAN_PROVIDER_NAME);
            CertPathBuilder builder = CertPathBuilder.getInstance("PKIX");
            PKIXCertPathBuilderResult verifiedCertChai =
                    (PKIXCertPathBuilderResult) builder.build(pkixParams);


            return verifiedCertChai != null;
        } catch (CertPathBuilderException certPathEx) {
            throw new DigiSignException(
                    "Error building certification path: " +
                            cert.getSubjectX500Principal(), certPathEx);
        } catch (DigiSignException cvex) {
            throw cvex;
        } catch (Exception ex) {
            throw new DigiSignException(
                    "Error verifying the certificate: " +
                            cert.getSubjectX500Principal(), ex);
        }
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private boolean isSignCert(X509Certificate cert) {
        boolean[] usages = cert.getKeyUsage();
        return usages[0] && usages[1];
    }

    private boolean isAuthCert(X509Certificate cert) {
        try {
            if (cert.getExtendedKeyUsage() != null) {
                for (String exKeyUsage : cert.getExtendedKeyUsage()) {
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

    @Override
    public Boolean verifySignature(String document, String signatureString, String publicCertificate, int operationType) throws DigiSignException {
        boolean result;
        try {
//            Provider provider = new KalkanProvider();
//            Security.addProvider(provider);

            ByteArrayInputStream in = new ByteArrayInputStream(publicCertificate.getBytes(StandardCharsets.UTF_8));
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(in);

            validateCertificateType(cert, operationType);

            if (new Date().after(cert.getNotAfter())) { //Проверка даты по на валидность
                throw new DigiSignException("The certificate is expired", DIGISIGN_ERROR_EXPIRED, DIGISIGN_ERROR_EXPIRED_MESSAGE_KEY);
            }

            verifyTrustChain(cert);//Проверка цепочки доверия

            isCertificeteRevoked(cert);//Проверка, не отозван ли сертификат пользователя

//            Signature signature = Signature.getInstance(cert.getSigAlgName(), provider.getName());
            Signature signature = Signature.getInstance(cert.getSigAlgName());
            signature.initVerify(cert);
            signature.update(document.getBytes(StandardCharsets.UTF_8));
            result = signature.verify(DatatypeConverter.parseHexBinary(signatureString.replaceAll("\n", "")));//Проверка цифровой подписи
        } catch (CertificateException e) {
            e.printStackTrace();
            throw new DigiSignException(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new DigiSignException(e.getMessage(), e);
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new DigiSignException(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new DigiSignException(e.getMessage(), e);
        } catch (DigiSignException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    /*@Override
    public Boolean verifyNCASignature(String document, String signatureString, int operationType) throws DigiSignException {
        return verifyNCASignature(document.getBytes(StandardCharsets.UTF_8), signatureString, operationType);
    }

    public Boolean verifyNCASignature(byte[] document, String signatureString, int operationType) throws DigiSignException {
        boolean result = true;
        try {
            Provider provider = new KalkanProvider();
            Security.addProvider(provider);

            CMSSignedData e = new CMSSignedData(Base64.decode(signatureString.getBytes(StandardCharsets.UTF_8)));
            boolean isAttachedContent = e.getSignedContent() != null;
            if (isAttachedContent) {
                e = new CMSSignedData(e.getEncoded());
            } else {
                CMSProcessableByteArray signers = new CMSProcessableByteArray(document);
                e = new CMSSignedData(signers, e.getEncoded());
            }

            SignerInformationStore signers1 = e.getSignerInfos();
            CertStore certs = e.getCertificatesAndCRLs("Collection", new KalkanProvider().getName());
            Iterator it = signers1.getSigners().iterator();

            while (it.hasNext()) {
                SignerInformation signer = (SignerInformation) it.next();
                SignerId signerConstraints = signer.getSID();
                Collection certCollection = certs.getCertificates(signerConstraints);

                X509Certificate cert;
                for (Object aCertCollection : certCollection) {
                    cert = (X509Certificate) aCertCollection;

                    if (new Date().after(cert.getNotAfter())) { //Проверка даты по на валидность
                        throw new DigiSignException("The certificate is expired");
                    }

                    validateCertificateType(cert, operationType);
                    verifyTrustChain(cert);//Проверка цепочки доверия
                    isCertificeteRevoked(cert);//Проверка, не отозван ли сертификат пользователя
                    result = result && signer.verify(cert, provider.getName());
                }
            }

        } catch (CertificateException | NoSuchAlgorithmException | CMSException | CertStoreException | IOException | NoSuchProviderException e) {
            e.printStackTrace();
            throw new DigiSignException(e.getMessage(), e);
        }
        return result;
    }*/

    private void validateCertificateType(X509Certificate cert, int operationType) throws DigiSignException {
        if (operationType == OPERATION_TYPE_AUTH && isSignCert(cert))//Если выполняется авторизация и выбран сертификат RSA
            throw new DigiSignException("Wrong certificate type. Must be AUTH, not RSA", DIGISIGN_ERROR_MUST_BE_AUTH_CERTIFICATE, "wrong_certificate_type_must_be_auth_message");
        if (operationType == OPERATION_TYPE_SIGN && isAuthCert(cert))//Если выполняется подписание и выбран сертификат AUTH
            throw new DigiSignException("Wrong certificate type. Must be RSA, not AUTH", DIGISIGN_ERROR_MUST_BE_RSA_CERTIFICATE, "wrong_certificate_type_must_be_rsa_message");
    }

    public String getPEMCertificate(Certificate certificate) {
        StringBuffer result = new StringBuffer();
        try {
            String cert_begin = "-----BEGIN CERTIFICATE-----\n";
            String end_cert = "\n-----END CERTIFICATE-----";
            String pemCertPre = new String(Base64.encode(certificate.getEncoded()));
            result.append(cert_begin).append(pemCertPre).append(end_cert);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    public SignatureInfo signString(String soap) {
//        Provider provider = new KalkanProvider();
//        Security.addProvider(provider);
        SignatureInfo result = new SignatureInfo();
        try {
//            File file = keystoreFile.getFile(keystoreFile);
//            String keystoreFileName = new StringBuffer(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getParentFile().getPath()).append(CRL_FILE_DIR).append(digisignConfigProperties.getKeystoreFileName()).toString();
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            keystore.load(resource.getInputStream(), digisignConfigProperties.getKeystoreFilePassword().toCharArray());
//            keystore.load(new FileInputStream(new ClassPathResource(digisignConfigProperties.getKeystoreFileName()).getFile()), digisignConfigProperties.getKeystoreFilePassword().toCharArray());
            Enumeration<String> enumeration = keystore.aliases();
//            enumeration.hasMoreElements();
            String alias = enumeration.nextElement();
            PrivateKey key = (PrivateKey) keystore.getKey(alias, digisignConfigProperties.getKeystoreFilePassword().toCharArray());
            Certificate certificate = keystore.getCertificate(alias);
            System.out.print(certificate.getPublicKey().toString());
            Signature signature = Signature.getInstance(digisignConfigProperties.getSignAlgName());
            signature.initSign(key);
            signature.update(soap.getBytes());
            result.setSignature(new String(Hex.encodeHex(signature.sign())));
            result.setX509Certificate(getPEMCertificate(certificate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Boolean verifySignature(byte[] document, String signatureString, String publicCertificate, int operationType) throws DigiSignException {
        boolean result;
        try {
//            Provider provider = new KalkanProvider();
//            Security.addProvider(provider);

            ByteArrayInputStream in = new ByteArrayInputStream(publicCertificate.getBytes(StandardCharsets.UTF_8));
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(in);

            validateCertificateType(cert, operationType);

            if (new Date().after(cert.getNotAfter())) { //Проверка даты по на валидность
                throw new DigiSignException("The certificate is expired");
            }

            verifyTrustChain(cert);//Проверка цепочки доверия

            isCertificeteRevoked(cert, "rsa.crl");//Проверка, не отозван ли сертификат пользователя

//            Signature signature = Signature.getInstance(cert.getSigAlgName(), provider.getName());
            Signature signature = Signature.getInstance(cert.getSigAlgName());
            signature.initVerify(cert);
            signature.update(document);
            result = signature.verify(DatatypeConverter.parseHexBinary(signatureString.replaceAll("\n", "")));//Проверка цифровой подписи
        } catch (CertificateException e) {
            e.printStackTrace();
            throw new DigiSignException(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new DigiSignException(e.getMessage(), e);
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new DigiSignException(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new DigiSignException(e.getMessage(), e);
        } catch (DigiSignException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    private X509CRL downloadCRL(String crlURL)
            throws IOException, CertificateException,
            CRLException {
        URL url = new URL(crlURL);
        InputStream crlStream = url.openStream();
        return loadCrlFromStream(crlStream);
    }

    private X509CRL loadFromFile(String src)
            throws IOException, CertificateException,
            CRLException {
        InputStream crlStream = new FileInputStream(src);
        return loadCrlFromStream(crlStream);
    }

    private X509CRL loadCrlFromStream(InputStream crlStream) throws CertificateException, CRLException, IOException {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509CRL crl = (X509CRL) cf.generateCRL(crlStream);
            return crl;
        } finally {
            crlStream.close();
        }
    }


    private static final Logger logger = LoggerFactory.getLogger(DigisignProcessorImpl.class);

    //@Scheduled(cron = "0 0 6 * * *")
    public void updateCrlFile() {
        try {
            replaceFile(CRL_FILE_URL, new StringBuffer(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getParentFile().getPath()).append(CRL_FILE_DIR).append(CRL_FILE_NAME).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@Scheduled(cron = "0 0 0/2 * * *")
    public void updateDeltaCrlFile() {
        try {
            replaceFile(DELTA_CRL_FILE_URL, new StringBuffer(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getParentFile().getPath()).append(CRL_FILE_DIR).append(DELTA_CRL_FILE_NAME).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void replaceFile(String srcUrl, String dstFileName) {
        try {
            URL link = new URL(srcUrl); //The file that you want to download

            InputStream in = new BufferedInputStream(link.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();

            File crlFile = new File(dstFileName);
            if (crlFile.exists())
                crlFile.delete();

            FileOutputStream fos = new FileOutputStream(dstFileName);
            fos.write(response);
            fos.close();

            //System.out.println("Finished");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void isCertificeteRevoked(X509Certificate cert, String crlFileName)
            throws DigiSignException {
        try {
//                X509CRL crl = downloadCRL("http://crl.pki.gov.kz/rsa.crl");
            X509CRL crl = loadFromFile(new StringBuffer(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getParentFile().getPath()).append(CRL_FILE_DIR).append(crlFileName).toString());
            if (crl.isRevoked(cert)) {
                throw new DigiSignException(new StringBuffer("Certificate with id = ").append(cert.getSerialNumber()).append(" is revoked").toString(), DIGISIGN_ERROR_CODE_CERT_REVOKED, DIGISIGN_ERROR_REVOKED_MESSAGE_KEY);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (ex instanceof DigiSignException) {
                throw (DigiSignException) ex;
            } else {
                throw new DigiSignException(
                        "Can not verify CRL for certificate: " +
                                cert.getSubjectX500Principal() + ", original exception = " + ex.getMessage());
            }
        }
    }

    private void isCertificeteRevoked(X509Certificate cert)
            throws DigiSignException {
        for (String crlFileName : CRL_FILE_NAMES) {
            isCertificeteRevoked(cert, crlFileName);
        }
    }

}