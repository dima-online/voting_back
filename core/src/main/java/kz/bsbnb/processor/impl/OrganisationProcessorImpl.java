package kz.bsbnb.processor.impl;

import kz.bsbnb.common.model.Organisation;
import kz.bsbnb.processor.OrganisationProcessor;
import kz.bsbnb.repository.IOrganisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ruslan on 10/10/2016.
 */
@Service
public class OrganisationProcessorImpl implements OrganisationProcessor {
    @Autowired
    private IOrganisationRepository organisationRepository;

    public void mergeOrganisation(Organisation organisation) {


    }

    @Override
    public List<Organisation> getOrganisations(int page, int count) {
        return organisationRepository.findAll(new PageRequest(page,count)).getContent();
    }
}
