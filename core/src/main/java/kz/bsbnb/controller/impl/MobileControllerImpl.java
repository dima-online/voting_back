package kz.bsbnb.controller.impl;

import kz.bsbnb.controller.IMobileController;
import kz.bsbnb.processor.FaqProcessor;
import kz.bsbnb.processor.MessageProcessor;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Olzhas.Pazyldayev on 10.12.2017
 */
@RestController
@RequestMapping(value = "/mobile")
public class MobileControllerImpl implements IMobileController {

    @Autowired
    MessageProcessor messageProcessor;
    @Autowired
    FaqProcessor faqProcessor;


    //****************************************** FAQ ************************************//

    @Override
    @RequestMapping("/faq")
    public SimpleResponse getFaqPosts(@RequestParam(defaultValue = "") String term,
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int pageSize) {
        return faqProcessor.getFaqPostListPage(term, page, pageSize);
    }

}