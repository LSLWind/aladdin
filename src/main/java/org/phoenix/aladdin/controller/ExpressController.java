package org.phoenix.aladdin.controller;

import com.alibaba.fastjson.JSONObject;
import org.phoenix.aladdin.response.CommonReturnType;
import org.phoenix.aladdin.service.ExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/web/express")
public class ExpressController extends BaseController{

    private ExpressService expressService;
    @Autowired
    public void setExpressService(ExpressService expressService){
        this.expressService=expressService;
    }


    /**
     * 根据快件id获取快件历史信息位置
     */
    @RequestMapping("/getExpressLocation/{express_id}")
    @ResponseBody
    public CommonReturnType getExpressLocationByExpressId(@PathVariable("express_id")long expressId){
        //使用JSONObject填充字段
        JSONObject res = new JSONObject();
        res.put("expressLocationVO",expressService.getExpressLocationById(expressId));
        return CommonReturnType.create(res);
    }
}
