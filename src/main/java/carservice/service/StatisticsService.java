package carservice.service;

import carservice.dao.IncomeTicketDAO;
import carservice.domain.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {
    @Autowired
    private IncomeTicketDAO incomeTicketDAO;

    public Statistics getStatistics() {
        Statistics statistics = new Statistics();

        statistics.setProfit(incomeTicketDAO.getServicesSumCost().intValue());

        statistics.setAverageTime(incomeTicketDAO.getAverageQueueAndProcessingTime());

        statistics.setServedCarCount(incomeTicketDAO.getServedCarCount());

        statistics.setServicesNumber(incomeTicketDAO.getServicesNumber());

        statistics.setMastersIncome(incomeTicketDAO.getMastersIncome());

        return statistics;
    }


}
