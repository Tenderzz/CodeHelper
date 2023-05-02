package edu.nuist.codehelper.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NodeWrapper {
    private int id;
    private int pid;
    private String content;
    private int indent;
}
