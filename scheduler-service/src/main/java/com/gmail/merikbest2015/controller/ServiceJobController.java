package com.gmail.merikbest2015.controller;

import com.gmail.merikbest2015.commons.constants.PathConstants;
import com.gmail.merikbest2015.dto.ServiceJobRequest;
import com.gmail.merikbest2015.dto.ServiceJobResponse;
import com.gmail.merikbest2015.mapper.ServiceJobMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.API_V1_SCHEDULER)
public class ServiceJobController {

    private final ServiceJobMapper serviceJobMapper;

    @GetMapping(PathConstants.SERVICE_JOBS)
    public ResponseEntity<List<ServiceJobResponse>> getServiceJobs() {
        return ResponseEntity.ok(serviceJobMapper.getServiceJobs());
    }

    @GetMapping(PathConstants.SERVICE_JOB_ID)
    public ResponseEntity<ServiceJobResponse> getServiceJobById(@PathVariable("serviceJobId") Long serviceJobId) {
        return ResponseEntity.ok(serviceJobMapper.getServiceJobById(serviceJobId));
    }

    @PostMapping(PathConstants.SERVICE_JOB)
    public ResponseEntity<ServiceJobResponse> createServiceJob(@RequestBody ServiceJobRequest serviceJobRequest) {
        return ResponseEntity.ok(serviceJobMapper.createServiceJob(serviceJobRequest));
    }

    @PutMapping(PathConstants.SERVICE_JOB_ID)
    public ResponseEntity<ServiceJobResponse> updateServiceJob(@PathVariable("serviceJobId") Long serviceJobId,
                                                               @RequestBody ServiceJobRequest serviceJobRequest) {
        return ResponseEntity.ok(serviceJobMapper.updateServiceJob(serviceJobId, serviceJobRequest));
    }

    @DeleteMapping(PathConstants.SERVICE_JOB_ID)
    public ResponseEntity<String> deleteServiceJob(@PathVariable("serviceJobId") Long serviceJobId) {
        return ResponseEntity.ok(serviceJobMapper.deleteServiceJob(serviceJobId));
    }
}
