package listeners;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Slf4j
public class ApplicationStartUpListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        /*log.info("Fetching data from db for entity containers");
        CoursesContainer coursesContainer = CoursesContainer.getInstance();
        coursesContainer.updateFromDB();

        log.info("Starting timer (per day) task for detecting finished courses");
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_WEEK, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new CoursesStateUpdater(), calendar.getTime(), TimeUnit.DAYS.toMillis(1));

        log.info("Starting timer task for loading data into database");
        timer.scheduleAtFixedRate(new DBEntitiesUpdater(), TimeUnit.MINUTES.toMillis(1), TimeUnit.MINUTES.toMillis(1));*/

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
