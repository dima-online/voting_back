package kz.bsbnb.digisign.processor;

import kz.bsbnb.digisign.model.DigisignResponse;

/**
 * Created by Olzhas.Pazyldayev on 08.09.2017.
 */
public interface DigisignRestProcessor {

    DigisignResponse verifySignature(String document, String signatureString, String publicCertificate, int operationType);

    DigisignResponse verifyNCASignature(String document, String signatureString, int operationType);

    DigisignResponse verifySignature(byte[] document, String signatureString, String publicCertificate, int operationType);

    DigisignResponse verifyNCASignature(byte[] document, String signatureString, int operationType);
}
