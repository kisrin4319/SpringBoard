package board.model;

public class BoardCommentModel {

	private int idx;
	private String writer;
	private String content;
	private String writeDate;
	private int boardidx;
	private String writerId;
	
	
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getWriteDate() {
		return writeDate;
	}
	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}
	public int getBoardidx() {
		return boardidx;
	}
	public void setBoardidx(int boardidx) {
		this.boardidx = boardidx;
	}
	public String getWriterId() {
		return writerId;
	}
	public void setWriterId(String writerId) {
		this.writerId = writerId;
	}

	
}