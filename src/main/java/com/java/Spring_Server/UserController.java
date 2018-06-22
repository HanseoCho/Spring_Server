package com.java.Spring_Server;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.java.Spring_Server.util.FinalUtil;
import com.java.Spring_Server.util.HttpUtil;
import com.java.Spring_Server.work.SessionControl;

@Controller
public class UserController {	
	@Autowired
	SessionControl sc;	
	
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	@RequestMapping("/user")
	public String user() {
		return "user";
	}
	@Autowired
	com.java.Spring_Server.DAO.DaoInterface dl;

	@RequestMapping("/userinsert")
	public String userinsert(HttpServletRequest req) {
		HashMap<String, Object> param = HttpUtil.getParamMap(req);

		param.put("sqlType", "user.insert");
		param.put("sql", "insert");
		
		//HashMap<String, Object> resultMap = (HashMap<String, Object>) dl.call(param);
		int status =  (Integer) dl.call(param);
		
		//int result = session.insert("user.insert",param);
		System.out.println("result : "+ status);
		return "redirect:/";
	}
	
	@RequestMapping("/userSelect")
	public String userSelect(HttpServletRequest req,Model m, RedirectAttributes ria,HttpSession session) {
		HashMap<String, Object> param = HttpUtil.getParamMap(req);		
		param.put("sqlType", "user.select");
		param.put("sql", "selectOne");
		
		HashMap<String, Object> result =  (HashMap<String, Object>) dl.call(param);
		//HashMap<String, Object> result = new HashMap<String, Object>();		
		//result = session.selectOne("user.select",param);

		System.out.println(result);
		if(result==null) {
			result = new HashMap<String, Object>();
			result.put("status", FinalUtil.no);
		}else {
			result.put("status", FinalUtil.ok);

		}
		session.setAttribute("user", result);		
		//ria.addFlashAttribute("data",result);
		return "redirect:/";
	}
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}
