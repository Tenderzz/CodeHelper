package edu.nuist.codehelper.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.nuist.codehelper.utils.ListToStringConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.sql.Timestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "user")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String account; //用户账号
    private String password;
    private String username; //用户昵称
    private String phone;
    private String email;
    private String photoUrl; //头像地址

    @Column(name="createtime",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false,updatable = false)
    @CreatedDate
    private Timestamp createtime;

    //默认为1 状态0为禁用状态 无法登陆
    private int status = 1;

}