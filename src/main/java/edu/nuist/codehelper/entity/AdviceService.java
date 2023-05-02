package edu.nuist.codehelper.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdviceService {
    @Autowired
    AdviceReps reps;

    public Advice save(Advice advice) {
        return reps.save(advice);
    }

}
