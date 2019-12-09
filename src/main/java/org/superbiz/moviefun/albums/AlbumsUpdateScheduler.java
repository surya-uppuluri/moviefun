package org.superbiz.moviefun.albums;

import org.apache.tomcat.jni.Local;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.Date;

///home/pal-8/workspace/movie-fun/albums.csv
@Configuration
@EnableAsync
@EnableScheduling
public class AlbumsUpdateScheduler {

    private static final long SECONDS = 1000;
    private static final long MINUTES = 60 * SECONDS;

    private final AlbumsUpdater albumsUpdater;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public AlbumsUpdateScheduler(AlbumsUpdater albumsUpdater) {
        this.albumsUpdater = albumsUpdater;
    }


    @Scheduled(initialDelay = 15 * SECONDS, fixedRate = 2 * MINUTES)
    public void run() {
        try {
            Boolean isUpdatedRecently = albumsUpdater.checkIfDone();
            if (isUpdatedRecently) {
                System.out.println("isUpdatedRecently? " + isUpdatedRecently);
            } else {

                logger.debug("Starting albums update");


                albumsUpdater.update();

                albumsUpdater.updateBlob();

                logger.debug("Finished albums update");
            }

        } catch (Throwable e) {
            logger.error("Error while updating albums", e);
            e.printStackTrace();
        }
    }
}
