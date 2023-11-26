package com.kma.cdhttt.controller;

import com.kma.cdhttt.entity.UserEntity;
import com.kma.cdhttt.model.ErrorResponse;
import com.kma.cdhttt.model.requestbody.JwtLoginRequestBody;
import com.kma.cdhttt.model.responsebody.JwtLoginResponseBody;
import com.kma.cdhttt.repository.UserRepository;
import com.kma.cdhttt.service.impl.JwtUserDetailsService;
import com.kma.cdhttt.ulti.Common;
import com.kma.cdhttt.ulti.ErrorCode;
import com.kma.cdhttt.ulti.JwtTokenUtil;
import com.kma.cdhttt.ulti.KMAException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Objects;

@RestController
@CrossOrigin("*")
@Slf4j
@RequestMapping("/kma/v1/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userService;
    private final UserRepository userRepository;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, JwtUserDetailsService userService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody @Valid JwtLoginRequestBody authenticationRequest) throws KMAException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            UserEntity user = userRepository.findAllByUserName(authenticationRequest.getUsername());
            if (Integer.parseInt(user.getOtpCount()) >= 5 && isDifferenceGreaterThan30Minutes(user.getUpdateDate(), new Timestamp(System.currentTimeMillis()))) {
                ErrorResponse response = new ErrorResponse();
                response.setMessage("bạn đã đăng nhập quá 5 lần, hãy thử lại sau 30 phút");
                response.setErrorCode(ErrorCode.ACCESS_DENIED);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if (Integer.parseInt(user.getOtpCount()) >= 5 && !isDifferenceGreaterThan30Minutes(user.getUpdateDate(), new Timestamp(System.currentTimeMillis()))) {
                user.setOtpCount("0");
                userRepository.save(user);
            }
            user.setOtpCount("0");
            user.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
            final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());

            final String token = jwtTokenUtil.generateToken(userDetails);

            return ResponseEntity.ok(new JwtLoginResponseBody(token, Common.SUCCESS));
        } catch (BadCredentialsException e) {
            UserEntity user = userRepository.findAllByUserName(authenticationRequest.getUsername());
            if(Objects.isNull(user)){
                throw new KMAException(ErrorCode.USER_NOT_EXITS, "USER_NOT_EXITS");
            }
            if (Integer.parseInt(user.getOtpCount()) >= 5) {
                ErrorResponse response = new ErrorResponse();
                response.setMessage("bạn đã đăng nhập quá 5 lần, hãy thử lại sau 30 phút");
                response.setErrorCode(ErrorCode.ACCESS_DENIED);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            user.setOtpCount(String.valueOf(Integer.parseInt(user.getOtpCount()) + 1));
            user.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
            ErrorResponse response = new ErrorResponse();
            response.setMessage("mật khẩu sai hay thử lại");
            response.setErrorCode(ErrorCode.ACCESS_DENIED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    private boolean isDifferenceGreaterThan30Minutes(Timestamp timestamp1, Timestamp timestamp2) {
        // Lấy khoảng thời gian giữa hai Timestamp
        long differenceInMillis = timestamp2.getTime() - timestamp1.getTime();

        // Chuyển đổi khoảng thời gian từ milliseconds sang phút
        long differenceInMinutes = differenceInMillis / (60 * 1000);

        // Kiểm tra xem khoảng thời gian có nhỏ 30 phút hay không
        return differenceInMinutes < 30;
    }
}
