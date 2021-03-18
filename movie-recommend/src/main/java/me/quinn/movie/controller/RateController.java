package me.quinn.movie.controller;

import io.jsonwebtoken.Claims;
import me.quinn.movie.domain.Rate;
import me.quinn.movie.service.RateService;
import me.quinn.movie.utils.ResultVoUtil;
import me.quinn.movie.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class RateController {

    private RateService rateService;
    @Autowired
    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    /**
     * 用户进行评分来保存用户评分值
     * @param movieId
     * @param rate
     * @param request
     * @return
     */
    @RequestMapping("/api/jwt/rate/save.do")
    public ResultVo save(Long movieId,Double rate, HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        Rate rate1 = new Rate();
        rate1.setMovieId(movieId);
        rate1.setRate(rate);
        rate1.setUserId(Long.parseLong(String.valueOf(claims.get("userid"))));
        System.out.println(rate1);
        Rate save = rateService.save(rate1);
        return ResultVoUtil.success(save);
    }

    /**
     * 列出当前用户所有的评分信息
     * @param request
     * @return
     */
    @RequestMapping("/api/jwt/rate/list.do")
    public ResultVo findByUserId(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        List<Rate> rateList = rateService.findByUserId(Long.parseLong(String.valueOf(claims.get("userid"))));
        return ResultVoUtil.success(rateList);
    }

    /**
     * 通过电影分组查询评分
     * @return
     */
    @RequestMapping("/api/rate/all.do")
    public ResultVo findAllGroupByMovieId(){
        List<?> rateList = rateService.findAllGroupByMovieId();
        return ResultVoUtil.success(rateList);
    }

}
