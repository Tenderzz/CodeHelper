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
@Table(name = "advice")
public class Advice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String account; //用户账号
    private String username; //用户昵称
    private String content;
    @Column(name="createtime",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false,updatable = false)
    @CreatedDate
    private Timestamp createtime;


}
