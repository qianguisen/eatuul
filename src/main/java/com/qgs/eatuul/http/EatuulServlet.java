package com.qgs.eatuul.http;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.qgs.eatuul.exception.ZuulException;

@WebServlet(name = "eatuul", urlPatterns = "/eat/*")
public class EatuulServlet extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		eatRunner = new EatRunner();
	}

	private EatRunner eatRunner;

	@Override
	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 将request，和response放入上下文对象中
		eatRunner.init(req, resp);
		try {
			try {
				preRoute();
			} catch (ZuulException e) {
				error(e);
				postRoute();
				return;
			}
			try {
				route();
			} catch (ZuulException e) {
				error(e);
				postRoute();
				return;
			}
			try {
				postRoute();
			} catch (ZuulException e) {
				error(e);
				return;
			}

		} catch (Throwable e) {
			RequestContext.getCurrentContext().getResponse().sendError(HttpServletResponse.SC_BAD_GATEWAY,
					e.getMessage());
		} finally {
			// 清除变量
			RequestContext.getCurrentContext().unset();
		}
	}

	private void error(ZuulException e) throws ZuulException{
		// 执行过滤
		eatRunner.error();
	}

	private void route() throws ZuulException {
		// 执行过滤
		eatRunner.route();
	}

	private void postRoute() throws ZuulException {
		// 执行后置过滤
		eatRunner.postRoute();
	}

	private void preRoute() throws ZuulException {
		// 执行前置过滤
		eatRunner.preRoute();

	}

}