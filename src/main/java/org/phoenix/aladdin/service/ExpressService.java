package org.phoenix.aladdin.service;

import org.phoenix.aladdin.constant.ExpressInfo;
import org.phoenix.aladdin.dao.EmployeeDao;
import org.phoenix.aladdin.dao.ExpressDao;
import org.phoenix.aladdin.dao.PackageContentDao;
import org.phoenix.aladdin.model.entity.*;
import org.phoenix.aladdin.model.view.ExpressLocationVO;
import org.phoenix.aladdin.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 提供快件查询、创建、更新服务
 */
@Component
public class ExpressService {

    private EmployeeDao employeeDao;
    @Autowired
    public void setEmployeeDao(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

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
    public List<ExpressLocationVO> getExpressLocationById(long id){
        List<ExpressLocationVO> res=new ArrayList<>();
        Express express=expressDao.findById(id);

        //检查快件是否存在
        if(express==null){
            res.add(new ExpressLocationVO("","",ExpressInfo.EXPRESS_NOT_FOUND,""));
            return res;
        }

        //检查快件状态
        if(express.getStatus().equals(ExpressInfo.EXPRESS_NOT_START)){
            res.add(new ExpressLocationVO(express.getBeginTime(),"",ExpressInfo.EXPRESS_NOT_START,""));
            return res;
        }

        //查找快件运输历史
        if(express.getPackageHistoryList()==null){
            res.add(new ExpressLocationVO(express.getBeginTime(),"",ExpressInfo.EXPRESS_NOT_START,""));
        }else {
            List<PackageHistory> packageHistoryList=express.getPackageHistoryList();
            for(PackageHistory p:packageHistoryList){
                String liablePersonName=employeeDao.findById(express.getId());
                res.add(new ExpressLocationVO(p.getTime(),p.getTransportNodeId(),p.getStatus(),liablePersonName));
            }
        }

        return res;
    }

    /**
     * 插入一条快递数据
     * @param express
     * @return
     */
    @Transactional
    public Express insertOneExpress(Express express){
        return expressDao.save(express);
    }

    /**
     * 获取用户发送过的快件列表
     */
    public List<Express> getSendOrdersByUserId(long userId){
        return expressDao.findBySenderId(userId);
    }

    /**
     * 获取用户接收的快件列表
     */
    public List<Express> getReceiveOrdersByUserId(long userId){
        return expressDao.findByReceiverId(userId);
    }

    /**
     * 根据id删除一个快件记录
     */
    @Transactional
    public boolean deleteExpressById(long id){
        try {
            expressDao.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
