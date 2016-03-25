package carservice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
public class Client {
    @Id
    @Column(name = "car_id")
    private String carId;

    @Column(name = "queue_start_date")
    private Date queueStartDate;

    private Boolean busy;
//    private List<Service> services;


    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public Date getQueueStartDate() {
        return queueStartDate;
    }

    public void setQueueStartDate(Date queueStartDate) {
        this.queueStartDate = queueStartDate;
    }

    public Boolean getBusy() {
        return busy;
    }

    public void setBusy(Boolean busy) {
        this.busy = busy;
    }
}