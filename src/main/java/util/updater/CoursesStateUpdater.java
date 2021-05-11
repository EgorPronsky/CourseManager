package util.updater;

import domain.course.Course;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Collectors;

@Slf4j
public class CoursesStateUpdater extends TimerTask {

    @Override
    public void run() {
        log.info("Start finding finished courses");

        //HibernateCourseService courseService = HibernateCourseService.getInstance();
       // List<Course> courses = courseService.getAll();

        Date today = new Date();

        // Getting finished courses
        //List<Course> finishedCourses = courses.parallelStream()
                //.filter(course -> today.after(course.getCourseInfo().getEndDate()))
                //.collect(Collectors.toList());

        // Notify teachers to grade students
        // ...

    }

}
