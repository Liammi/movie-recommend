package me.quinn.movie.exception;

public class LoginException extends RuntimeException{
    public LoginException(){
        super();
    }
    public LoginException(String msg){
        super(msg);
    }
}
