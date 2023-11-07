package com.kma.cdhttt.ulti;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {
    public static boolean isPasswordValid(String password) {
        // Kiểm tra xem mật khẩu có ít nhất 8 ký tự
        if (password.length() < 8) {
            return false;
        }

        // Kiểm tra xem mật khẩu có ít nhất một chữ hoa
        Pattern uppercasePattern = Pattern.compile("[A-Z]");
        Matcher uppercaseMatcher = uppercasePattern.matcher(password);
        if (!uppercaseMatcher.find()) {
            return false;
        }

        // Kiểm tra xem mật khẩu có ít nhất một chữ thường
        Pattern lowercasePattern = Pattern.compile("[a-z]");
        Matcher lowercaseMatcher = lowercasePattern.matcher(password);
        if (!lowercaseMatcher.find()) {
            return false;
        }

        // Kiểm tra xem mật khẩu có ít nhất một số
        Pattern digitPattern = Pattern.compile("[0-9]");
        Matcher digitMatcher = digitPattern.matcher(password);
        if (!digitMatcher.find()) {
            return false;
        }

        // Nếu mật khẩu vượt qua tất cả các kiểm tra, trả về true
        return true;
    }

}
