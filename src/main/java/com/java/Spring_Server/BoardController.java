package com.java.Spring_Server;

import java.io.Console;
import java.io.Serializable;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebParam.Mode;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.java.Spring_Server.util.FinalUtil;
import com.java.Spring_Server.util.HttpUtil;
import com.java.Spring_Server.work.SessionControl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@Controller
public class BoardController {
	@Resource(name="sqlSession")
	SqlSession session;	
	
	@Autowired
	SessionControl sc;
	
	@RequestMapping("/")
	public String main(HttpServletRequest req,HttpSession session) {
		HttpSession hs;
		req.getSession();
		return "home";
	}
	
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	@RequestMapping("/user")
	public String user() {
		return "user";
	}
	
	@RequestMapping("/insert")
	public String insert() {
		return "board/insert";
	}
	@RequestMapping("/bSelect")
	public String view() {
		return "board/detail";
	}	
	@RequestMapping("/bLits")
	public String list() {
		return "board/list";
	}
	@RequestMapping("/bUpdate")
	public String bUpdate() {
		
		return "board/update";
	}
	
	@RequestMapping("/userinsert")
	public String userinsert(HttpServletRequest req) {
		String userEmail = req.getParameter("userEmail");
		String userPassword = req.getParameter("userPassword");
		String userName = req.getParameter("userName");
		System.out.println(userEmail+userPassword+userName);
		Map<String, String> param = new HashMap<String, String>();
		param.put("userEmail", userEmail);
		param.put("userPassword", userPassword);
		param.put("userName", userName);
		int result = session.insert("user.insert",param);
		System.out.println("result : "+ result);
		return "redirect:/";
	}
	
	@RequestMapping("/userSelect")
	public String userSelect(HttpServletRequest req,Model m, RedirectAttributes ria,HttpSession hsession) {
		String userEmail = req.getParameter("userEmail");
		String userPassword = req.getParameter("userPassword");
		Map<String, String> param = new HashMap<String, String>();
		param.put("userEmail", userEmail);
		param.put("userPassword", userPassword);
		HashMap<String, Object> result = new HashMap<String, Object>();
		System.out.println("result : "+ result);
		
		result = session.selectOne("user.select",param);

		System.out.println(result);
		if(result==null) {
			result = new HashMap<String, Object>();
			result.put("status", FinalUtil.no);
		}else {
			result.put("status", FinalUtil.ok);			
		}
		ria.addFlashAttribute("data",result);
		return "redirect:/";
	}	
	
	@RequestMapping("/bid")
	public ModelAndView bid(HttpServletRequest req) {
		String boardTitle = req.getParameter("boardTitle");
		String boardContents = req.getParameter("boardContents");
		String data = req.getParameter("data");
		
		HashMap<String, Object> map = new HashMap<String, Object>();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("boardTitle", boardTitle);
		params.put("boardContents", boardContents);
		params.put("userNo", 1);
		int status = session.insert("board.insert",params);
		
		
		if(status==1) {
			int boardNo = session.selectOne("board.getBoardNo");
			//System.out.println(boardNo);
			
			List<Map<String, Object>> list = JSONArray.fromObject(data);
			System.out.println(list);
			for(int i=0;i<list.size();i++) {
				String fileName = list.get(i).get("fileName").toString();
				String fileURL = list.get(i).get("fileURL").toString();
				System.out.println("fileName : "+ fileName + "fileURL : " + fileURL);				
				Map<String, Object> fileMap = new HashMap<String, Object>();
				//fileMap.put("boardNo", boardNo);
				fileMap.put("fileName", fileName);
				fileMap.put("fileURL",FinalUtil.File_DNS+fileURL);
				fileMap.put("userNo", 1);
				
				status = session.insert("board.fileinsert2",fileMap);
			}		
			if(status == 1) {
				map.put("msg","글작성 완료");
				map.put("status", FinalUtil.ok);
				map.put("boardNo",boardNo);
			}else {
				map.put("msg","첨부파일 오류발생");
				map.put("status", FinalUtil.no);
			}
		} else {
			map.put("msg","글 작성시 오류 발생");
			map.put("status", FinalUtil.no);
		}
/*		String str = req.getParameter("data");
		JSONObject jo = JSONObject.fromObject(str);
		System.out.println(jo);*/
		return HttpUtil.makeJsonView(map);
	}
	@RequestMapping("/bld")
	public ModelAndView bld(HttpServletRequest req) {
		String boardNo = req.getParameter("boardNo");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("boardNo", boardNo);
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("boardData",session.selectOne("board.boardOne",param));
		result.put("fileData",session.selectList("board.fileList",param));
		System.out.println(result);
		return HttpUtil.makeJsonView(result);
	}
	
