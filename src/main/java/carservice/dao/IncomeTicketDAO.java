package carservice.dao;

import carservice.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class IncomeTicketDAO {


    @PersistenceContext
    private EntityManager entityManager;


    public void insertIncomeTicket(int workshopId, IncomeTicket incomeTicket) {
        Query query = entityManager.createQuery("select w from Workshop w where w.id = :workshopId");
        query.setParameter("workshopId", workshopId);
        Workshop workshop = (Workshop) query.getSingleResult();
        List<IncomeTicket> queue = workshop.getQueue();
        queue.add(incomeTicket);
        workshop.setQueue(queue);
    }


    public void setTicketStatus(int ticketId, String status) {
        Query query = entityManager.createQuery("update IncomeTicket t set t.status = :status where t.id = :id");
        query.setParameter("id", ticketId);
        query.setParameter("status", status);
        query.executeUpdate();
    }

    public void setTicketMaster(int ticketId, Master master) {
        Query query = entityManager.createQuery("update IncomeTicket t set t.master = :master where t.id = :id");
        query.setParameter("id", ticketId);
        query.setParameter("master", master);
        query.executeUpdate();
    }

    public int getTicketsInQueueCount() {
        Query query = entityManager.createQuery("select count(t) from IncomeTicket t where t.status = 'InQueue'");
        long result = (Long) query.getSingleResult();
        return (int) result;
    }

    public IncomeTicket getFirstTicketInQueue() {
        String queryText = "select top(1) from IncomeTicket t where t.status = 'InQueue' order by t.addQueueDate";
        return (IncomeTicket) entityManager.createQuery(queryText).getSingleResult();
    }


}