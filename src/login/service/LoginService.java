package login.service;

import org.springframework.orm.ibatis.SqlMapClientTemplate;
import login.dao.LoginDao;
import login.model.LoginSessionModel;

public class LoginService implements LoginDao {
	private SqlMapClientTemplate sqlMapClientTemplate;
	
	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}


	@Override
	public LoginSessionModel checkUserId(String member_id) {
		return (LoginSessionModel)sqlMapClientTemplate.queryForObject("login.loginCheck",member_id);
	}

}
