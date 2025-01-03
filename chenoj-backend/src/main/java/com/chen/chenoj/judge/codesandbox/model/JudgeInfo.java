package com.chen.chenoj.judge.codesandbox.model;

import lombok.Data;

/**
 * 判题信息
 */
@Data
public class JudgeInfo {

    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 消耗内存（kb）
     */
    private Long memory;

    /**
     * 消耗时间（ms）
     */
    private Long time;

    /**
     * 通过的案例数
     */
    private Integer passNum;

    /**
     * 总案例数
     */
    private Integer totalNum;
}
