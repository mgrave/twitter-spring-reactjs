package com.gmail.merikbest2015.service.impl;

import com.gmail.merikbest2015.repository.ServiceJobRepository;
import com.gmail.merikbest2015.service.ServiceJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceJobServiceImpl implements ServiceJobService {

    private final ServiceJobRepository serviceJobRepository;



}
