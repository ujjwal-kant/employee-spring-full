package com.increff.pos.dao;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.DailyReportSalesPojo;
import com.increff.pos.service.ApiException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import java.util.List;


@Repository
@Transactional(rollbackOn  = ApiException.class)
public class DailySalesReportDao extends AbstractDao {

    private static final String select_all = "select p from DailyReportSalesPojo p";
    private static final String SELECT_LAST_DAY = "select p from DailyReportSalesPojo p order by Id DESC";

    @PersistenceContext
	private EntityManager em;

    @Transactional
    public void insert(DailyReportSalesPojo salesReportPojo) {
        em.persist(salesReportPojo);
    }

    @Transactional
    public List<DailyReportSalesPojo> selectAll() {
        TypedQuery<DailyReportSalesPojo> query = getQuery(select_all, DailyReportSalesPojo.class);
        return query.getResultList();
    }

    public DailyReportSalesPojo getLastDay() {
        TypedQuery<DailyReportSalesPojo> query = getQuery(SELECT_LAST_DAY,DailyReportSalesPojo.class);
        query.setMaxResults(1);
        return getSingle(query);
    }
}