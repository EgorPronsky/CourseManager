package com.company.manager.domain.archive;

public enum CourseResult {
    EXCELLENT,
    OK,
    BAD;

    @Override
    public String toString() {
        switch(this) {
            case EXCELLENT: return "Excellent";
            case OK: return "OK";
            case BAD: return "Bad";
            default: throw new IllegalArgumentException();
        }
    }
}
