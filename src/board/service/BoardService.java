package board.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

import board.dao.BoardDao;
import board.model.BoardCommentModel;
import board.model.BoardModel;

public class BoardService implements BoardDao {

	private SqlMapClientTemplate sqlMapClientTemplate;
	private HashMap<String, Object> valueMap = new HashMap<String,Object>();
	
	
	
	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	@Override
	public List<BoardModel> SelectAllBoard(int startArticleNum, int endArticleNum) {
		valueMap.put("startArticleNum", startArticleNum);
		valueMap.put("endArticleNum", endArticleNum);
		
		return sqlMapClientTemplate.queryForList("board.SelectAllBoard",valueMap);
	}

	@Override
	public BoardModel SelectOneBoard(int idx) {

		return (BoardModel) sqlMapClientTemplate.queryForObject("board.SelectOneBoard",idx);
	}

	@Override
	public List<BoardModel> searchBoard(String type, String keyword, int startArticleNum, int endArticleNum) {
		valueMap.put("type", type);
		valueMap.put("keyword", keyword);
		valueMap.put("startArticleNum", startArticleNum);
		valueMap.put("endArticleNum", endArticleNum);
		return sqlMapClientTemplate.queryForList("board.searchBoard",valueMap);
	}

	@Override
	public List<BoardCommentModel> SelectAllComment(int idx) {
		return sqlMapClientTemplate.queryForList("board.SelectAllComment",idx);
	}

	@Override
	public BoardCommentModel SelectOneComment(int idx) {
		return (BoardCommentModel) sqlMapClientTemplate.queryForObject("board.SelectOneComment",idx);
	}

	@Override
	public boolean modifyBoard(BoardModel board) {
		sqlMapClientTemplate.update("board.modifyBoard", board);
		return true;
	}

	@Override
	public boolean writeBoard(BoardModel board) {
		sqlMapClientTemplate.insert("board.writeBoard",board);
		return true;
	}

	@Override
	public boolean writeComment(BoardCommentModel comment) {
		sqlMapClientTemplate.insert("board.writeComment",comment);
		return true;
	}

	@Override
	public void updateHitcount(int hitcount, int idx) {
		valueMap.put("hitcount", hitcount);
		valueMap.put("idx", idx);
		sqlMapClientTemplate.update("board.updateHitcount",valueMap);
	}

	@Override
	public void updateRecommendcount(int recommendcount, int idx) {
		valueMap.put("recommendcount", recommendcount);
		valueMap.put("idx", idx);
		sqlMapClientTemplate.update("board.updateRecommendcount",valueMap);

	}

	@Override
	public int TotalNum() {
		return (Integer) sqlMapClientTemplate.queryForObject("board.TotalNum");
	}

	@Override
	public int SearchTotalNum(String type, String keyword) {
		valueMap.put("type", type);
		valueMap.put("keyword", keyword);
		return (Integer) sqlMapClientTemplate.queryForObject("board.SearchTotalNum",valueMap);
	}

	@Override
	public void deleteComment(int idx) {
		sqlMapClientTemplate.delete("board.deleteComment", idx);
	}

	@Override
	public void deleteBoard(int idx) {
		sqlMapClientTemplate.delete("board.deleteBoard", idx);
	}

}
