package ru.netology.controller;

import com.google.gson.Gson;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;


public class PostController {
  public static final String APPLICATION_JSON = "application/json";
  private final PostService service;

  public PostController(PostService service) {
    this.service = service;
  }

  public void all(HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    final var gson = new Gson();
    response.getWriter().print(gson.toJson(service.all()));
  }

  public void getById(long id, HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    final var gson = new Gson();
    try {
      final var post = service.getById(id);
      response.getWriter().print(gson.toJson(post));
    } catch (NotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().print("{\"error\": \"Post not found\"}");
    }
  }

  public void save(Reader body, HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    final var gson = new Gson();
    final var post = gson.fromJson(body, Post.class);
    final var data = service.save(post);
    response.getWriter().print(gson.toJson(data));
  }

  public void removeById(long id, HttpServletResponse response) {
    service.removeById(id);
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }
}
