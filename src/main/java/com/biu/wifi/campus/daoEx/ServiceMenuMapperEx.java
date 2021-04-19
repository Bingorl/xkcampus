package com.biu.wifi.campus.daoEx;

import com.biu.wifi.campus.dao.model.ServiceMenu;
import com.biu.wifi.campus.dao.model.ServiceMenuCategory;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceMenuMapperEx extends CoreDao {

    List<ServiceMenu> findListByKeyword(@Param("keyword") String keyword, @Param("categoryId") Integer categoryId);

    List<ServiceMenuCategory> findCategoryListByKeyword(@Param("keyword") String keyword);
}