package com.yupi.chenoj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.chenoj.annotation.AuthCheck;
import com.yupi.chenoj.common.BaseResponse;
import com.yupi.chenoj.common.ErrorCode;
import com.yupi.chenoj.common.ResultUtils;
import com.yupi.chenoj.constant.UserConstant;
import com.yupi.chenoj.exception.BusinessException;
import com.yupi.chenoj.model.dto.question.QuestionQueryRequest;
import com.yupi.chenoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yupi.chenoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yupi.chenoj.model.entity.Question;
import com.yupi.chenoj.model.entity.QuestionSubmit;
import com.yupi.chenoj.model.entity.User;
import com.yupi.chenoj.model.vo.QuestionSubmitVO;
import com.yupi.chenoj.service.QuestionSubmitService;
import com.yupi.chenoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 题目提交
     * @param questionSubmitAddRequest
     * @param request
     * @return  提交记录的id
     */
    @PostMapping("/")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
            HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能题目提交
        final User loginUser = userService.getLoginUser(request);
        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(questionSubmitId);
    }

    /**
     * 分页获取题目列表(除了管理员外，普通用户只能看到非答案，提交答案等公开信息)
     *
     * @param questionSubmitQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
                                                                         HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        //从数据库中查询原始的题目提交分页信息
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        final User loginUser = userService.getLoginUser(request);
        //返回脱敏信息
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage,loginUser));
    }

}
