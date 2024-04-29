package sn.sonatel.dsi.ins.ftsirc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private final OLTService oltService;

    public ScheduledTasks(OLTService oltService) {
        this.oltService = oltService;
    }

    @Scheduled(cron = " 0 8 * * *")
    public void updateOLT() {
        log.debug("Started OLT UPDATE CRON");
        oltService.updateOLT("/Users/nfl/Documents/Sonatel/SAYTU/OLT_OSN_22.04.24.xlsx");
    }
}
