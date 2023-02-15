package d1.project.questionnaire.business;

import com.alibaba.fastjson.JSONObject;
import d1.project.questionnaire.entity.examination.*;
import d1.project.questionnaire.model.exam.exam.AppExamQuestionReturnVm;
import d1.project.questionnaire.model.exam.exam.AppExamReturnVm;
import d1.project.questionnaire.model.exam.exam.ExamQuestionReturnVm;
import d1.project.questionnaire.model.exam.exam.ExamReturnVm;
import d1.project.questionnaire.model.exam.examgroup.ExamGroupInsertVm;
import d1.project.questionnaire.model.exam.examgroup.ExamGroupUpdateVm;
import d1.project.questionnaire.model.exam.examination.ExaminationInsertVm;
import d1.project.questionnaire.model.exam.examination.ExaminationUpdateVm;
import d1.project.questionnaire.model.exam.examinationquestion.ExaminationQuestionInsertVm;
import d1.project.questionnaire.model.exam.question.*;
import d1.project.questionnaire.service.examination.*;
import d1.project.questionnaire.util.BaseUtils;
import d1.project.questionnaire.util.Constants;
import d1.project.questionnaire.view.entity.ExamGroupNum;
import d1.project.questionnaire.view.entity.ExamList;
import d1.project.questionnaire.view.entity.QuestionBank;
import d1.project.questionnaire.view.service.ExamGroupNumService;
import d1.project.questionnaire.view.service.ExamListService;
import d1.project.questionnaire.view.service.QuestionBankService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * liangxin_new 考试相关业务处理
 *
 * @author kikki
 * @date 2020-10-12 15:37
 */
@Service
public class ExaminationBusiness {

    private final ExaminationService examinationService;
    private final ExaminationGroupService examinationGroupService;
    private final QuestionService questionService;
    private final QuestionBankService questionBankService;
    private final OptionService optionService;
    private final ExamGroupNumService examGroupNumService;
    private final ExamListService examListService;
    private final ExaminationQuestionService examinationQuestionService;
    private final ExaminationUserService examinationUserService;

    public ExaminationBusiness(ExaminationService examinationService,
                               ExaminationGroupService examinationGroupService,
                               QuestionService questionService,
                               QuestionBankService questionBankService,
                               OptionService optionService,
                               ExamGroupNumService examGroupNumService,
                               ExamListService examListService,
                               ExaminationQuestionService examinationQuestionService,
                               ExaminationUserService examinationUserService) {
        this.examinationService = examinationService;
        this.examinationGroupService = examinationGroupService;
        this.questionService = questionService;
        this.questionBankService = questionBankService;
        this.optionService = optionService;
        this.examGroupNumService = examGroupNumService;
        this.examListService = examListService;
        this.examinationQuestionService = examinationQuestionService;
        this.examinationUserService = examinationUserService;
    }


    /**
     * 题库分组详情
     *
     * @param id 数据ID
     * @return 查询结果
     */
    public ExaminationGroup findExamGroupById(String id) {
        return examinationGroupService.findById(id);
    }

    /**
     * 添加题库分组
     *
     * @param insertVm 添加信息
     * @param request  请求信息
     * @throws Exception 向上抛出添加异常
     */
    public void insertExamGroup(ExamGroupInsertVm insertVm, HttpServletRequest request) throws Exception {
        String name = insertVm.getName();
        if (examinationGroupService.existsAllByName(name)) {
            throw new Exception(Constants.EXAM_GROUP_REPEAT);
        }
        examinationGroupService.insertExamGroup(name, request);
    }

    /**
     * 添加题库分组
     *
     * @param updateVm 添加信息
     * @throws Exception 向上抛出添加异常
     */
    public void updateExamGroup(ExamGroupUpdateVm updateVm) throws Exception {
        String id = updateVm.getId();
        String name = updateVm.getName();
        if (examinationGroupService.existsAllByNameAndIdNot(name, id)) {
            throw new Exception(Constants.EXAM_GROUP_REPEAT);
        }
        examinationGroupService.updateExamGroup(name, id);
    }

    /**
     * 删除题库分组
     *
     * @param id 题库ID
     * @throws Exception 存在考题无法删除
     */
    public void deleteExamGroupById(String id) throws Exception {
        String defaultId = "1";
        if (defaultId.equals(id)) {
            throw new Exception(Constants.DEFAULT_CAN_NOT_DELETE);
        }
        if (questionService.existsAllByExaminationGroupId(id)) {
            throw new Exception(Constants.EXAM_GROUP_IS_NOT_NULL);
        }
        examinationGroupService.delete(id);
    }

