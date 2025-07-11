package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.AppConfig;
import ru.netology.controller.PostController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private static final String API_POSTS = "/api/posts";
  private static final String API_POSTS_WITH_ID_REGEX = "/api/posts/\\d+";

  private PostController controller;

  @Override
  public void init() {
    // Используем Spring-контекст для внедрения зависимостей
    var context = new AnnotationConfigApplicationContext(AppConfig.class);
    controller = context.getBean(PostController.class);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();

      if (method.equals("GET") && path.equals(API_POSTS)) {
        controller.all(resp);
        return;
      }

      if (method.equals("GET") && path.matches(API_POSTS_WITH_ID_REGEX)) {
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
        controller.getById(id, resp);
        return;
      }

      if (method.equals("POST") && path.equals(API_POSTS)) {
        controller.save(req.getReader(), resp);
        return;
      }

      if (method.equals("DELETE") && path.matches(API_POSTS_WITH_ID_REGEX)) {
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
        controller.removeById(id, resp);
        return;
      }

      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}