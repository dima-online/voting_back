package kz.bsbnb.processor.impl;

import kz.bsbnb.common.model.FaqItem;
import kz.bsbnb.common.model.FaqPost;
import kz.bsbnb.common.model.Status;
import kz.bsbnb.common.model.User;
import kz.bsbnb.processor.FaqProcessor;
import kz.bsbnb.processor.MessageProcessor;
import kz.bsbnb.processor.SecurityProcessor;
import kz.bsbnb.repository.IFaqPostRepository;
import kz.bsbnb.repository.IFaqRepository;
import kz.bsbnb.util.SimpleResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Olzhas.Pazyldayev on 10.12.2017
 */
@Service
public class FaqProcessorImpl implements FaqProcessor {
    @Autowired
    IFaqRepository faqRepository;

    @Autowired
    IFaqPostRepository faqPostRepository;

    @Autowired
    EntityManager em;

    @Autowired
    SecurityProcessor securityProcessor;
    @Autowired
    MessageProcessor messageProcessor;

    @Override
    public SimpleResponse getFaqPost(Long id) {
        FaqPost item = faqPostRepository.findOne(id);
        if (item == null)
            return new SimpleResponse("Post not found").ERROR();

        return new SimpleResponse(item).SUCCESS();
    }

    @Override
    public SimpleResponse updateFaqItem(Long id, FaqItem data) {
        FaqItem item = faqRepository.findOne(id);
        if (item == null)
            return new SimpleResponse("Item not found").ERROR();

        item.setTitle(data.getTitle());
        item.setDescription(data.getDescription());
        item.setStatus(data.getStatus());
        faqRepository.save(item);
        return new SimpleResponse(item).SUCCESS();
    }

    @Override
    public SimpleResponse deleteFaqItem(Long id) {
        try {
            faqRepository.delete(id);
        } catch (Exception e) {
            return new SimpleResponse(e).ERROR();
        }

        return new SimpleResponse("ok").SUCCESS();
    }

    @Override
    public SimpleResponse saveFaqItem(Long postId, FaqItem item) {
        FaqPost post = faqPostRepository.findOne(postId);
        if (post == null) {
            return new SimpleResponse("faq.error.post.not.found");
        }
        try {
            User user = securityProcessor.getLoggedUser();
            item.setPost(post);
            item.setAuthor(user);
            item.setCreateTime(new Date());
            faqRepository.save(item);
        } catch (Exception e) {
            return new SimpleResponse(e).ERROR();
        }

        return new SimpleResponse(item).SUCCESS();
    }

    @Override
    public SimpleResponse saveFaqPostWithItems(List<FaqItem> items) {
        try {
            User user = securityProcessor.getLoggedUser();
            FaqPost post = new FaqPost();
            post.setCreateTime(new Date());
            post.setAuthor(user);

            items.forEach(faqItem -> {
                faqItem.setPost(post);
                faqItem.setAuthor(user);
                faqItem.setCreateTime(new Date());
                post.getItems().add(faqItem);
            });

            faqPostRepository.save(post);
        } catch (Exception e) {
            e.printStackTrace();
            return new SimpleResponse(e.getMessage()).ERROR();
        }

        return new SimpleResponse(Status.SUCCESS.name()).SUCCESS();
    }


    @Override
    public SimpleResponse updateFaqPost(Long postId, FaqPost data) {
        FaqPost post = faqPostRepository.findOne(postId);
        if (post == null)
            return new SimpleResponse("Post not found").ERROR();

        post.setStatus(data.getStatus());
        faqPostRepository.save(post);
        return new SimpleResponse(post).SUCCESS();
    }

    @Override
    public SimpleResponse deleteFaqPost(Long postId) {
        try {
            faqPostRepository.delete(postId);
        } catch (Exception e) {
            return new SimpleResponse(e).ERROR();
        }

        return new SimpleResponse(Status.SUCCESS.name()).SUCCESS();
    }

    @Override
    public SimpleResponse getFaqPostListPage(String term, int page, int pageSize) {
        List statuses = Arrays.asList(Status.BLOCKED, Status.DELETED);
        List<FaqPost> list;
        Long numElements = 0L;
        try {
            if (StringUtils.isNotBlank(term)) {
                numElements = (Long) em.createQuery("select count(distinct fp.id) from FaqPost fp " +
                        "left join fp.items fi " +
                        "where fp.status not in (:statuses) and lower(fi.title) like :term ")
                        .setParameter("statuses", statuses)
                        .setParameter("term", "%" + term.toLowerCase() + "%")
                        .getSingleResult();
                list = em.createQuery("select fp from FaqPost fp " +
                        "left join fetch fp.items fi " +
                        "where fp.status not in (:statuses) and lower(fi.title) like :term " +
                        "order by fp.id asc")
                        .setParameter("statuses", statuses)
                        .setParameter("term", "%" + term.toLowerCase() + "%")
                        .setFirstResult((page - 1) * pageSize)
                        .setMaxResults(pageSize)
                        .getResultList();
            } else {
                numElements = (Long) em.createQuery("select count(fp.id) from FaqPost fp " +
                        "where fp.status not in (:statuses)")
                        .setParameter("statuses", statuses)
                        .getSingleResult();
                list = em.createQuery("select fp from FaqPost fp " +
                        "left join fetch fp.items fi " +
                        "where fp.status not in (:statuses) " +
                        "order by fp.id asc")
                        .setParameter("statuses", statuses)
                        .setFirstResult((page - 1) * pageSize)
                        .setMaxResults(pageSize)
                        .getResultList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            list = new ArrayList<>();
        }
        return new SimpleResponse(new PageImpl<>(list, new PageRequest(page - 1, pageSize), numElements)).SUCCESS();
    }

    @Override
    public SimpleResponse getFaqItemsPage(String term, int page, int pageSize) {
        List statuses = Arrays.asList(Status.ACTIVE);
        if (StringUtils.isNotBlank(term)) {
            return new SimpleResponse(faqRepository.findByTitleContainingIgnoreCaseAndStatusInOrderByIdAsc(term, statuses, new PageRequest(page - 1, pageSize)));
        } else {
            return new SimpleResponse(faqRepository.findByStatusInOrderByIdAsc(statuses, new PageRequest(page - 1, pageSize)));
        }
    }
}
