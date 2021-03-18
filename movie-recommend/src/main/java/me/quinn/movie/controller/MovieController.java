package me.quinn.movie.controller;


import io.jsonwebtoken.Claims;
import me.quinn.movie.domain.Movie;
import me.quinn.movie.service.MovieService;
import me.quinn.movie.service.impl.RecommendServiceImpl;
import me.quinn.movie.utils.ResultVoUtil;
import me.quinn.movie.vo.ResultVo;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
public class MovieController {

    @Value("${recommend.size}")
    private Integer recommendSize;

    private MovieService movieService;
    private RecommendServiceImpl recommendService;
    @Autowired
    public MovieController(MovieService movieService, RecommendServiceImpl recommendService) {
        this.movieService = movieService;
        this.recommendService = recommendService;
    }

    /**
     * 上传影片封面以及影片名称，上传到OSS
     * @param file
     * @param movieName
     * @return
     * @throws IOException
     */
    @RequestMapping("/api/jwt/movie/upload.do")
    public ResultVo upload(MultipartFile file,String movieName) throws IOException {
        if(file.isEmpty()){
            return ResultVoUtil.error(400,"error");
        }
        String imgName = movieService.uploadPictrue(file,movieName);
        Movie movie = new Movie();
        movie.setMovieName(movieName);
        movie.setImgName(imgName);
        movieService.save(movie);
        return ResultVoUtil.success(imgName);
    }

    /**
     * 查找所有的影片
     * @return
     */
    @RequestMapping("/api/movie/list.do")
    public ResultVo list(){
        List<Movie> movieList = movieService.findAll();
        return ResultVoUtil.success(movieList);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/api/movie/listPage.do")
    public ResultVo list(Integer page,Integer size){
        Page<Movie> movieList = movieService.findAll(page,size);
        return ResultVoUtil.success(movieList);
    }

    /**
     * 通过影片id返回Movie
     * @param id
     * @return
     */
    @RequestMapping("/api/movie/info.do")
    public ResultVo list(Long id){
        Movie movie  = movieService.getOne(id);
        return ResultVoUtil.success(movie);
    }

    /**
     * 获取token中的数据来推荐视频
     * @param request
     * @return
     * @throws TasteException
     */
    @RequestMapping("/api/jwt/movie/recommend.do")
    public ResultVo recommend(HttpServletRequest request) throws TasteException {
        Claims claims = (Claims) request.getAttribute("claims");
        List<RecommendedItem> recommendedItems = recommendService.recommenderItem(Long.parseLong(String.valueOf(claims.get("userid"))), recommendSize);
        return ResultVoUtil.success(recommendedItems);
    }

}
