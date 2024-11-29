package com.gmail.merikbest2015.service.impl;

import com.gmail.merikbest2015.commons.exception.ApiRequestException;
import com.gmail.merikbest2015.constants.ServiceJobErrorMessage;
import com.gmail.merikbest2015.constants.ServiceJobSuccessMessage;
import com.gmail.merikbest2015.dto.ServiceJobRequest;
import com.gmail.merikbest2015.model.ServiceJob;
import com.gmail.merikbest2015.repository.ServiceJobRepository;
import com.gmail.merikbest2015.service.ServiceJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceJobServiceImpl implements ServiceJobService {

    private final ServiceJobRepository serviceJobRepository;

    @Override
    public List<ServiceJob> getServiceJobs() {
        return serviceJobRepository.findAll();
    }

    @Override
    public ServiceJob getServiceJobById(Long serviceJobId) {
        return getServiceJob(serviceJobId);
    }

    @Override
    @Transactional
    public ServiceJob createServiceJob(ServiceJobRequest serviceJobRequest) {
        ServiceJob serviceJob = new ServiceJob();
        serviceJob.setJobName(serviceJobRequest.getJobName());
        serviceJob.setCronExpression(serviceJobRequest.getCronExpression());
        serviceJob.setActive(true);
        return serviceJobRepository.save(serviceJob);
    }

    @Override
    @Transactional
    public ServiceJob updateServiceJob(Long serviceJobId, ServiceJobRequest serviceJobRequest) {
        ServiceJob serviceJob = getServiceJob(serviceJobId);
        serviceJob.setJobName(serviceJobRequest.getJobName());
        serviceJob.setCronExpression(serviceJobRequest.getCronExpression());
        serviceJob.setActive(serviceJobRequest.isActive());
        return serviceJob;
    }

    @Override
    @Transactional
    public String deleteServiceJob(Long serviceJobId) {
        ServiceJob serviceJob = getServiceJob(serviceJobId);
        serviceJobRepository.delete(serviceJob);
        return ServiceJobSuccessMessage.SERVICE_JOB_DELETED;
    }

    private ServiceJob getServiceJob(Long serviceJobId) {
        return serviceJobRepository.findById(serviceJobId)
                .orElseThrow(() -> new ApiRequestException(ServiceJobErrorMessage.SERVICE_JOB_NOT_FOUND, HttpStatus.NOT_FOUND));
    }
}