	@RequestMapping("/bbld")
	public ModelAndView bbld() {
		List list  = session.selectList("board.boardList");
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", list);
		return HttpUtil.makeJsonView(resultMap);
	}

@RequestMapping("/bud")
public ModelAndView bud(HttpServletRequest req) {
	String boardTitle = req.getParameter("boardTitle");
	int boardNo = Integer.parseInt(req.getParameter("boardNo"));
	String boardContents = req.getParameter("boardContents");
	//String data = req.getParameter("data");
	String delData = req.getParameter("delData");
	
	HashMap<String, Object> map = new HashMap<String, Object>();

	Map<String, Object> params = new HashMap<String, Object>();
	params.put("boardNo", boardNo);
	params.put("boardTitle", boardTitle);
	params.put("boardContents", boardContents);
	params.put("userNo", 1);
	int status = session.insert("board.update",params);
	
	
	if(status==1) {
		//System.out.println(boardNo);
		List<Map<String, Object>> delList = JSONArray.fromObject(delData);
		for(int i=0;i<delList.size();i++) {
			session.update("board.fileDel",delList.get(i));
		}
		
/*		List<Map<String, Object>> list = JSONArray.fromObject(data);
		System.out.println(list);
		for(int i=0;i<list.size();i++) {
			String fileName = list.get(i).get("fileName").toString();
			String fileURL = list.get(i).get("fileURL").toString();
			System.out.println("fileName : "+ fileName + "fileURL : " + fileURL);				
			Map<String, Object> fileMap = new HashMap<String, Object>();
			//fileMap.put("boardNo", boardNo);
			fileMap.put("fileName", fileName);
			fileMap.put("fileURL",FinalUtil.File_DNS+fileURL);
			fileMap.put("userNo", 1);
			
			status = session.insert("board.fileinsert2",fileMap);
		}	*/	
		if(status == 1) {
			map.put("msg","글수정 완료");
			map.put("status", FinalUtil.ok);
			map.put("boardNo",boardNo);
		}else {
			map.put("msg","첨부파일 오류발생");
			map.put("status", FinalUtil.no);
		}
	} else {
		map.put("msg","글 수정시 오류 발생");
		map.put("status", FinalUtil.no);
	}
/*		String str = req.getParameter("data");
	JSONObject jo = JSONObject.fromObject(str);
	System.out.println(jo);*/
	return HttpUtil.makeJsonView(map);
	}	
@RequestMapping("/dlb")
public ModelAndView dlb(HttpServletRequest req) {
	HashMap<String, Object> map = new HashMap<String, Object>();	
	int boardNo = Integer.parseInt(req.getParameter("boardNo"));
	map.put("boardNo", boardNo);
	int status = session.insert("board.boardDel",map);	

	if(status == 1) {
		map.put("msg","글삭제 완료");
		map.put("status", FinalUtil.ok);
		map.put("boardNo",boardNo);
	}else {
		map.put("msg","첨부파일 오류발생");
		map.put("status", FinalUtil.no);
	}

	return HttpUtil.makeJsonView(map);
	}
}
