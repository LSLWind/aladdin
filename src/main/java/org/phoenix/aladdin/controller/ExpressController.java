package org.phoenix.aladdin.controller;

import com.alibaba.fastjson.JSONObject;
import org.phoenix.aladdin.constant.Constant;
import org.phoenix.aladdin.constant.Result;
import org.phoenix.aladdin.model.view.ExpressLocationVO;
import org.phoenix.aladdin.service.ExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/web/express")
public class ExpressController {

    private ExpressService expressService;
    @Autowired
    public void setExpressService(ExpressService expressService){
        this.expressService=expressService;
    }


    @RequestMapping("/getExpressLocation/{express_id}")
    @ResponseBody
    public Result<Object> getExpressLocationByExpressId(@PathVariable("express_id")long expressId){
        Result<Object> result=new Result<>();
        result.setCode(Constant.OK);
        //使用JSONObject填充字段
        JSONObject object = new JSONObject();
        object.put("expressLocationVO",expressService.getExpressLocationById(expressId));
        result.setData(object);
        return result;
    }
}
