package com.prweb.service;

import com.prweb.entity.News;

public interface NewsService {
    //模糊搜索带分页
    public String getAllByLike(String username,String title,int skip,int take);

    //增加、修改News
    public String saveNews(String idStr,String username,String title,String content,String publish_time);

    //删除News
    public String delNews(String hlparam);

    //根据id查询News
    public String getNewsById(int id);
}
