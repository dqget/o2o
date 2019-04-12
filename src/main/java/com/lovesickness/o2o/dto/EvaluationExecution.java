package com.lovesickness.o2o.dto;

import com.lovesickness.o2o.entity.Evaluation;
import com.lovesickness.o2o.enums.EvaluationStateEnum;

import java.util.List;

public class EvaluationExecution {
    //结果状态
    private int state;
    private int count;
    private Evaluation evaluation;
    //状态标识
    private String stateInfo;

    private List<Evaluation> evaluationList;

    public EvaluationExecution() {
    }

    //失败
    public EvaluationExecution(EvaluationStateEnum evaluationStateEnum) {
        this.state = evaluationStateEnum.getState();
        this.stateInfo = evaluationStateEnum.getStateInfo();
    }

    //成功
    public EvaluationExecution(EvaluationStateEnum evaluationStateEnum, List<Evaluation> evaluationList) {
        this.state = evaluationStateEnum.getState();
        this.stateInfo = evaluationStateEnum.getStateInfo();
        this.evaluationList = evaluationList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public List<Evaluation> getEvaluationList() {
        return evaluationList;
    }

    public void setEvaluationList(List<Evaluation> evaluationList) {
        this.evaluationList = evaluationList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }
}
