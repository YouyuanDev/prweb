package com.prweb.dao;

import com.prweb.entity.Function;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface FunctionDao {
    public List<HashMap<String,Object>>getAllByNoName(@Param("function_no")String function_no, @Param("function_name")String function_name);

    //模糊搜索带分页
    public List<HashMap<String,Object>> getAllByLike(@Param("function_no")String function_no, @Param("function_name")String function_name, @Param("skip")int skip, @Param("take")int take);

    //模糊搜索总数
    public int getCountAllByLike(@Param("function_no")String function_no, @Param("function_name")String function_name);

    //修改function
    public int updateFunction(Function function);
    //增加function
    public int addFunction(Function function);
    //删除function
    public int delFunction(String[]arrId);

    //根据function_no得到Function
    public List<Function> getFunctionByFunctionNo(@Param("function_no")String function_no);
}
