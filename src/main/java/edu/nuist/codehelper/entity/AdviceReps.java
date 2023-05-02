package edu.nuist.codehelper.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdviceReps extends JpaRepository<Advice,Long> {
    Advice save(Advice advice);
}
