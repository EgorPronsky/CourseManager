package dao.custom;

import dao.generic.GenericDAO;
import domain.user.AccessInfo;

import java.util.Optional;

public interface AccessInfoDAO extends GenericDAO<AccessInfo> {

    Optional<AccessInfo> findAccessInfoByEmail(String email);

}
