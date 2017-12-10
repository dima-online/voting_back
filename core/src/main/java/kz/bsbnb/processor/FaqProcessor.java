package kz.bsbnb.processor;

import kz.bsbnb.common.model.FaqItem;
import kz.bsbnb.common.model.FaqPost;
import kz.bsbnb.util.SimpleResponse;

import java.util.List;

/**
 * Created by Olzhas.Pazyldayev on 10.12.2017
 */
public interface FaqProcessor {
    SimpleResponse getFaqItemsPage(String term, int page, int pageSize);

    SimpleResponse getFaqPost(Long id);

    SimpleResponse updateFaqItem(Long id, FaqItem item);

    SimpleResponse deleteFaqItem(Long id);

    SimpleResponse saveFaqItem(Long postId, FaqItem item);

    SimpleResponse saveFaqPostWithItems(List<FaqItem> items);

    SimpleResponse updateFaqPost(Long postId, FaqPost item);

    SimpleResponse deleteFaqPost(Long postId);

    SimpleResponse getFaqPostListPage(String term, int page, int pageSize);
}
