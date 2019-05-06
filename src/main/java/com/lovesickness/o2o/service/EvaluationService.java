package com.lovesickness.o2o.service;

import com.lovesickness.o2o.dto.EvaluationExecution;
import com.lovesickness.o2o.entity.Evaluation;
import com.lovesickness.o2o.exception.EvaluationOperationException;

public interface EvaluationService {

    EvaluationExecution addEvaluation(Evaluation evaluation) throws EvaluationOperationException;

    EvaluationExecution getEvaluation(Evaluation evaluationCondition, int rowIndex, int pageSize);

}
