package com.landlordcommunication.web.repositories;

import com.landlordcommunication.web.models.Residence;

import java.util.List;

public interface ResidenceRepository {

    List<Residence> getResidencesByUser(int userId);
}
