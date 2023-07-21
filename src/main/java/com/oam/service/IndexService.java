package com.oam.service;

import com.oam.model.Index;
import com.oam.repository.IndexRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexService {

    private final IndexRepository indexRepository;

    public Index create(Index index) {
        return indexRepository.save(index);
    }
}
