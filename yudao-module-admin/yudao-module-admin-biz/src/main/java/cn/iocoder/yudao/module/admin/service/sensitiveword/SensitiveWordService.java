package cn.iocoder.yudao.module.admin.service.sensitiveword;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.admin.controller.admin.sensitiveword.vo.SensitiveWordPageReqVO;
import cn.iocoder.yudao.module.admin.controller.admin.sensitiveword.vo.SensitiveWordSaveVO;
import cn.iocoder.yudao.module.admin.dal.dataobject.sensitiveword.SensitiveWordDO;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * 敏感词 Service 接口
 *
 * @author 永不言败
 */
public interface SensitiveWordService {

    /**
     * 创建敏感词
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSensitiveWord(@Valid SensitiveWordSaveVO createReqVO);

    /**
     * 更新敏感词
     *
     * @param updateReqVO 更新信息
     */
    void updateSensitiveWord(@Valid SensitiveWordSaveVO updateReqVO);

    /**
     * 删除敏感词
     *
     * @param id 编号
     */
    void deleteSensitiveWord(Long id);

    /**
     * 获得敏感词
     *
     * @param id 编号
     * @return 敏感词
     */
    SensitiveWordDO getSensitiveWord(Long id);

    /**
     * 获得敏感词列表
     *
     * @return 敏感词列表
     */
    List<SensitiveWordDO> getSensitiveWordList();

    /**
     * 获得敏感词分页
     *
     * @param pageReqVO 分页查询
     * @return 敏感词分页
     */
    PageResult<SensitiveWordDO> getSensitiveWordPage(SensitiveWordPageReqVO pageReqVO);

    /**
     * 获得所有敏感词的标签数组
     *
     * @return 标签数组
     */
    Set<String> getSensitiveWordTagSet();

    /**
     * 获得文本所包含的不合法的敏感词数组
     *
     * @param text 文本
     * @param tags 标签数组
     * @return 不合法的敏感词数组
     */
    List<String> validateText(String text, List<String> tags);

    /**
     * 判断文本是否包含敏感词
     *
     * @param text 文本
     * @param tags 标签数组
     * @return 是否包含敏感词
     */
    boolean isTextValid(String text, List<String> tags);

}
