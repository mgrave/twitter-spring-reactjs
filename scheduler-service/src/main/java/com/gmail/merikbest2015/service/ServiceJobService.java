package com.gmail.merikbest2015.service;

import com.gmail.merikbest2015.dto.ServiceJobRequest;
import com.gmail.merikbest2015.model.ServiceJob;

import java.util.List;

public interface ServiceJobService {
    List<ServiceJob> getServiceJobs();
    ServiceJob getServiceJobById(Long serviceJobId);
    ServiceJob createServiceJob(ServiceJobRequest serviceJobRequest);
    ServiceJob updateServiceJob(Long serviceJobId, ServiceJobRequest serviceJobRequest);
    String deleteServiceJob(Long serviceJobId);
}
