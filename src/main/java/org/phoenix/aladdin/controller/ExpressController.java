package org.phoenix.aladdin.controller;

import org.phoenix.aladdin.constant.Result;
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
    public Result<List<String>> getExpressLocationByExpressId(@PathVariable("express_id")long expressId){
        Result<List<String>> result=new Result<>();
        result.setCode(1);
        result.setData(expressService.getExpressLocationById(expressId));
        return result;
    }
}
