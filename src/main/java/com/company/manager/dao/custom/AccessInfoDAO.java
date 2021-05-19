package com.company.manager.dao.custom;

import com.company.manager.dao.generic.GenericDAO;
import com.company.manager.domain.user.AccessInfo;

import java.util.Optional;

public interface AccessInfoDAO extends GenericDAO<AccessInfo> {

    Optional<AccessInfo> findAccessInfoByEmail(String email);

}
