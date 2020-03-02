package org.phoenix.aladdin.service;

import org.phoenix.aladdin.constant.ExpressInfo;
import org.phoenix.aladdin.dao.ExpressDao;
import org.phoenix.aladdin.dao.PackageContentDao;
import org.phoenix.aladdin.model.entity.Express;
import org.phoenix.aladdin.model.entity.PackageContent;
import org.phoenix.aladdin.model.entity.PackageHistory;
import org.phoenix.aladdin.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.DataFormatException;

/**
 * 提供快件查询、创建、更新服务
 */
@Component
public class ExpressService {

    private ExpressDao expressDao;
    @Autowired
    public void setExpressDao(ExpressDao expressDao) {
        this.expressDao = expressDao;
    }

    private PackageContentDao packageContentDao;
    @Autowired
    public void setPackageContentDao(PackageContentDao packageContentDao){
        this.packageContentDao=packageContentDao;
    }

    /**
     * 给定快件id，返回快件的历史位置信息记录
     * @param id 快件id
     * @return 快件的历史位置信息记录
     */
    public List<String> getExpressLocationById(long id){
        List<String> res=new ArrayList<>();
        Express express=expressDao.findById(id);

        //检查快件是否存在
        if(express==null){
            res.add(ExpressInfo.EXPRESS_NOT_FOUND);
            return res;
        }

        //检查快件状态
        if(express.getStatus().equals(ExpressInfo.EXPRESS_NOT_START)){
            res.add(ExpressInfo.EXPRESS_NOT_START);
            return res;
        }

        //查找快件运输历史
        if(express.getPackageHistoryList()==null){
            res.add(ExpressInfo.EXPRESS_NOT_START);
        }else {
            List<PackageHistory> packageHistoryList=express.getPackageHistoryList();
            for(PackageHistory p:packageHistoryList){
                res.add(p.getTime()+"到达"+p.getTransportNodeId());
            }
        }

        return res;
    }
}
