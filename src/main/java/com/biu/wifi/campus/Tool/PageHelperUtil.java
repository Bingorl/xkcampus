package com.biu.wifi.campus.Tool;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务层分页工具类
 *
 * @author zhangbin.
 * @date 2018/11/5.
 */
public class PageHelperUtil<T> {

    public static <T> List getPageList(int page, int pageSize, List<T> list) {
        if (page < 0) {
            page = 1;
        }

        //总记录数
        int totalCount = list.size();
        int index = (page - 1) * pageSize;
        int toIndex = index + pageSize;

        List<?> subList;
        if (index <= totalCount) {
            if (toIndex <= totalCount) {
                subList = list.subList(index, toIndex);
            } else {
                subList = list.subList(index, totalCount);
            }
        } else {
            subList = new ArrayList<>();
        }

        return subList;
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 103; i++) {
            list.add(i);
        }

        List<Integer> subList = getPageList(1, 5, list);
        System.out.println(new ArrayList<>(subList));
    }
}
