package kz.bsbnb.digisign.processor.impl;

import kz.bsbnb.digisign.model.Code;
import kz.bsbnb.digisign.model.DigisignResponse;
import kz.bsbnb.digisign.processor.DigisignRestProcessor;
import kz.bsbnb.digisign.util.DigisignConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by Olzhas.Pazyldayev on 08.09.2017.
 */
@Service
public class DigisignRestProcessorImpl implements DigisignRestProcessor {
    @Autowired
    DigisignConfigProperties digisignConfigProperties;

    @Override
    public DigisignResponse verifySignature(String document, String signatureString, String publicCertificate, int operationType) {

        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
            MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            params.add("document", document);
            params.add("signatureString", signatureString);
            params.add("publicCertificate", publicCertificate);
            params.add("operationType", operationType);

            return restTemplate.postForObject(digisignConfigProperties.getVerifyUrl() + "/verify", params, DigisignResponse.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return new DigisignResponse(false, Code.INVALID, Code.INVALID.name());
    }

    @Override
    public DigisignResponse verifyNCASignature(String document, String signatureString, int operationType) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
            MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            params.add("document", document);
            params.add("signatureString", signatureString);
            params.add("operationType", operationType);

            return restTemplate.postForObject(digisignConfigProperties.getVerifyUrl() + "/verify/nca", params, DigisignResponse.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return new DigisignResponse(false, Code.INVALID, Code.INVALID.name());
    }

    @Override
    public DigisignResponse verifySignature(byte[] document, String signatureString, String publicCertificate, int operationType) {
        return verifySignature(new String(document, StandardCharsets.UTF_8), signatureString, publicCertificate, operationType);
    }

    @Override
    public DigisignResponse verifyNCASignature(byte[] document, String signatureString, int operationType) {
        return verifyNCASignature(new String(document, StandardCharsets.UTF_8), signatureString, operationType);
    }
}