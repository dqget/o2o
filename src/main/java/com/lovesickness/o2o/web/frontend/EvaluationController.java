package com.lovesickness.o2o.web.frontend;

import com.lovesickness.o2o.dto.EvaluationExecution;
import com.lovesickness.o2o.entity.Evaluation;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.Product;
import com.lovesickness.o2o.enums.EvaluationStateEnum;
import com.lovesickness.o2o.service.EvaluationService;
import com.lovesickness.o2o.util.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/evaluation")
@Api(tags = "EvaluationController|评论操作控制器")
public class EvaluationController {
    @Autowired
    private EvaluationService evaluationService;

    @GetMapping("/getevaluation")
    @ApiOperation(value = "查询评论列表", notes = "根据产品ID查询评论列表")
    public ResultBean<EvaluationExecution> getEvaluation(
            @RequestParam(value = "productId") long productId,
            @RequestParam(value = "pageIndex") int pageIndex,
            @RequestParam(value = "pageSize") int pageSize) {
        Product product = new Product();
        product.setProductId(productId);
        Evaluation evaluation = new Evaluation();
        evaluation.setProduct(product);

        return new ResultBean<>(evaluationService.getEvaluation(evaluation, pageIndex, pageSize));
    }

    @PostMapping("/addevaluationByUser")
    @ApiOperation(value = "添加评论", notes = "根据Session中的用户添加评论")
    public ResultBean<EvaluationExecution> addEvaluationByUser(
            @RequestBody Evaluation evaluation,
            HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user == null || user.getUserId() == null) {
            return new ResultBean<>(false, 0, "请重新登录");
        }
        evaluation.setFromName(user.getName());
        evaluation.setFromUid(user.getUserId());
        EvaluationExecution ee = evaluationService.addEvaluation(evaluation);
        if (ee.getState() == EvaluationStateEnum.SUCCESS.getState()) {
            return new ResultBean<>(ee);
        } else {
            return new ResultBean<>(false, ResultBean.FAIL, ee.getStateInfo());
        }
    }
}
