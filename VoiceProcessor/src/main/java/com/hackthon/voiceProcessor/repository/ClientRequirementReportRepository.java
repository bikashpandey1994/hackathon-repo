package com.hackthon.voiceProcessor.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRequirementReportRepository extends CrudRepository<ClientRequirementReportEntity, String>{

}
