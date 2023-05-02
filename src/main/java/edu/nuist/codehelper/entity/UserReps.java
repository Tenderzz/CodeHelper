package edu.nuist.codehelper.entity;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserReps extends JpaRepository<User,Long> {
    User save(User user);

    User findByAccount(String account);

    @Transactional
    @Modifying
    @Query(value = "delete from user where id=?1 ", nativeQuery = true)
    void deleteById(long id);

    @Transactional
    @Modifying
    @Query(value = "update user set password= ?1 where account=?2 ", nativeQuery = true)
    void updateByAccount(String password,String account);

}
