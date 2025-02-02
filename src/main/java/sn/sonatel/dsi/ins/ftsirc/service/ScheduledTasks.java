package sn.sonatel.dsi.ins.ftsirc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private final OLTService oltService;
    private final ONTService ontService;

    public ScheduledTasks(OLTService oltService, ONTService ontService) {
        this.oltService = oltService;
        this.ontService = ontService;
    }
    //    @Scheduled(cron = " 0 33 16 * * *")
    ////    public void updateOLT() {
    ////        log.debug("Started OLT UPDATE CRON");
    ////        oltService.updateOLT("/Users/nfl/Documents/Sonatel/SAYTU/OLT_OSN_22.04.24 (1).xlsx");
    ////    }

    //    @Scheduled(cron = " 0  16 * * *")
    //    public void updateAllONT() {
    //        log.debug("Started ONT UPDATE CRON");
    //        ontService.updateALLONTS();
    //    }
}
