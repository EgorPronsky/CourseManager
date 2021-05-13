package util.converters;

import domain.archive.CourseResult;
import domain.archive.StudentCourseResult;
import domain.course.Course;
import domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentCourseResultMapService {

    public static List<StudentCourseResult> convertStudentsResultsToDTO(Course course,
                                                                                  Map<User, CourseResult> studentsResults) {
        List<StudentCourseResult> resultList = new ArrayList<>();

        for (Map.Entry<User, CourseResult> studentAndResult : studentsResults.entrySet()) {
            resultList.add(StudentCourseResult.builder()
                    .course(course)
                    .student(studentAndResult.getKey())
                    .result(studentAndResult.getValue())
                    .build());
        }
        return resultList;
    }

}
