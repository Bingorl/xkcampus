package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.AssetMapper;
import com.biu.wifi.campus.dao.model.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author MCX
 * @Version 1.0
 **/
@Service
public class AssetService {
    @Autowired
    private AssetMapper assetMapper;

    public List<Asset> selectAll(){
        List<Asset> assets = assetMapper.selectByExample(null);
        return assets;
    }
}
