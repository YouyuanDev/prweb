package com.prweb.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.CommentDao;
import com.prweb.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Override
    public String getAvgRatingByCompanyNo(String company_no) {
        JSONObject json=new JSONObject();
        List<HashMap<String,Object>>lt=commentDao.getAvgRatingByCompanyNo(company_no);
        if(lt!=null&&lt.size()>0){
            String resTotal=String.valueOf(lt.get(0).get("rating"));
            json.put("success",true);
            json.put("message",resTotal);
        }else{
            json.put("success",false);
            json.put("message","获取失败");
        }
        String map= JSONObject.toJSONString(json);
        return map;
    }

    public String getCommentByLike(String comment_from_person_user_no,String comment_to_comany_no,int start,int rows){
        List<HashMap<String,Object>> list=commentDao.getAllByLike(comment_from_person_user_no,comment_to_comany_no,start,rows);
        int count=commentDao.getCountAllByLike(comment_from_person_user_no,comment_to_comany_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        //System.out.print("mmp:"+mmp);
        return mmp;
    }


    public String saveComment(Comment comment){
        int resTotal=0;
        JSONObject json=new JSONObject();
        if(comment.getComment_time()==null){
            comment.setComment_time(new Date());
        }
        if(comment.getId()==0){
            //添加
            String uuuid= UUID.randomUUID().toString();
            uuuid=uuuid.replace("-","");
            comment.setComment_no(uuuid);
            resTotal=commentDao.addComment(comment);
        }else{
            //修改！
            resTotal=commentDao.updateComment(comment);
        }
        if(resTotal>0){
            json.put("success",true);
            json.put("message","保存成功");
        }else{
            json.put("success",false);
            json.put("message","保存失败");
        }
        String map= JSONObject.toJSONString(json);
        return map;
    }


    public String delComment(String hlparam){
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=commentDao.delComment(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项评论信息删除成功\n");
        if(resTotal>0){
            //System.out.print("删除成功");
            json.put("success",true);
        }else{
            //System.out.print("删除失败");
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        String map= JSONObject.toJSONString(json);
        return map;
    }

    //获取某个订单的评论
    public String getCommentByOrderNo(String order_no){
        JSONObject json=new JSONObject();
        List<HashMap<String,Object>> lt=commentDao.getCommentByOrderNo(order_no);
        if(lt.size()>0){
            json.put("success",true);
            json.put("message","存在评论记录"+lt.size()+"条");
            json.put("data",lt);

        }else{
            json.put("success",false);
            json.put("message","不存在评论记录");
        }
        String map= JSONObject.toJSONString(json);
        return map;
    }

    //获取某个商户的评论
    public String getCommentByCompanyNo(String company_no,int start,int rows){
        JSONObject json=new JSONObject();
        List<HashMap<String,Object>> lt=commentDao.getCommentByCompanyNo(company_no, start, rows);
        int count=commentDao.getCountAllByCompanyNo(company_no);
        if(lt.size()>0){
            json.put("success",true);
            json.put("message","存在评论记录"+lt.size()+"条");
            json.put("data",lt);
            json.put("count",count);
        }else{
            json.put("success",false);
            json.put("message","不存在评论记录");
        }
        String map= JSONObject.toJSONString(json);
        return map;
    }

}
