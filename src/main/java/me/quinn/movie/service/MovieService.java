package me.quinn.movie.service;

import me.quinn.movie.domain.Movie;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MovieService {
    void save(Movie movie);

    Page<Movie> findAll(Integer page,Integer size);
    List<Movie> findAll();
    Movie getOne(Long id);
    String uploadPictrue(MultipartFile file, String movieName);
}
