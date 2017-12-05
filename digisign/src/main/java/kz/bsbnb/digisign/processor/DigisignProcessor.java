package kz.bsbnb.digisign.processor;

import kz.bsbnb.digisign.util.DigiSignException;
import kz.bsbnb.digisign.util.SignatureInfo;

/**
 * Created by Roman.Rychkov on 28.10.2016.
 */
public interface DigisignProcessor {

    int DIGISIGN_ERROR_CODE_CERT_REVOKED = 1;
    int DIGISIGN_ERROR_MUST_BE_RSA_CERTIFICATE = 2;
    int DIGISIGN_ERROR_MUST_BE_AUTH_CERTIFICATE = 3;
    int DIGISIGN_ERROR_EXPIRED = 4;
    String DIGISIGN_ERROR_EXPIRED_MESSAGE_KEY = "cert_experied_message";
    String DIGISIGN_ERROR_REVOKED_MESSAGE_KEY = "cert_revoked_message";
    int OPERATION_TYPE_SIGN = 2;
    int OPERATION_TYPE_AUTH = 3;
    String CRL_FILE_DIR = "/resources/main/";
    String CRL_FILE_NAME = "rsa.crl";
    String DELTA_CRL_FILE_NAME = "d_rsa.crl";
    String[] CRL_FILE_NAMES = new String[]{CRL_FILE_NAME, DELTA_CRL_FILE_NAME};
    String CRL_FILE_URL = "http://crl.pki.gov.kz/rsa.crl";
    String DELTA_CRL_FILE_URL = "http://crl.pki.gov.kz/d_rsa.crl";

    Boolean verifySignature(String document, String signatureString, String publicCertificate, int operationType) throws DigiSignException;

    //    Boolean verifyNCASignature(String document, String signatureString, int operationType) throws DigiSignException;
//    Boolean verifyNCASignature(byte[] document, String signatureString, int operationType) throws DigiSignException;
    Boolean verifySignature(byte[] document, String signatureString, String publicCertificate, int operationType) throws DigiSignException;

    SignatureInfo signString(String soap);
}
