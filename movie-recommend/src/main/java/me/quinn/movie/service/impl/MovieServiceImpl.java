package me.quinn.movie.service.impl;

import com.aliyun.oss.OSSClient;
import me.quinn.movie.domain.Movie;
import me.quinn.movie.enums.OssConstant;
import me.quinn.movie.repository.MovieRepository;
import me.quinn.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
public class MovieServiceImpl implements MovieService {

    private MovieRepository movieRepository;
    private OSSClient ossClient;
    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository,OSSClient ossClient) {
        this.movieRepository = movieRepository;
        this.ossClient = ossClient;
    }

    @Override
    public void save(Movie movie) {
        movieRepository.save(movie);
    }

    @Override
    public Page<Movie> findAll(Integer page,Integer size) {
        return movieRepository.findAll(new QPageRequest(page,size));
    }

    @Override
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }
    @Override
    public Movie getOne(Long id) {
        return movieRepository.getOne(id);
    }

    /**
     *
     * @param file
     * @param movieName
     * @return imgName
     */
    @Override
    public String uploadPictrue(MultipartFile file, String movieName) {
        String picUrl = "";
        try{
            String fileName = UUID.randomUUID().toString().replace("-","")+
                    "_"+
                    file.getOriginalFilename();
            picUrl = uploadToOss(fileName,new ByteArrayInputStream(file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return picUrl;
    }

    /**
     * 上传图片
     *
     * @param fileName 图片名称，图片名称包括文件夹名称和“/”
     * @param in 图片输入流
     */
    private String uploadToOss(String fileName, InputStream in) {
        // 上传Object.
        ossClient.putObject(OssConstant.BUCKET, fileName, in);
        //返回oss服务器访问上传图片的url
        return "https://" + OssConstant.BUCKET + "." + OssConstant.END_POINT + "/" + fileName;
    }
}
