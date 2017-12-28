package kz.bsbnb.processor;

import kz.bsbnb.common.model.ProxyQuestion;

import java.util.List;

/**
 * Created by serik.mukashev on 28.12.2017.
 */
public interface IProxyQuestionProcessor {
    List<ProxyQuestion> getList(Long votingId, Long voterId);

    List<ProxyQuestion> getListByVoter(Long voterId);
}
