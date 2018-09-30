package com.prweb.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.NewsDao;
import com.prweb.entity.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NewsServiceImpl implements NewsService{
    @Autowired
    private NewsDao newsDao;

    @Override
    public String getAllByLike(String username, String title, int skip, int take) {
        List<HashMap<String,Object>> list=newsDao.getAllByLike(username,title,skip,take);
        int count=newsDao.getCountAllByLike(username,title);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }

    @Override
    public String saveNews(String idStr,String username,String title,String content,String publish_time) {
        String mmp="";
        JSONObject json = new JSONObject();
        try{

            if(username!=null&&idStr!=null&&!"".equals(idStr)){
                if(title!=null&&!"".equals(title)&&content!=null&&!"".equals(content)&&publish_time!=null&&!"".equals(publish_time)){
                    int id=Integer.parseInt(idStr);
                    int resTotal=0;
                    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date time=format.parse(publish_time);
                    News news=new News();
                    news.setUsername(username);
                    news.setTitle(title);
                    news.setContent(content);
                    news.setPublish_time(time);
                    if(id==0){
                        //添加
                        resTotal=newsDao.addNews(news);
                    }else{
                        //修改！
                        news.setId(id);
                        resTotal=newsDao.updateNews(news);
                    }
                    if(resTotal>0){
                        json.put("success",true);
                        json.put("message","保存成功");
                    }else{
                        json.put("success",false);
                        json.put("message","保存失败");
                    }
                }else{
                    json.put("success",false);
                    json.put("message","保存失败,有内容为空,请检查!");
                }
            }else{
                json.put("success",false);
                json.put("message","已掉线,请重新登录!");
                mmp= JSONObject.toJSONString(json);
            }
            mmp= JSONArray.toJSONString(json);
        }catch (Exception ex){
            json.put("success",false);
            json.put("message","保存失败");
        }
        return mmp;
    }

    @Override
    public String delNews(String hlparam) {
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=newsDao.delNews(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项新闻删除成功\n");
        if(resTotal>0){
            json.put("success",true);
        }else{
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        String mmp= JSONArray.toJSONString(json);
        return mmp;
    }

    @Override
    public String getNewsById(int id) {
        String mmp="";
        JSONObject json = new JSONObject();
        try{
            News news=newsDao.getNewsById(id);
            json.put("success",true);
            if(news!=null){
                json.put("message",news);
            }else{
                json.put("message","");
            }
            mmp= JSONArray.toJSONString(json);
        }catch (Exception ex){
            json.put("success",false);
            json.put("message","获取");
        }
        return mmp;
    }
}
