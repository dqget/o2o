package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.Evaluation;
import com.lovesickness.o2o.entity.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EvaluationDaoTest {
    @Autowired
    private EvaluationDao evaluationDao;

    @Test
    public void testAInsertEvaluation() {
        Evaluation evaluation = new Evaluation();
        Product product = new Product();
        product.setProductId(4L);
        evaluation.setProduct(product);
        evaluation.setContent("你骗人");
        evaluation.setFromName("小慧慧");
        evaluation.setFromUid(6L);
        evaluation.setToUid(1L);
        evaluation.setToName("xiaoy");

        int effectedNum = evaluationDao.insertEvaluation(evaluation);
        assertEquals(effectedNum, 1);
    }

    @Test
    public void testBQueryEvaluation() {
        Evaluation evaluation = new Evaluation();
        Product product = new Product();
        product.setProductId(4L);
        evaluation.setProduct(product);
        List<Evaluation> evaluations =
                evaluationDao.queryEvaluation(evaluation, 0, 99);
        int effectedNum = evaluationDao.queryEvaluationCount(evaluation);
        System.out.println(evaluations);
        System.out.println(effectedNum);
    }
}
