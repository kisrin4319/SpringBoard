package interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


public class SessionInterceptor extends HandlerInterceptorAdapter{
	
	//controller에 요청이 가기전에 메서드 실행
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// check variable
		Object member_id = request.getSession().getAttribute("member_id");
		//
		
		// pass through when access login.do, join.do
		if(request.getRequestURI().equals("/SpringBoard/login.do") || request.getRequestURI().equals("/SpringBoard/member/join.do")){
			if(member_id != null){
				response.sendRedirect(request.getContextPath() + "/board/list.do");
				return true;
			} else {
				return true;
			}
		}
		//
		// where other pages		
		if(member_id == null){
			response.sendRedirect(request.getContextPath() + "/login.do");
			return false;
		} else {
			return true;
		}
		//		
	}
	
	//controller 요청 후에 동작
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}
}

