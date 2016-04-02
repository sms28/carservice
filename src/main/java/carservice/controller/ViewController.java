package carservice.controller;

import carservice.dao.WorkshopMasterDAO;
import carservice.domain.*;
import carservice.service.StatisticsService;
import carservice.service.TaskExecutorExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Класс ответственный за отправку данных в модуль отображения данных
 */
@Controller
@RequestMapping("/")
public class ViewController {

    @Autowired
    public WorkshopMasterDAO workshopMasterDAO;

    @Autowired
    public TaskExecutorExample taskExecutorExample;

    @Autowired
    public StatisticsService statisticsService;

    @RequestMapping(method = RequestMethod.GET, value = "/getCurrentState", produces = "application/json")
    @ResponseBody
    public CarService getCurrentState() {
        Statistics statistics = statisticsService.getStatistics();
        CarService carService = new CarService();
        carService.setWorkshops(workshopMasterDAO.getWorkshopList());
        carService.setStatistics(statistics);
        return carService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/startQueueThread")
    @ResponseBody
    public String startQueueThread() {
        taskExecutorExample.startGeneratingTickets();
        return "";
    }

}
