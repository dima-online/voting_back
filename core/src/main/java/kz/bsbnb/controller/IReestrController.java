package kz.bsbnb.controller;

import kz.bsbnb.common.external.Reestr;
import kz.bsbnb.common.external.ReestrHead;
import kz.bsbnb.util.SimpleResponse;

import java.util.List;

/**
 * Created by Ruslan on 15.11.2016.
 */
public interface IReestrController {
    SimpleResponse newHead(ReestrHead reestrHead);

    SimpleResponse newList(Long reestrHeadId,  List<Reestr> list);

    SimpleResponse getList(Long reestrHeadId);

    SimpleResponse getHeadList(String iin);

    SimpleResponse getList(Long reestrHeadId, Long votingId);

    SimpleResponse getHeadList(Long votingId);
}
