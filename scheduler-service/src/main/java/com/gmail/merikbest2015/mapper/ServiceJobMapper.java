package com.gmail.merikbest2015.mapper;

import com.gmail.merikbest2015.commons.mapper.BasicMapper;
import com.gmail.merikbest2015.dto.ServiceJobRequest;
import com.gmail.merikbest2015.dto.ServiceJobResponse;
import com.gmail.merikbest2015.model.ServiceJob;
import com.gmail.merikbest2015.service.ServiceJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ServiceJobMapper {

    private final BasicMapper basicMapper;
    private final ServiceJobService serviceJobService;

    public List<ServiceJobResponse> getServiceJobs() {
        return basicMapper.convertToResponseList(serviceJobService.getServiceJobs(), ServiceJobResponse.class);
    }

    public ServiceJobResponse getServiceJobById(Long serviceJobId) {
        return basicMapper.convertToResponse(serviceJobService.getServiceJobById(serviceJobId), ServiceJobResponse.class);
    }

    public ServiceJobResponse createServiceJob(ServiceJobRequest serviceJobRequest) {
        return basicMapper.convertToResponse(serviceJobService.createServiceJob(serviceJobRequest), ServiceJobResponse.class);
    }

    public ServiceJobResponse updateServiceJob(Long serviceJobId, ServiceJobRequest serviceJobRequest) {
        ServiceJob serviceJob = serviceJobService.updateServiceJob(serviceJobId, serviceJobRequest);
        return basicMapper.convertToResponse(serviceJob, ServiceJobResponse.class);
    }

    public String deleteServiceJob(Long serviceJobId) {
        return serviceJobService.deleteServiceJob(serviceJobId);
    }
}
