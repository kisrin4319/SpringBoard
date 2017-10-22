package member.service;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

import member.dao.MemberDao;
import member.model.MemberModel;

public class MemberService implements MemberDao {

	private SqlMapClientTemplate sqlMapClientTemplate;
	
	@Override
	public boolean addMember(MemberModel memberModel) {
		sqlMapClientTemplate.insert("member.insertMember",memberModel);
		MemberModel checkAddMember = findById(memberModel.getMember_id());
		//check addMember Process
		if(checkAddMember == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public MemberModel findById(String member_id) {
		return (MemberModel) sqlMapClientTemplate.queryForObject("member.findById",member_id);
	}

	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}
}
