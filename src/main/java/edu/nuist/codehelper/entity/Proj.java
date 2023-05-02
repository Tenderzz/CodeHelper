package edu.nuist.codehelper.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "project")
public class Proj {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String account; //用户账号
    private String projid;//项目编号 随机生成
    private String projname;//项目名

    @Column(columnDefinition="TEXT")
    private String content;//节点内容

    @Column(name="createtime",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false,updatable = false)
    @CreatedDate
    private Timestamp createtime;


}
