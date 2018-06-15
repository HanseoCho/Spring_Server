package com.java.Spring_Server.work;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

@Component
public class SessionControl {
	public void set(HttpSession session,HashMap<String, Object> params) {
		System.out.println("params : " + params);
		Set<String> pkey= params.keySet();
		ArrayList<String> key = new ArrayList<String>();
		for(Iterator<String> i = pkey.iterator();i.hasNext();) {
			key.add(i.next());
		}
		for(int i=0;i<key.size();i++) {
			session.setAttribute(key.get(i),params.get(key.get(i)));
		}
		
	}
}
