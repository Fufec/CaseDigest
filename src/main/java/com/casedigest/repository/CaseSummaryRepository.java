package com.casedigest.repository;

import com.casedigest.model.CaseSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseSummaryRepository extends JpaRepository<CaseSummary, Long> {

    List<CaseSummary> findAllByOrderByCreatedAtDesc();
}