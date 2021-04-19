package com.biu.wifi.campus.controller.admin;

import com.biu.wifi.campus.dao.model.DictInfo;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.DictInfoService;
import com.biu.wifi.core.util.ServletUtilsEx;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 张彬.
 * @date 2021/4/6 9:49.
 */
@Controller
public class BackedDictInfoController {

    @Autowired
    private DictInfoService dictInfoService;

    /**
     * 新增字典
     *
     * @param req
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/6 9:56.
     */
    @RequestMapping("backed_addDictInfo")
    public void add(DictInfo req, HttpServletResponse response) {
        Assert.notNull(req.getPid(), "上级id不能为空，没有上级默认为0");
        Assert.notNull(req.getName(), "字典名称不能为空");
        Assert.notNull(req.getValue(), "字典值不能为空");
        if (req.getPid() != 0 && StringUtils.isEmpty(req.getCode())) {
            DictInfo parent = dictInfoService.selectById(req.getPid());
            Assert.notNull(parent, "上级字典不存在");
            try {
                HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
                hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
                req.setCode(parent.getCode() + "_" + PinyinHelper.toHanyuPinyinString(req.getName(), hanyuPinyinOutputFormat, ""));
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            }
        }
        Assert.notNull(req.getCode(), "字典code不能为空");

        dictInfoService.add(req);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
    }

    /**
     * 修改字典
     *
     * @param req
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/6 9:56.
     */
    @RequestMapping("backed_updateDictInfo")
    public void update(DictInfo req, HttpServletResponse response) {
        Assert.notNull(req.getId(), "请选择要修改的字典");
        Assert.notNull(req.getCode(), "字典code不能为空");
        Assert.notNull(req.getName(), "字典名称不能为空");
        Assert.notNull(req.getValue(), "字典值不能为空");
        Assert.notNull(req.getPid(), "上级id不能为空，没有上级默认为0");

        dictInfoService.update(req);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
    }

    /**
     * 删除字典
     *
     * @param ids
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/6 9:56.
     */
    @RequestMapping("backed_deleteDictInfo")
    public void delete(String ids, HttpServletResponse response) {
        Assert.notNull(ids, "请选择要删除的字典");
        List<Integer> deleteIds = new ArrayList<>();
        for (String id : ids.split(",")) {
            deleteIds.add(Integer.valueOf(id));
        }
        Assert.notEmpty(deleteIds, "请选择要删除的字典");
        dictInfoService.delete(deleteIds);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
    }
}
