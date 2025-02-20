package com.example.good_lodging_service.security;

import com.example.good_lodging_service.constants.ConstantsAll;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUserIdLogin().orElse(ConstantsAll.SYSTEM_ID));
    }
}
