package service;

import dao.custom.AccessInfoDAO;
import dao.custom.impl.HibernateAccessInfoDAO;
import domain.user.AccessInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class AccessInfoService {

    private final AccessInfoDAO dao;

    private AccessInfoService(){
        dao = new HibernateAccessInfoDAO();
    }

    // Lazy init
    private static class ServiceHolder {
        static final AccessInfoService service = new AccessInfoService();
    }

    public static AccessInfoService getService() {
        return ServiceHolder.service;
    }

    public boolean isEmailExists(String email) {
        return findAccessInfoByEmail(email).isPresent();
    }

    public Optional<AccessInfo> findAccessInfoByEmail(String email) {
        return dao.findAccessInfoByEmail(email);
    }

    public Optional<AccessInfo> findAccessInfoByEmailAndPassword(String email, String password) {
        int passwordHash = password.hashCode();
        return dao.findAccessInfoByEmailAndPasswordHash(email, passwordHash);
    }

}
