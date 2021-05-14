package services.impl;

import dao.custom.AccessInfoDAO;
import dao.custom.impl.HibernateAccessInfoDAO;
import dao.custom.impl.HibernateCourseDAO;
import domain.user.AccessInfo;
import lombok.extern.slf4j.Slf4j;
import services.AccessInfoService;

import java.util.Optional;

@Slf4j
public class AccessInfoServiceImpl implements AccessInfoService {

    private final AccessInfoDAO dao;

    private AccessInfoServiceImpl(){
        log.info("Initializing {}", AccessInfoServiceImpl.class.getName());
        dao = new HibernateAccessInfoDAO();
        log.info("{} was initialized", AccessInfoServiceImpl.class.getName());
    }

    // Init on-demand
    private static class ServiceHolder {
        static final AccessInfoServiceImpl service = new AccessInfoServiceImpl();
    }

    public static AccessInfoServiceImpl getService() {
        return ServiceHolder.service;
    }

    @Override
    public boolean isEmailExists(String email) {
        return findAccessInfoByEmail(email).isPresent();
    }

    private Optional<AccessInfo> findAccessInfoByEmail(String email) {
        return dao.findAccessInfoByEmail(email);
    }

}