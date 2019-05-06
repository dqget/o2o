package com.lovesickness.o2o.service.impl;

import com.lovesickness.o2o.dao.EvaluationDao;
import com.lovesickness.o2o.dto.EvaluationExecution;
import com.lovesickness.o2o.entity.Evaluation;
import com.lovesickness.o2o.enums.EvaluationStateEnum;
import com.lovesickness.o2o.exception.EvaluationOperationException;
import com.lovesickness.o2o.service.EvaluationService;
import com.lovesickness.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class EvaluationServiceImpl implements EvaluationService {
    @Autowired
    private EvaluationDao evaluationDao;

    @Override
    @Transactional(rollbackFor = EvaluationOperationException.class)
    public EvaluationExecution addEvaluation(Evaluation evaluation) throws EvaluationOperationException {
        evaluation.setCreateTime(new Date());
        int effectedNum = evaluationDao.insertEvaluation(evaluation);

        if (effectedNum != 1) {
            throw new EvaluationOperationException("评论插入错误");
        }
        return new EvaluationExecution(EvaluationStateEnum.SUCCESS);
    }

    @Override
    public EvaluationExecution getEvaluation(Evaluation evaluationCondition, int beginIndex, int pageSize) {
        int rowIndex = PageCalculator.calculateRowIndex(beginIndex, pageSize);
        EvaluationExecution ee = new EvaluationExecution(EvaluationStateEnum.SUCCESS);
        ee.setEvaluationList(evaluationDao.queryEvaluation(evaluationCondition, rowIndex, pageSize));
        ee.setCount(evaluationDao.queryEvaluationCount(evaluationCondition));
        return ee;
    }
}
