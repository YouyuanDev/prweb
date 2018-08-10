package com.prweb.service;

import com.prweb.entity.Function;

public interface FunctionService {

    //分页搜索
    public String getFunctionByLike(String function_no,String function_name,int start,int rows);

    //根据编号、名称查找Function
    public String getFunctionByNoName(String function_no,String function_name);

    //保存function
    public String saveFunction(Function function);

    //删除Function信息
    public String delFunction(String hlparam);

}
