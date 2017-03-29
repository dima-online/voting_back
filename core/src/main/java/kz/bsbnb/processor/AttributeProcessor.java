package kz.bsbnb.processor;

import kz.bsbnb.common.model.Attribute;

import java.util.List;

/**
 * Created by ruslan on 10/10/2016.
 */
public interface AttributeProcessor {

    List<Attribute> merge(String object, Long objectId, List<Attribute> list);

    String getValue(List<Attribute> list, String typeValue);
}
