package util.updater;

import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;

@Slf4j
public class DBEntitiesUpdater extends TimerTask {

    @Override
    public void run() {
        log.info("Loading new data into database from containers");

        //CoursesContainer.getInstance().updateToDB();

    }
}
