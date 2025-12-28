package com.example.demo.service.impl;
import com.example.demo.entity.ServiceCounter;
import com.example.demo.repository.ServiceCounterRepository;
import com.example.demo.service.ServiceCounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceCounterServiceImpl implements ServiceCounterService {
    private final ServiceCounterRepository repo;
    @Override
    public ServiceCounter addCounter(ServiceCounter c) { return repo.save(c); }
    @Override
    public List<ServiceCounter> getActiveCounters() { return repo.findByIsActiveTrue(); }
}