package com.tsinghua.sec.service;

import com.tsinghua.sec.cache.SolderCache;
import com.tsinghua.sec.domain.Solder;
import com.tsinghua.sec.util.PropertiesUtil;

import java.util.List;

/**
 * Created by ji on 16-5-27.
 */
public class InitService {
    public void init() {
        PropertiesUtil.getInstance();

        ShareKeys shareKeys = ShareKeys.getInstance();

        List<Solder> solderList = SolderCache.getInstance().getSolders();

        int solderNum = solderList.size();

        List<PointNum> pointNums = shareKeys.Producekeys(ShareKeys.KEY_NUM, solderNum, ShareKeys.degree);
        for (int i = 0; i < solderList.size(); i++) {
            Solder solder = solderList.get(i);
            PointNum point = pointNums.get(i);
            solder.setPassword(point.X + "," + point.Y);
            SolderCache.getInstance().addSolder(solder.getName(), solder);
        }
    }
}
