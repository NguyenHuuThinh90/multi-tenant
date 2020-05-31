package com.app.config.security;

import com.app.entity.AccountModel;
import com.app.entity.RoleModel;
import com.app.service.FlexibleSearchService;
import com.app.service.MessageService;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Optional;

@Service
public class DefaultUserDetailService implements UserDetailsService {

    @Resource
    private FlexibleSearchService flexibleSearchService;
    @Resource
    private MessageService messageService;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<AccountModel> accountModel = flexibleSearchService
                .create(AccountModel.class)
                .select("username", "password")
                .and("username", username)
                .getSingleResult();

        if (!accountModel.isPresent()) {
            throw new UsernameNotFoundException(messageService.get("account.error.not.found", username));
        }

        AccountModel account = accountModel.get();
        DefaultUserDetail userDetail = new DefaultUserDetail(username, passwordEncoder.encode(account.getPassword()), true, Collections.emptyList());
        userDetail.setId(account.getId());

        return userDetail;
    }
}
