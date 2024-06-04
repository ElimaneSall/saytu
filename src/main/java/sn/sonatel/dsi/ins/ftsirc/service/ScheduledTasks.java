package sn.sonatel.dsi.ins.ftsirc.service;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private final OLTService oltService;
    private final ONTService ontService;
    private final DiagnosticService diagnosticService;

    public ScheduledTasks(OLTService oltService, ONTService ontService, DiagnosticService diagnosticService) {
        this.oltService = oltService;
        this.ontService = ontService;
        this.diagnosticService = diagnosticService;
    }

    //    @Scheduled(cron = " 0 33 16 * * *")
    ////    public void updateOLT() {
    ////        log.debug("Started OLT UPDATE CRON");
    ////        oltService.updateOLT("/Users/nfl/Documents/Sonatel/SAYTU/OLT_OSN_22.04.24 (1).xlsx");
    ////    }

    @Scheduled(cron = " 0 30 22 * * *")
    public void updateOLT() {
        log.debug("Started OLT UPDATE CRON");
        oltService.updateOLT("C:\\Users\\Surface\\Desktop\\Sonatel_2023\\saytou\\saytu-project\\saytu-backend\\OLT_OSN_22.04.24.xlsx");
    }

    @Scheduled(cron = " 0 20 22 * * *")
    public void updateAllONT() {
        log.debug("Started ONT UPDATE CRON");
        ontService.updateALLONTS();
    }

    @Scheduled(cron = " 0 08 22 * * *")
    public void diagnosticFiberAutomatique() throws IOException {
        log.debug("Started Maintenance Predictive");
        diagnosticService.diagnosticFiberAutomatique();
    }
}
