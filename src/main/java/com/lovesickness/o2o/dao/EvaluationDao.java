package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.Evaluation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EvaluationDao {

    int insertEvaluation(Evaluation evaluation);

    List<Evaluation> queryEvaluation(
            @Param("evaluationCondition") Evaluation evaluationCondition,
            @Param("rowIndex") int rowIndex,
            @Param("pageSize") int pageSize);

    int queryEvaluationCount(@Param("evaluationCondition") Evaluation evaluationCondition);

}
