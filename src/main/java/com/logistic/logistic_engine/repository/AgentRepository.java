package com.logistic.logistic_engine.repository;

import com.logistic.logistic_engine.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.StackWalker.Option;
import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent, Long> {

    Optional<Agent> findByUserId(Long userId);
    Optional<Agent> findById(Long id);
}