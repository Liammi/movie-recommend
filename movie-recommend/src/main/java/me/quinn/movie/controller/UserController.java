package me.quinn.movie.controller;

import me.quinn.movie.domain.Audience;
import me.quinn.movie.domain.User;
import me.quinn.movie.service.UserService;
import me.quinn.movie.utils.JwtHelper;
import me.quinn.movie.utils.ResultVoUtil;
import me.quinn.movie.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RequestMapping
@RestController
public class UserController {

    // 使用构造器自动注入对象
    private UserService userService;
    private Audience audience;
    @Autowired
    public UserController(UserService userService, Audience audience) {
        this.userService = userService;
        this.audience = audience;
    }

    /**
     * 如果校验成功，将结果存入数据库
     * @param user
     * @return 返回Token
     */
    @RequestMapping("/api/user/sign.do")
    @ResponseBody
    public ResultVo userAdd(User user){
        System.out.println(audience.toString());
        User user1 = userService.save(user);
        // 如果数据库保存成功时，返回
        if(null!=user1 && user.getPassword().equals(user1.getPassword())){
            user1.setPassword(null);

            String jwtToken = JwtHelper.createJWT(user1.getUserName(),
                    String.valueOf(user1.getId()),
//                query_user.getRole().toString(),
                    "",
                    audience.getClientId(),
                    audience.getName(),
                    audience.getExpiresSecond()*1000,
                    audience.getBase64Secret());

            String resultStr = "bearer;" + jwtToken;
            return ResultVoUtil.success(resultStr);
        }
        return null;
    }

    /**
     * 判断用户名是否已被注册
     * @param userName
     * @return true,false
     */
    @RequestMapping("/api/user/validate.do")
    @ResponseBody
    public boolean userValidate(String userName){
        User user = userService.findByUserName(userName);
        return user==null;
    }

    /**
     *
     * @param user
     * @return 如果验证没有此用户名则返回用户名错误状态码
     * 如果密码错误则返回错误信息
     * 如果成功则返回成功状态码
     */
    @PostMapping("/api/user/login.do")
    public ResultVo login(User user) {

        User queryUser = userService.findByUserName(user.getUserName());
        if (queryUser == null) {
            return ResultVoUtil.error(400, "用户名错误");
        }
        //验证密码
        if(!user.getPassword().equals(queryUser.getPassword())) {
            return ResultVoUtil.error(400, "密码错误");
        }
        String jwtToken = JwtHelper.createJWT(queryUser.getUserName(),
                String.valueOf(queryUser.getId()),
//                query_user.getRole().toString(),
                "",
                audience.getClientId(),
                audience.getName(),
                audience.getExpiresSecond()*1000,
                audience.getBase64Secret());

        String resultStr = "bearer;" + jwtToken;
        return ResultVoUtil.success(resultStr);
    }
}
