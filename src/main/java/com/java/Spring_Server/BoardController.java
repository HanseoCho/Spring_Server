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

import com.java.Spring_Server.DAO.DaoInterface;
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
	
	@Autowired
	DaoInterface di;
	
	
	@RequestMapping("/insert")
	public String insert(HttpSession session) {
		if(HttpUtil.checkLogin(session)) {
			return "board/insert";
		}
		else {
			return "redirect/login";
		}
		
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
	public String bUpdate(HttpSession session,HttpServletRequest req,RedirectAttributes ra) {
		if(HttpUtil.checkLogin(session)) {
			HashMap<String, Object> user = (HashMap<String, Object>) session.getAttribute("user");
			HashMap<String, Object> reqMap =  HttpUtil.getParamMap(req);
			String userNo1 = user.get("userNo").toString();
			
			reqMap.put("sqlType", "board.boardOne");
			reqMap.put("sql", "selectOne");
    		HashMap<String, Object> resultMap = (HashMap<String, Object>) di.call(reqMap);			
			String userNo2 = resultMap.get("userNo").toString();
			if(userNo1.equals(userNo2)) {
				return "board/update";	
			}
			else {
				ra.addFlashAttribute("userNo", userNo2);
				return "redirect:/bSelect";
			}	
		}
		else {
			return "redirect:/login";
			
		}
		
	}
	
	@RequestMapping("/bid")
	public ModelAndView bid(HttpServletRequest req, HttpSession session) {
		String boardTitle = req.getParameter("boardTitle");
		String boardContents = req.getParameter("boardContents");
		String data = req.getParameter("data");
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> userMap = (HashMap<String, Object>) session.getAttribute("user");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("boardTitle", boardTitle);
		params.put("boardContents", boardContents);
		params.put("userNo", userMap.get("userNo"));
		
		params.put("sqlType", "board.insert");
		params.put("sql", "insert");
		
		int status = (int) di.call(params);
		//int status = session.insert("board.insert",params);
		
		
		
		if(status==1) {
			//int boardNo = session.selectOne("board.getBoardNo");
			//System.out.println(boardNo);
			params = new HashMap<String,Object>();
			params.put("sqlType", "board.getBoardNo");
			params.put("sql", "selectOne");
			int boardNo = (int) di.call(params);
			//int boardNo = session.selectOne("board.getBoardNo");
			
			List<Map<String, Object>> list = JSONArray.fromObject(data);
			System.out.println(list);
			for(int i=0;i<list.size();i++) {
				String fileName = list.get(i).get("fileName").toString();
				String fileURL = list.get(i).get("fileURL").toString();
				System.out.println("fileName : "+ fileName + "fileURL : " + fileURL);				
				HashMap<String, Object> fileMap = new HashMap<String, Object>();
				//fileMap.put("boardNo", boardNo);
				fileMap.put("fileName", fileName);
				fileMap.put("fileURL",FinalUtil.File_DNS+fileURL);
				fileMap.put("userNo", userMap.get("userNo"));
				
				fileMap.put("sqlType", "board.fileinsert2");
				fileMap.put("sql", "insert");				
				
				
				//status = session.insert("board.fileinsert2",fileMap);
				status = (int) di.call(fileMap);
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
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("boardNo", boardNo);
		HashMap<String, Object> result = new HashMap<String, Object>();
		param.put("sqlType", "board.boardOne");
		param.put("sql", "selectOne");
		result.put("boardData",di.call(param));
		param.put("sqlType", "board.fileList");
		param.put("sql", "selectList");
		result.put("fileData",di.call(param));
		System.out.println(result);
		return HttpUtil.makeJsonView(result);
	}
	
	@RequestMapping("/bbld")
	public ModelAndView bbld() {
		HashMap<String, Object> param = new HashMap<>();
		param.put("sqlType", "board.boardList");
		param.put("sql", "selectList");		
		List list = (List) di.call(param);
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list",list);
		return HttpUtil.makeJsonView(resultMap);
	}

@RequestMapping("/bud")
public ModelAndView bud(HttpServletRequest req, HttpSession session) {
	String data = req.getParameter("data");
	if(HttpUtil.checkLogin(session)) {
	String boardTitle = req.getParameter("boardTitle");
	int boardNo = Integer.parseInt(req.getParameter("boardNo"));
	String boardContents = req.getParameter("boardContents");
	//String data = req.getParameter("data");
	String delData = req.getParameter("delData");
	HashMap<String, Object> user = (HashMap<String, Object>) session.getAttribute("user");
	
	HashMap<String, Object> map = new HashMap<String, Object>();

	HashMap<String, Object> params = new HashMap<String, Object>();
	params.put("boardNo", boardNo);
	params.put("boardTitle", boardTitle);
	params.put("boardContents", boardContents);
	params.put("userNo", user.get("userNo"));
	params.put("sqlType", "board.update");
	params.put("sql", "insert");
	
	int status = (int) di.call(params);
	//int status = session.insert("board.update",params);
	
	
	if(status==1) {
		//System.out.println(boardNo);
		List<JSONObject> delList = JSONArray.fromObject(delData);
		System.out.println("--------------------------------------------");
		System.out.println(delList);
		for(int i=0;i<delList.size();i++) {
			JSONObject j = delList.get(i);
			params = new HashMap<String, Object>();
			params.put("fileNo",j.get("fileNo"));
			params.put("sqlType", "board.fileDel");
			params.put("sql", "update");
			di.call(params);
			//session.update("board.fileDel",delList.get(i));
		}
		
		List<Map<String, Object>> list = JSONArray.fromObject(data);
		System.out.println(JSONArray.fromObject(data));
		System.out.println("list"+list);
		for(int i=0;i<list.size();i++) {
			String fileName = list.get(i).get("fileName").toString();
			String fileURL = list.get(i).get("fileURL").toString();
			System.out.println("fileName : "+ fileName + "fileURL : " + fileURL);				
			HashMap<String, Object> fileMap = new HashMap<String, Object>();			
			
			fileMap.put("boardNo", boardNo);
			fileMap.put("fileName", fileName);
			fileMap.put("fileURL",FinalUtil.File_DNS+fileURL);
			fileMap.put("userNo", user.get("userNo"));
			fileMap.put("sqlType", "board.fileinsert2");
			fileMap.put("sql", "insert");
			
			status = (int) di.call(fileMap);
			//status = session.insert("board.fileinsert2",fileMap);
		}		
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
		else {
			System.out.println("로그인이 필요합니다");
		}
	return null;
	}	
@RequestMapping("/dlb")
public String dlb(HttpServletRequest req) {
	HashMap<String, Object> map = new HashMap<String, Object>();	
	int boardNo = Integer.parseInt(req.getParameter("boardNo"));
	map.put("boardNo", boardNo);
	map.put("sqlType", "board.boardDel");
	map.put("sql", "update");	
	int status = (int) di.call(map);	

	map.put("sqlType", "board.fileDel");
	map.put("sql", "update");	
	int status2 = (int) di.call(map);	
	
	System.out.println("board삭제 여부 : " + status);
	System.out.println("file삭제 여부 : " + status2);
	

	return "redirect:/bLits";
	}
}
