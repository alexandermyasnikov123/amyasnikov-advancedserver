package net.dunice.newsapi.services.impls;

import net.dunice.newsapi.services.JwtService;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {
    @Override
    public String getCurrentToken() {
        return "here will be my implementation";
    }
}
