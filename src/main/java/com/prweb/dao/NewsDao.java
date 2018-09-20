package com.prweb.dao;

import com.prweb.entity.News;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface NewsDao {
    //模糊搜索带分页
    public List<HashMap<String,Object>> getAllByLike(@Param("username")String username, @Param("title")String title, @Param("skip")int skip, @Param("take")int take);

    //模糊搜索总数
    public int getCountAllByLike(@Param("username")String username, @Param("title")String title);

    //修改News
    public int updateNews(News role);
    //增加News
    public int addNews(News role);
    //删除News
    public int delNews(String[]arrId);

}
