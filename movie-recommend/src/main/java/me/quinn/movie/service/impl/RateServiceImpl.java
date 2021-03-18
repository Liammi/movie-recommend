package me.quinn.movie.service.impl;

import me.quinn.movie.domain.Rate;
import me.quinn.movie.repository.RateRepository;
import me.quinn.movie.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RateServiceImpl implements RateService {
    @Autowired
    public RateServiceImpl(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }
    private RateRepository rateRepository;
    @Override
    public Rate save(Rate rate) {
        return rateRepository.save(rate);
    }

    @Override
    public List<Rate> findByUserId(Long userId) {
        return rateRepository.findByUserId(userId);
    }

    @Override
    public List<?> findAllGroupByMovieId() {
        return rateRepository.findRateCount();
    }
}
