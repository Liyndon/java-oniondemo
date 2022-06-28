package com.dustess.oniondemo.service;

import com.dustess.oniondemo.interfaces.TimeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LocalTimeService implements TimeService {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
