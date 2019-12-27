package com.wzh.service.serviceImpl;

import com.wzh.mapper.ItripCommentMapper;
import com.wzh.mapper.ItripHotelOrderMapper;
import com.wzh.mapper.ItripImageMapper;
import com.wzh.po.ItripComment;
import com.wzh.po.ItripImage;
import com.wzh.service.ItripCommentService;
import com.wzh.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ItripCommentServiceImpl implements ItripCommentService {
    @Autowired
    private ItripCommentMapper itripCommentMapper;
    @Autowired
    private ItripImageMapper itripImageMapper;
    @Autowired
    private ItripHotelOrderMapper itripHotelOrderMapper;

    public ItripImageMapper getItripImageMapper() {
        return itripImageMapper;
    }

    public void setItripImageMapper(ItripImageMapper itripImageMapper) {
        this.itripImageMapper = itripImageMapper;
    }

    public ItripHotelOrderMapper getItripHotelOrderMapper() {
        return itripHotelOrderMapper;
    }

    public void setItripHotelOrderMapper(ItripHotelOrderMapper itripHotelOrderMapper) {
        this.itripHotelOrderMapper = itripHotelOrderMapper;
    }

    public ItripCommentMapper getItripCommentMapper() {
        return itripCommentMapper;
    }

    public void setItripCommentMapper(ItripCommentMapper itripCommentMapper) {
        this.itripCommentMapper = itripCommentMapper;
    }

    @Override
    public ItripScoreCommentVO getCommentAvgScore(Long id) throws Exception {
        return itripCommentMapper.getCommentAvgScore(id);
    }

    @Override
    public Integer getItripCommentCountByMap(Map<String, Object> param) throws Exception {
        return itripCommentMapper.getItripCommentCountByMap(param);
    }

    @Override
    public Page<ItripListCommentVO> getItripCommentListByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        Integer totel=itripCommentMapper.getItripCommentCountByMap(param);
        pageNo= EmptyUtils.isEmpty(pageNo)? Constants.DEFAULT_PAGE_NO:pageNo;
        pageSize=EmptyUtils.isEmpty(pageSize)?Constants.DEFAULT_PAGE_SIZE:pageSize;
        Page page=new Page(pageNo,pageSize,totel);
        param.put("beginPos",page.getBeginPos());
        param.put("pageSize",page.getPageSize());
        List<ItripListCommentVO> itripListCommentVOS=itripCommentMapper.getItripCommentListByMap(param);
        page.setRows(itripListCommentVOS);
        return page;
    }

    @Override
    public boolean itriptxAddItripComment(ItripComment obj, List<ItripImage> itripImages) throws Exception {
        if(null != obj ){
            //计算综合评分，综合评分=(设施+卫生+位置+服务)/4
            float score = 0;
            int sum = obj.getFacilitiesScore()+obj.getHygieneScore()+obj.getPositionScore()+obj.getServiceScore();
            score = BigDecimalUtil.OperationASMD(sum,4, BigDecimalUtil.BigDecimalOprations.divide,1, BigDecimal.ROUND_DOWN).floatValue();
            //对结果四舍五入
            obj.setScore(Math.round(score));
            Long commentId = 0L;
            if(itripCommentMapper.insertItripComment(obj) > 0 ){
                commentId = obj.getId();
                if(null != itripImages && itripImages.size() > 0 && commentId > 0){
                    for (ItripImage itripImage:itripImages) {
                        itripImage.setTargetId(commentId);
                        itripImageMapper.insertItripImage(itripImage);
                    }
                }
                //更新订单表-订单状态为4（已评论）
                itripHotelOrderMapper.updateHotelOrderStatus(obj.getOrderId(),obj.getCreatedBy());
                return true;
            }
        }
        return false;
    }
}
