package com.company.manager.util;

import java.util.List;

public class TestUtil {

    public static boolean areListsEqualIgnoringOrder(List<?> l1, List<?> l2) {
        return l1.size() == l2.size() && l1.containsAll(l2) && l2.containsAll(l1);
    }
}
