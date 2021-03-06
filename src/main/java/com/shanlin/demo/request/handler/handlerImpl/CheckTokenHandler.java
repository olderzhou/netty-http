package com.shanlin.demo.request.handler.handlerImpl;

import com.shanlin.demo.constans.ErrorCode;
import com.shanlin.demo.database.entry.User;
import com.shanlin.demo.database.repository.UserRepository;
import com.shanlin.demo.exceptions.ResponseException;
import com.shanlin.demo.request.entry.RequestBaseEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2016/6/10.
 */
@Component
public abstract class CheckTokenHandler<T extends RequestBaseEntry> extends AbstractHandler<T> {


    @Value("${token.timeout}")
    private long timeOut;
    @Autowired
    protected UserRepository userRepository;
    protected User user;

    @Override
    public boolean checkRequest(RequestBaseEntry entry) throws ResponseException {
        if ( super.checkRequest(entry)){
            user = userRepository.findByUsername(entry.getHead().getUsername());
            if(!user.getToken().equals(entry.getHead().getToken())){
                throw new ResponseException(ErrorCode.TOKEN_ERROR);
            }
            long time = System.currentTimeMillis() - user.getCreateTokenTime().getTime();
            if (time >  timeOut * 60 * 1000){
                throw new ResponseException(ErrorCode.TOKEN_TIME_OUT);
            }
        }
        return  true;
    }
}