    /**
     * 题库分组列表
     *
     * @param params 查询信息
     * @return 查询结果
     * @throws Exception 向上抛出查询异常
     */
    public List<ExamGroupNum> findAllExamGroup(JSONObject params) throws Exception {
        return examGroupNumService.findAllByParams(params);
    }

    /**
     * 查看题库中的试题列表
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 查询异常向上抛出
     */
    public Page<QuestionBank> findQuestionList(JSONObject params) throws Exception {
        return questionBankService.findAll(params);
    }

    /**
     * 获取分组列表
     *
     * @return 查询结果
     */
    public List<ExaminationGroup> getExaminationGroup() {
        return examinationGroupService.findAll();
    }

    /**
     * 试题详情查询
     *
     * @param id 试题ID
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public QuestionFindReturnVm findQuestionById(String id) throws Exception {
        Question question = questionService.findById(id);
        if (question == null) {
            throw new Exception(Constants.DATA_IS_NULL_OR_FAIL);
        }
        List<Option> list = optionService.findAllByQuestionId(id);
        QuestionFindReturnVm returnVm = new QuestionFindReturnVm();
        returnVm.setQuestion(question);
        if (list.size() > 0) {
            returnVm.setOptionList(list);
        }
        return returnVm;
    }

    /**
     * 批量查询试题详情
     *
     * @param ids 试题ID集合
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public List<QuestionFindReturnVm> findQuestionInfo(List<String> ids) throws Exception {
        List<Question> list = questionService.findQuestionListByIds(ids);
        List<QuestionFindReturnVm> returnList = new ArrayList<>();
        if (list.size() <= 0) {
            throw new Exception(Constants.DATA_IS_NULL_OR_FAIL);
        }
        for (Question question : list) {
            String questionId = question.getId();
            List<Option> optionList = optionService.findAllByQuestionId(questionId);
            QuestionFindReturnVm returnVm = new QuestionFindReturnVm();
            returnVm.setQuestion(question);
            if (list.size() > 0) {
                returnVm.setOptionList(optionList);
            }
            returnList.add(returnVm);
        }
        return returnList;
    }

    /**
     * 试题添加
     *
     * @param insertVm 添加信息
     * @param request  请求
     * @throws Exception 向上抛出添加异常
     */
    public void insertQuestion(QuestionInsertVm insertVm, HttpServletRequest request) throws Exception {
        String userId = examinationService.getCurrentUser(request).getString("id");
        String id = BaseUtils.getUid();
        Question question = new Question();
        question.setType(insertVm.getType());
        question.setTopic(insertVm.getTopic());
        question.setProblemAnalysis(insertVm.getProblemAnalysis());
        question.setExaminationGroupId(insertVm.getExaminationGroupId());
        question.setCreateById(userId);
        question.setCreateTime(new Date());
        question.setId(id);
        questionService.insert(question);
        List<OptionInsertVm> list = insertVm.getOption();
        for (OptionInsertVm optionInsertVm : list) {
            Option option = BaseUtils.copyPropertiesInsert(optionInsertVm, userId, Option.class);
            option.setQuestionId(id);
            optionService.insert(option);
        }
    }

    /**
     * 修改试题
     *
     * @param updateVm 修改信息
     * @param request  请求信息
     * @throws Exception 向上抛出异常
     */
    public void updateQuestion(QuestionUpdateVm updateVm, HttpServletRequest request) throws Exception {
        String userId = examinationService.getCurrentUser(request).getString("id");
        String id = updateVm.getId();
        Question question = new Question();
        question.setType(updateVm.getType());
        question.setTopic(updateVm.getTopic());
        question.setProblemAnalysis(updateVm.getProblemAnalysis());
        question.setExaminationGroupId(updateVm.getExaminationGroupId());
        question.setUpdateById(userId);
        question.setUpdateTime(new Date());
        question.setId(id);
        questionService.update(question);
        List<OptionUpdateVm> list = updateVm.getOption();
        for (OptionUpdateVm optionUpdateVm : list) {
            Option option = BaseUtils.copyPropertiesUpdate(optionUpdateVm, userId, Option.class);
            option.setQuestionId(id);
            optionService.update(option);
        }
    }

    /**
     * 删除试题
     *
     * @param ids 试题ID集合
     */
    public void deleteQuestions(List<String> ids) throws Exception {
        if (ids == null || ids.size() <= 0) {
            throw new Exception(Constants.QUESTION_ID_IS_NULL);
        }
        if (examinationQuestionService.existsAllByQuestionIdIn(ids)) {
            throw new Exception(Constants.EXAM_QUESTION_IS_NOT_NULL);
        }
        optionService.deleteByQuestionIdIn(ids);
        questionService.deleteAllByIdIn(ids);
    }

