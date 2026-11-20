package com.example.shardedsagawallet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shardedsagawallet.schema.SagaInstance;

public interface SagaInstanceRepository extends JpaRepository<SagaInstance, Long> {
    
}
