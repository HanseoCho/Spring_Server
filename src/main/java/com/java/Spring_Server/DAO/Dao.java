package com.java.Spring_Server.DAO;

import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class Dao implements DaoInterface {
	@Resource(name="sqlSession")
	SqlSession session;	

		@Override
		public Object call(HashMap<String, Object> param) {
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			int resultint = 0;
			String sql = param.get("sql").toString();
			String sqltype = param.get("sqlType").toString();
			
			if("selectOne".equals(sql)) {
				return session.selectOne(sqltype,param);
			}
			else if("insert".equals(sql)) {
				return session.insert(sqltype,param);
			}
			else if("update".equals(sql)) {
				return session.update(sqltype,param);
			}
			else if("selectList".equals(sql)) {
				return session.selectList(sqltype,param);
			}else {			
				System.out.println("sql조건절 추가하자 sql : " + sql);
			}
			
			return null;
		}

}
