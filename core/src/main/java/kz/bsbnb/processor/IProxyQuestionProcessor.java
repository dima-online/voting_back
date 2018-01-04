package kz.bsbnb.processor;

import kz.bsbnb.common.bean.ProxyQuestionBean;
import kz.bsbnb.common.model.ProxyQuestion;
import kz.bsbnb.util.SimpleResponse;

import java.util.List;

/**
 * Created by serik.mukashev on 28.12.2017.
 */
public interface IProxyQuestionProcessor {

    List<ProxyQuestionBean> getListByVoter(Long voterId);

    SimpleResponse saveProxyQuestions(Long parentVoterId,
                                      Long executiveVoterId,
                                      List<ProxyQuestionBean> proxyQuestions);
}
