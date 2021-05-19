package com.company.manager.services.impl;

import com.company.manager.dao.custom.AccessInfoDAO;
import com.company.manager.dao.custom.impl.HibernateAccessInfoDAO;
import com.company.manager.domain.user.info.AccessInfo;
import com.company.manager.services.AccessInfoService;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class AccessInfoServiceImpl implements AccessInfoService {

    private static final AccessInfoServiceImpl service;
    private final AccessInfoDAO dao;

    private AccessInfoServiceImpl(){
        log.info("Initializing {}", AccessInfoServiceImpl.class.getName());
        dao = new HibernateAccessInfoDAO();
        log.info("{} was initialized", AccessInfoServiceImpl.class.getName());
    }

    static {
        service = new AccessInfoServiceImpl();
    }

    public static AccessInfoServiceImpl getService() {
        return service;
    }

    @Override
    public boolean isEmailExists(String email) {
        return findAccessInfoByEmail(email).isPresent();
    }

    private Optional<AccessInfo> findAccessInfoByEmail(String email) {
        return dao.findAccessInfoByEmail(email);
    }

}
