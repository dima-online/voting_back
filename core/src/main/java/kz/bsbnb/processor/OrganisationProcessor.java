package kz.bsbnb.processor;

import kz.bsbnb.common.model.Organisation;

import java.util.List;

/**
 * Created by ruslan on 10/10/2016.
 */
public interface OrganisationProcessor {

    void mergeOrganisation(Organisation organisation);

    List<Organisation> getOrganisations(int page, int count);

}
