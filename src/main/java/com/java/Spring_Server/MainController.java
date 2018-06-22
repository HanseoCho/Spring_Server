package com.java.Spring_Server;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.java.Spring_Server.util.HttpUtil;

@Controller
public class MainController {
	@RequestMapping("/")
	public String main(HttpServletRequest req,HttpSession session) {
		HashMap<String,Object> userMap = (HashMap<String, Object>) session.getAttribute("user");
		if(HttpUtil.checkLogin(session)){
			System.out.println("로그인중");
		}
		else {
			System.out.println("비로그인중");
		}
		return "home";
	}
}
