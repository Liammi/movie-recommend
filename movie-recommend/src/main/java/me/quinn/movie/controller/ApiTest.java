package me.quinn.movie.controller;

import io.jsonwebtoken.Claims;
import me.quinn.movie.utils.ResultVoUtil;
import me.quinn.movie.vo.ResultVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ApiTest {

    @RequestMapping("/api/jwt/v.do")
    public ResultVo show(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        return ResultVoUtil.success(claims.get("userid"));
    }

    @RequestMapping("/ping")
    public String show(){
        return "I`m Live";
    }

}
