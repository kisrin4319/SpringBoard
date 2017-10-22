package board.dao;

import java.util.List;

import board.model.BoardCommentModel;
import board.model.BoardModel;

public interface BoardDao {
	//get all contents in board table
	List<BoardModel> SelectAllBoard(int startArticleNum,int showArticleLimit);
	
	//show detail about selected board
	BoardModel SelectOneBoard(int idx);
	
	//get search result list
	List<BoardModel> searchBoard(String type, String keyword,int startArticleNum,int endArticleNum);
	
	//get all comments
	List<BoardCommentModel> SelectAllComment(int idx);
	
	//get a comment
	BoardCommentModel SelectOneComment(int idx);
	
	//modify(update) board
	boolean modifyBoard(BoardModel board);
	
	// insert board data
	boolean writeBoard(BoardModel board);
	
	//insert comment data
	
	boolean writeComment(BoardCommentModel comment);
	
	//update hitcount
	
	void updateHitcount(int hitcount , int idx);

	//update recommendcount
	
	void updateRecommendcount(int recommendcount, int idx);
	
	//get contents count
	int TotalNum();
	
	//get count of search results
	
	int SearchTotalNum(String type, String keyword);
	
	// delete a comment
	
	void deleteComment(int idx);
	
	//delete a board
	
	void deleteBoard(int idx);
}
