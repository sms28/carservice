package carservice.service;

import carservice.dao.IncomeTicketDAO;
import carservice.dao.MasterDAO;
import carservice.dao.SystemStateDAO;
import carservice.domain.IncomeTicket;
import carservice.domain.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.util.Calendar;
import java.util.Random;

@Service
@Scope("prototype")
public class MasterWorking extends Thread {

    private IncomeTicket incomeTicket;

    @Autowired
    private SystemTimer systemTimer;

    @Autowired
    private IncomeTicketDAO incomeTicketDAO;
    @Autowired
    private MasterDAO masterDAO;
    @Autowired
    private SystemStateDAO systemStateDAO;

    @Autowired
    private ServletContext servletContext;

    private static int runTimeDeflectionPercents;

    public static void setRunTimeDeflectionPercents(int deflection) {
        runTimeDeflectionPercents = deflection;
    }

    public void init(IncomeTicket incomeTicket) {
        this.incomeTicket = incomeTicket;
    }

    public void run() {
        try {
            sleep(getServiceRunTime());
            if (systemStateDAO.getSystemState().getStatus() == Status.RUNNING) {
                finishProcessService();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int getServiceRunTime() {
        int averageTimeMinutes = incomeTicket.getService().getAverageTime();
        int serviceRunTime = (int) (averageTimeMinutes * generateRandomRatio());
        return systemTimer.minutesToMilliSeconds(systemTimer.convertWorkTime(serviceRunTime)) / SystemTimer.TIME_SCALE;
    }

    private double generateRandomRatio() {
        double randomDeflection = new Random().nextInt(2 * runTimeDeflectionPercents + 1) - runTimeDeflectionPercents;
        return 1 + randomDeflection / 100;
    }

    @Transactional
    private void finishProcessService() {
        Calendar now = systemTimer.getCurrentDateTime();
        incomeTicketDAO.setTicketStatus(incomeTicket.getId(), "Complete");
        incomeTicketDAO.setTicketFinishDate(incomeTicket.getId(), now);
        IncomeTicket firstTicket = incomeTicketDAO.getFirstTicketInQueue(incomeTicket.getMaster());
        if (firstTicket == null) {
            masterDAO.setMasterBusy(incomeTicket.getMaster().getId(), false);
        } else {
            incomeTicketDAO.setTicketMaster(firstTicket.getId(), incomeTicket.getMaster());
            incomeTicketDAO.setTicketStatus(firstTicket.getId(), "InProcess");
            incomeTicketDAO.setTicketStartProcessDate(firstTicket.getId(), now);
            firstTicket.setMaster(incomeTicket.getMaster());
            firstTicket.setStatus("InProcess");
            firstTicket.setStartProcessDate(now);

            WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            MasterWorking masterWorking = (MasterWorking) context.getBean("masterWorking");
            masterWorking.init(firstTicket);
            masterWorking.start();

        }
    }


}
