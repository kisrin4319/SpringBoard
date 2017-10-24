package board.controller;

import java.io.File;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import board.model.BoardCommentModel;
import board.model.BoardModel;
import board.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController implements ApplicationContextAware {

	private WebApplicationContext webcontext = null;
	//DI
	private ApplicationContext context = new ClassPathXmlApplicationContext("/config/applicationContext.xml");
	private BoardService boardService = (BoardService) context.getBean("boardService");
	
	//
	// User variable
	// board, page variables
	
	private int currentPage = 1;
	private int showArticleLimit = 10; //change value if want to show more boards by one page
	private int showPageLimit = 10; //change value if want to show more page links
	private int startArticleNum = 0;
	private int endArticleNum = 0;
	private int totalNum = 0;
	
	//file upload path
	private String uploadPath = "C:\\Java\\Workspace\\SpringBoard\\WebContent\\files\\";
	
	@RequestMapping("/list.do")
	public ModelAndView boardList(HttpServletRequest request, HttpServletResponse response) {
		String type = null;
		String keyword = null;
		
		//set variables from request parameter
		if(request.getParameter("page") == null || request.getParameter("page").trim().isEmpty() || request.getParameter("page").equals("0")) {
			currentPage =1;
		} else {
			currentPage = Integer.parseInt(request.getParameter("page"));
		}
		
		if(request.getParameter("type")!=null) {
			type = request.getParameter("type").trim();
		}
		if(request.getParameter("keyword")!=null) {
			keyword = request.getParameter("keyword").trim();
		}
		
		//expression board variables value
		startArticleNum = (currentPage-1) * showArticleLimit+1;
		endArticleNum = startArticleNum+ showArticleLimit -1;
		
		//get boardList and get page html code
		List<BoardModel> boardList;
		if(type!=null && keyword!=null) {
			boardList = boardService.searchBoard(type, keyword, startArticleNum, endArticleNum);
			totalNum = boardService.SearchTotalNum(type, keyword);
		} else {
			boardList = boardService.SelectAllBoard(startArticleNum, endArticleNum);
			totalNum = boardService.TotalNum();
		}
		
		StringBuffer pageHtml = getpageHtml(currentPage,totalNum,showArticleLimit,showPageLimit,type,keyword);
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("boardList",boardList);
		mv.addObject("pageHtml",pageHtml);
		mv.setViewName("/board/list");
		return mv;
	}

	private StringBuffer getpageHtml(int currentPage, int totalNum, int showArticleLimit, int showPageLimit,String type, String keyword) {
		StringBuffer pageHtml = new StringBuffer();
		int startPage = 0;
		int lastPage = 0;
		
		//expression page variables
		startPage = ((currentPage-1) / showPageLimit) * showPageLimit +1;
		lastPage = startPage + showPageLimit -1;
		
		if(lastPage > totalNum / showArticleLimit) {
			lastPage = (totalNum /showArticleLimit)+1;
		}
		
		//create page html code
		// if: when no search
		if(type == null && keyword == null) {
			if(currentPage == 1) {
				pageHtml.append("<span>");
			} else {
				pageHtml.append("<span><a href =\"list.do?page="+(currentPage-1)+"\"><이전></a>&nbsp;&nbsp;");
			}
			for(int i = startPage ; i<=lastPage; i++) {
				if(i == currentPage) {
					pageHtml.append(".&nbsp;<strong>");
					pageHtml.append("<a href =\"list.do?page="+i+"\" class=\"page\">"+i+"</a>");
					pageHtml.append("&nbsp;</strong>");
		
				} else {
					pageHtml.append(".&nbsp;<a href=\"list.do?page=" +i + "\" class=\"page\">" + i + "</a>&nbsp;");
				}
			}
			
			if(currentPage == lastPage){
				pageHtml.append(".</span>");
			} else {
				pageHtml.append(".&nbsp;&nbsp;<a href=\"list.do?page=" + (currentPage+1) + "\"><다음></a></span>");
			}
		//
		// else: when search
		} else {			
			if(currentPage == 1){
				pageHtml.append("<span>");
			} else {
				pageHtml.append("<span><a href=\"list.do?page=" + (currentPage-1) + "&type=" + type + "&keyword=" + keyword + "\"><이전></a>&nbsp;&nbsp;");
			}
			
			for(int i = startPage ; i <= lastPage ; i++) {
				if(i == currentPage){
					pageHtml.append(".&nbsp;<strong>");
					pageHtml.append("<a href=\"list.do?page=" +i + "&type=" + type + "&keyword=" + keyword + "\" class=\"page\">" + i + "</a>&nbsp;");
					pageHtml.append("&nbsp;</strong>");
				} else {
					pageHtml.append(".&nbsp;<a href=\"list.do?page=" +i + "&type=" + type + "&keyword=" + keyword + "\" class=\"page\">" + i + "</a>&nbsp;");
				}
				
			}
			if(currentPage == lastPage){
				pageHtml.append("</span>");
			} else {
				pageHtml.append(".&nbsp;&nbsp;<a href=\"list.do?page=" + (currentPage+1) + "&type=" + type + "&keyword=" + keyword + "\"><다음></a></span>");
			}
		}
		//		
		return pageHtml;
		}
	
	@RequestMapping("/view.do")
	public ModelAndView boardView(HttpServletRequest request) {
		int idx = Integer.parseInt(request.getParameter("idx"));
		BoardModel board = boardService.SelectOneBoard(idx); // get selected board model
		boardService.updateHitcount(board.getHitcount()+1, idx);
		//update hitcount
		List<BoardCommentModel> commentList = boardService.SelectAllComment(idx); // get comment List
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("board",board);
		mv.addObject("commentList",commentList);
		mv.setViewName("/board/view");
		return mv;
	}
	
	@RequestMapping("/write.do")
	public String boardWrite(@ModelAttribute("BoardModel") BoardModel boardModel) {
		return "/board/write";
	}
	
	@RequestMapping(value = "/write.do",method =RequestMethod.POST)
	public String boardWriteProc(@ModelAttribute("BoardModel") BoardModel boardModel,MultipartHttpServletRequest request) {
		MultipartFile file = request.getFile("file");
		String fileName = file.getOriginalFilename();
		File uploadFile = new File(uploadPath+fileName);
		System.out.println(uploadFile);
		//when file exists as same name
		if(uploadFile.exists()) {
			fileName = new Date().getTime() + fileName;
			uploadFile = new File(uploadFile + fileName);
		}
		
		//save upload file to uploadPath
		try {
			file.transferTo(uploadFile);
		} catch (Exception e) {

		}
		boardModel.setFileName(fileName);
		
		//new line code change to <br /> tag
		String content = boardModel.getContent().replaceAll("\r\n", "<br />");
		boardModel.setContent(content);
		boardService.writeBoard(boardModel);
		
		return "redirect:list.do";
	}
	
	@RequestMapping("/commentWrite.do")
	public ModelAndView commentWriteProc(@ModelAttribute("CommentModel") BoardCommentModel commentModel) {
		//new line code change to <br /> tag
		String content = commentModel.getContent().replaceAll("\r\n", "<br />");
		
		commentModel.setContent(content);
		
		boardService.writeComment(commentModel);
		ModelAndView mv = new ModelAndView();
		mv.addObject("idx",commentModel.getBoardidx());
		mv.setViewName("redirect:view.do");
		return mv;
	}
	
	@RequestMapping("/modify.do")
	public ModelAndView boardModify(HttpServletRequest request, HttpSession session) {
		String member_id = (String) session.getAttribute("member_id");
		int idx = Integer.parseInt(request.getParameter("idx"));
		BoardModel board = boardService.SelectOneBoard(idx);
		// <br /> tag change to new line code
		String content = board.getContent().replaceAll("<br />","\r\n");
		board.setContent(content);
		
		ModelAndView mv = new ModelAndView();
		
		if(!member_id.equals(board.getWriterId())) {
			mv.addObject("errCode","1"); //forbidden connection
			mv.addObject("idx",idx);
			mv.setViewName("redirect:view.do");
		} else {
			mv.addObject("board",board);
			mv.setViewName("/board/modify");
		}
		
		return mv;
	}
	
	@RequestMapping(value = "/modify.do",method = RequestMethod.POST)
	public ModelAndView boardModifyProc(@ModelAttribute("BoardModel") BoardModel boardModel, MultipartHttpServletRequest request) {
		String orgFileName = request.getParameter("orgFile");
		MultipartFile newFile = request.getFile("newFile");
		String newFileName = newFile.getOriginalFilename();
		boardModel.setFileName(orgFileName);
		
		//if: when want to change upload file
		if(newFile!=null && !newFileName.equals("")) {
			if(orgFileName!=null || !orgFileName.equals("")) {
				//remove upload file
				File removeFile = new File(uploadPath+orgFileName);
				removeFile.delete();
			}
			//create new upload file
			File newUploadFile = new File(uploadPath+newFileName);
			try {
				newFile.transferTo(newUploadFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			boardModel.setFileName(newFileName);
		}
		
		String content = boardModel.getContent().replaceAll("\r\n", "<br />");
		boardModel.setContent(content);
		
		boardService.modifyBoard(boardModel);
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("idx",boardModel.getIdx());
		mv.setViewName("redirect:/board/view.do");
		return mv;
	}
	
	@RequestMapping("/delete.do")
	public ModelAndView boardDelete(HttpServletRequest request, HttpSession session) {
		String member_id = (String) session.getAttribute("member_id");
		int idx = Integer.parseInt(request.getParameter("idx"));
		BoardModel board = boardService.SelectOneBoard(idx);
		
		ModelAndView mv = new ModelAndView();
		
		if(!member_id.equals(board.getWriterId())) {
			mv.addObject("errCode","1");
			mv.addObject("idx",idx);
			mv.setViewName("redirect:view.do");
		} else {
			List<BoardCommentModel> commentList = boardService.SelectAllComment(idx);
			if(commentList.size() >0) {
				mv.addObject("errCode","2");
				mv.addObject("idx",idx);
				mv.setViewName("redirect:view.do");
			} else {
				if(board.getFileName()!=null) {
					File removeFile = new File(uploadPath+board.getFileName());
					removeFile.delete();
				}
				boardService.deleteBoard(idx);
				mv.setViewName("redirect:list.do");
			}
		}
		return mv;
	}
	
	@RequestMapping("/commentDelete.do")
	public ModelAndView commentDelete(HttpServletRequest request, HttpSession session) {
		int idx = Integer.parseInt(request.getParameter("idx"));
		int boardidx = Integer.parseInt(request.getParameter("boardidx"));
		String member_id = (String) session.getAttribute("member_id");
		BoardCommentModel comment = boardService.SelectOneComment(idx);
		ModelAndView mv = new ModelAndView();
		
		if(!member_id.equals(comment.getWriterId())) {
			mv.addObject("errCode","1");
		} else {
			boardService.deleteComment(idx);
		}
		mv.addObject("idx",boardidx);
		mv.setViewName("redirect:view.do");
		return mv;
	}
	
	@RequestMapping("/recommend.do")
	public ModelAndView updateRecommendcount(HttpServletRequest request, HttpSession session) {
		int idx = Integer.parseInt(request.getParameter("idx"));
		String member_id = (String) session.getAttribute("member_id");
		BoardModel board = boardService.SelectOneBoard(idx);
		
		ModelAndView mv = new ModelAndView();
		
		if(member_id.equals(board.getWriterId())) {
			mv.addObject("errCode","1");
		} else {
			boardService.updateRecommendcount(board.getRecommendcount()+1, idx);
		}
		mv.addObject("idx",idx);
		mv.setViewName("redirect:/board/view.do");
		return mv;
	}
	
	@RequestMapping("/download")
	public ModelAndView download(@RequestParam("fileName") String FileName) throws Exception {
		String path = "C:\\Java\\Workspace\\SpringBoard\\WebContent\\files\\";
		File file = new File(path+FileName);
		return new ModelAndView("download","downloadFile",file);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.webcontext = (WebApplicationContext) applicationContext;
		
	}
	}
