package edu.nuist.codehelper.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class ProjService {
    @Autowired
    private ProjReps reps;

    public Proj save(Proj proj){
        return reps.save(proj);
    }
    public List<Proj> findAllByAccount(String account){
        return reps.findAllByAccount(account);
    }
    public Proj findByProjid(String projid){
        return reps.findByProjid(projid);
    }
    @Transactional
    public void delete(String projid){
        reps.delete(projid);
    }

    @Transactional
    public void update(String projname,String content, String projid) {
        reps.update(projname,content, projid);
    }
}
