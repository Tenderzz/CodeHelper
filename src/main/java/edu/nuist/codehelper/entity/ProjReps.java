package edu.nuist.codehelper.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProjReps extends JpaRepository<Proj,Long> {
    Proj save(Proj proj);
    List<Proj> findAllByAccount(String account);
    Proj findByProjid(String projid);
    @Transactional
    @Modifying
    @Query(value = "delete from project where projid=?1 ", nativeQuery = true)
    void delete(String projid);

    @Transactional
    @Modifying
    @Query(value = "update project set projname=?1 , content=?2 where projid=?3", nativeQuery = true)
    void update(String projname,String content,String projid);
}
