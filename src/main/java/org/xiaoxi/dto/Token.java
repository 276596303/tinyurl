package org.xiaoxi.dto;

/**
 * Created by YanYang on 2016/6/23.
 */
public class Token {
    private String username;
    private String token;

    public Token(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token1 = (Token) o;

        if (username != null ? !username.equals(token1.username) : token1.username != null) return false;
        return !(token != null ? !token.equals(token1.token) : token1.token != null);

    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Token{" +
                "username='" + username + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
