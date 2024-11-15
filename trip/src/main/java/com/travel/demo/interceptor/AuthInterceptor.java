package com.travel.demo.interceptor;

import com.travel.demo.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    private final JWTUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        //단순 조회 요청과 preflight 요청인 경우, true 로 넘김
        String method = request.getMethod();
        log.trace("AuthInterceptor()의 preHandle실행 method:{}", method);
        if(method.equals("GET") || method.equals("OPTIONS")) return true;

        String tokenHeader = request.getHeader("Authorization");	//Header에서 토큰 정보 추출
        log.trace("Authorization헤더 : {}", tokenHeader);
        //토큰 헤더가 없거나 Bearer로 시작하지 않거나 유효하지 않은 토큰인 경우
        if(tokenHeader == null || !tokenHeader.startsWith("Bearer ") || !jwtUtil.isValid(tokenHeader.substring(7))) {
            response.setStatus(401);
            response.getWriter().write("Unauthorized");
            return false;
        }
        //컨트롤러에서 쉽게 활용할 수 있도록 request에 memberID를 담아두자
        String memberId = jwtUtil.getIdFromToken(tokenHeader.substring(7));
        request.setAttribute("memberId", memberId);
        //토큰이 유효한 경우
        return true;
    }
}
