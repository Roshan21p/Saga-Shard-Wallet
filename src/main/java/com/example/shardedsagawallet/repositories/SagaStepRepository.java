package com.example.shardedsagawallet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.shardedsagawallet.schema.SagaStep;

public interface SagaStepRepository extends JpaRepository<SagaStep, Long> {
    
    List<SagaStep> findBySagaInstanceId(Long sagaInstanceId);

    @Query ("SELECT s FROM SagaStep s WHERE s.sagaInstance.id = :sagaInstanceId AND s.status = 'COMPLETED'")
    List<SagaStep> findCompletedStepBySagaInstanceId( @Param ("sagaInstanceId") Long sagaInstanceId);

    @Query ("SELECT s FROM SagaStep s WHERE s.sagaInstance.id = :sagaInstanceId AND s.status IN ('COMPLETED', 'COMPENSATED')")
    List<SagaStep> findCompletedOrCompensatedStepBySagaInstanceId( @Param ("sagaInstanceId") Long sagaInstanceId);
}
