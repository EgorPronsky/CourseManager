package com.company.manager.domain.archive;

public enum CourseResult {
    EXCELLENT,
    OK,
    BAD,

    NOT_GRADED;

    @Override
    public String toString() {
        switch(this) {
            case EXCELLENT: return "Excellent";
            case OK: return "OK";
            case BAD: return "Bad";
            case NOT_GRADED: return "Not graded yet";
            default: throw new IllegalArgumentException();
        }
    }
}