    /**
     * 移动分组
     *
     * @param moveVm 移动信息
     */
    public void moveQuestions(MoveQuestion moveVm) {
        String groupId = moveVm.getExaminationGroupId();
        List<String> ids = moveVm.getIds();
        questionService.updateGroupIdByIds(groupId, ids);
    }

    /**
     * 查询考试列表
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出查询异常
     */
    public Page<ExamList> findExam(JSONObject params) throws Exception {
        return examListService.findAllByParams(params);
    }

    /**
     * 考试详情
     *
     * @param id 考试ID
     * @return 考试详情
     * @throws Exception 向上抛出异常
     */
    public ExamReturnVm findExamById(String id) throws Exception {
        ExamReturnVm examReturnVm = new ExamReturnVm();
        /*查找考试*/
        Examination exam = examinationService.findByInfoId(id);
        examReturnVm.setId(id);
        examReturnVm.setName(exam.getName());
        /*查找考试选项*/
        List<ExaminationQuestion> questionList = examinationQuestionService.findByExamId(id);
        if (questionList == null || questionList.size() <= 0) {
            return examReturnVm;
        }
        List<String> collect = questionList.stream().map(ExaminationQuestion::getQuestionId).collect(Collectors.toList());
        List<Question> list = questionService.getList(questionDao -> questionDao.findAllById(collect));
        List<Option> list1 = optionService.getList(optionDao -> optionDao.findAllByQuestionIdInOrderByNumberAsc(collect));
        long singleNum = 0L;
        long singleScore = 0L;
        long multipleNum = 0L;
        long multipleScore = 0L;

        List<ExamQuestionReturnVm> examQuestionList = new ArrayList<>(questionList.size());
        for (ExaminationQuestion examinationQuestion : questionList) {
            String questionId = examinationQuestion.getQuestionId();
            Question question = list.stream().filter(question1 -> question1.getId().equals(questionId)).findFirst().orElseThrow(() -> new Exception("试题不存在"));

            List<Option> optionsList = list1.stream().filter(question1 -> question1.getQuestionId().equals(questionId)).collect(Collectors.toList());

            String type = examinationQuestion.getType();
            Integer questionScore = examinationQuestion.getScore();
            if (questionScore == null) {
                questionScore = 0;
            }

            ExamQuestionReturnVm examQuestion = new ExamQuestionReturnVm();
            examQuestion.setId(question.getId());
            examQuestion.setNumber(examinationQuestion.getNumber());
            examQuestion.setType(type);
            examQuestion.setTopic(question.getTopic());
            examQuestion.setScore(questionScore);
            examQuestion.setOption(optionsList);

            switch (type) {
                case "0":
                    singleNum++;
                    singleScore += questionScore;
                    break;
                case "1":
                    multipleNum++;
                    multipleScore += questionScore;
                    break;
                default:
                    continue;
            }
            examQuestionList.add(examQuestion);
        }
        examReturnVm.setQuestion(examQuestionList);

        long totalScore = singleScore + multipleScore;
        examReturnVm.setSingleNum(singleNum);
        examReturnVm.setSingleScore(singleScore);
        examReturnVm.setMultipleNum(multipleNum);
        examReturnVm.setMultipleScore(multipleScore);
        examReturnVm.setTotalScore(totalScore);

        return examReturnVm;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertExamination(ExaminationInsertVm insertVm, HttpServletRequest request) throws Exception {
        String userId = examinationService.getCurrentUser(request).getString("id");
        Examination audio = BaseUtils.copyPropertiesInsert(insertVm, userId, Examination.class);
        String examId = BaseUtils.getUid();
        audio.setId(examId);
        String name = audio.getName();
        if (examinationService.existsAllByName(name)) {
            throw new Exception(Constants.EXAM_NAME_REPEAT);
        }
        examinationService.insert(audio);
        List<ExaminationQuestionInsertVm> questionList = insertVm.getQuestion();
        for (ExaminationQuestionInsertVm vm : questionList) {
            ExaminationQuestion question = BaseUtils.copyPropertiesInsert(vm, userId, ExaminationQuestion.class);
            question.setExaminationId(examId);
            examinationQuestionService.insert(question);
        }
    }

    /**
     * 编辑考试
     *
     * @param updateVm 编辑信息
     * @param request  请求信息
     * @throws Exception 向上抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateExamination(ExaminationUpdateVm updateVm, HttpServletRequest request) throws Exception {
        String userId = examinationService.getCurrentUser(request).getString("id");
        Examination exam = BaseUtils.copyPropertiesUpdate(updateVm, userId, Examination.class);
        String examId = exam.getId();
        String name = exam.getName();
        if (!StringUtils.isEmpty(name) && examinationService.existsAllByNameAndIdNot(name, examId)) {
            throw new Exception(Constants.EXAM_NAME_REPEAT);
        }
        examinationService.update(exam);
        examinationQuestionService.deleteAllByExamId(examId);
        List<ExaminationQuestionInsertVm> questionList = updateVm.getQuestion();
        for (ExaminationQuestionInsertVm insertVm : questionList) {
            ExaminationQuestion question = BaseUtils.copyPropertiesInsert(insertVm, userId, ExaminationQuestion.class);
            question.setExaminationId(examId);
            examinationQuestionService.insert(question);
        }
    }

    /**
     * 删除考试
     *
     * @param id 数据ID
     * @throws Exception 向上抛出异常
     */
    public void deleteExamination(String id) throws Exception {
//        if (learnPlanService.existsAllByContentId(id)) {
//            throw new Exception(Constants.EXAM_GROUP_IS_NOT_NULL);
//        }
        examinationQuestionService.deleteAllByExamId(id);
        examinationService.delete(id);
    }

    /**
     * 添加试题时查看试题列表
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<Question> findAllExamQuestion(JSONObject params) throws Exception {
        return questionService.findByParams(params);
    }

    /**
     * APP考试详情
     *
     * @param id 学习计划ID
     * @return 考试详情
     * @throws Exception 向上抛出异常
     */
    public AppExamReturnVm appFindExam(String id) throws Exception {
//        LearnPlan learnPlan = learnPlanService.findById(learnPlanId);
//        if (learnPlan == null) {
//            throw new Exception(Constants.DATA_IS_NULL_OR_FAIL);
//        }
//        String id = learnPlan.getContentId();
        AppExamReturnVm appExamReturnVm = new AppExamReturnVm();
        Examination exam = examinationService.findByInfoId(id);
        appExamReturnVm.setId(id);
        appExamReturnVm.setName(exam.getName());
        List<ExaminationQuestion> questionList = examinationQuestionService.findByExamId(id);
        if (questionList == null || questionList.size() <= 0) {
            return appExamReturnVm;
        }
        long questionNum = 0L;
        long totalScore = 0L;
        List<String> collect = questionList.stream().map(ExaminationQuestion::getQuestionId).collect(Collectors.toList());
        List<Question> list = questionService.getList(questionDao -> questionDao.findAllById(collect));
        List<Option> list1 = optionService.getList(optionDao -> optionDao.findAllByQuestionIdInOrderByNumberAsc(collect));

        List<AppExamQuestionReturnVm> appExamQuestionList = new ArrayList<>(questionList.size());
        for (ExaminationQuestion examinationQuestion : questionList) {
            String questionId = examinationQuestion.getQuestionId();
            Question question = list.stream().filter(question1 -> question1.getId().equals(questionId)).findFirst().orElseThrow(() -> new Exception("试题不存在"));

            List<Option> optionsList = list1.stream().filter(question1 -> question1.getQuestionId().equals(questionId)).collect(Collectors.toList());

            Integer questionScore = examinationQuestion.getScore();
            if (questionScore == null) {
                questionScore = 0;
            }
            AppExamQuestionReturnVm appExamQuestion = new AppExamQuestionReturnVm();
            appExamQuestion.setProblemAnalysis(question.getProblemAnalysis());
            appExamQuestion.setId(examinationQuestion.getId());
            appExamQuestion.setNumber(examinationQuestion.getNumber());
            appExamQuestion.setType(examinationQuestion.getType());
            appExamQuestion.setTopic(question.getTopic());
            appExamQuestion.setScore(questionScore);
            appExamQuestion.setOption(optionsList);
            questionNum++;
            totalScore += questionScore;
            appExamQuestionList.add(appExamQuestion);
        }
        appExamReturnVm.setQuestion(appExamQuestionList);
        appExamReturnVm.setQuestionNum(questionNum);
        appExamReturnVm.setTotalScore(totalScore);
        return appExamReturnVm;
    }


    public Map<String, Object> checkUserExamination(String learnPlanId, String campPeriodId, HttpServletRequest request) {
//        JSONObject currentUser = userSignInService.getCurrentUser(request);
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("score", 0);
        objectMap.put("createTime", null);
        objectMap.put("isExamination", "0");
        examinationUserService.getOptional(examinationUserDao -> examinationUserDao.findByLearnPlanIdAndCampPeriodIdAndUserId(learnPlanId, campPeriodId, "currentUser.getString(id)")).ifPresent(examinationUser -> {
            objectMap.put("score", examinationUser.getUserAllfraction());
            objectMap.put("createTime", examinationUser.getCreateTime());
            objectMap.put("isExamination", "1");
        });
        return objectMap;

    }
}
