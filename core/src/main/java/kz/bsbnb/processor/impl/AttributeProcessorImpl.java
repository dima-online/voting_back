package kz.bsbnb.processor.impl;

import kz.bsbnb.common.model.Attribute;
import kz.bsbnb.processor.AttributeProcessor;
import kz.bsbnb.repository.IAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruslan on 10/10/2016.
 */
@Service
public class AttributeProcessorImpl implements AttributeProcessor {

    @Autowired
    IAttributeRepository attributeRepository;

    @Override
    public List<Attribute> merge(String object, Long objectId, List<Attribute> list) {
        List<Attribute> attr = attributeRepository.findByObjectAndObjectId(object,objectId);
        List<Attribute> result = new ArrayList<>();
        for (Attribute attribute:list) {
            boolean isFound = false;
            for (Attribute next:attr) {
                if (attribute.getTypeValue().equals(next.getTypeValue())) {
                    isFound = true;
                    next.setValue(attribute.getValue());
                    next= attributeRepository.save(next);
                    result.add(next);
                    break;
                }
            }
            if (!isFound) {
                attribute.setObject(object);
                attribute.setObjectId(objectId);
                attribute=attributeRepository.save(attribute);
                result.add(attribute);
            }
        }
        return result;
    }

    @Override
    public String getValue(List<Attribute> list, String typeValue) {
        String result = null;
        for (Attribute attribute:list) {
            if (attribute.getTypeValue().equals(typeValue)) {
                result = attribute.getValue();
                break;
            }
        }
        return result;
    }
}
