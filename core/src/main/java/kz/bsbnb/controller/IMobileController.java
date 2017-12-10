package kz.bsbnb.controller;

import kz.bsbnb.util.SimpleResponse;

/**
 * Created by Olzhas.Pazyldayev on 10.12.2017
 */
public interface IMobileController {

    //****************************************** FAQ ************************************//

    SimpleResponse getFaqPosts(String term, int page, int pageSize);

}
