package com.telus.credit.crda.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.crda.util.CrdaUtility;
import com.telus.credit.crda.util.LogUtil;
import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.framework.persistence.AbstractSqlMapDao;

public class EcrdaAbstractSqlMapDao extends AbstractSqlMapDao {
    public static final Log log = LogFactory.getLog(EcrdaAbstractSqlMapDao.class);

    public HashMap<String, Object> queryForObject(String aStmt, long aID) throws ObjectNotFoundException {

        LogUtil.traceCalllog("aStmt=" + aStmt + "  , ID =" + aID);
        long startTime = System.nanoTime();
        HashMap<String, Object> rslt = (HashMap<String, Object>) super.queryForObject(aStmt, aID);
        LogUtil.infolog("IGNORE : Total execution time to call " + aStmt + "  in millis: " + (System.nanoTime() - startTime) / 1000000);
        return rslt;
    }


    public void update(String aStmt, Map<String, Object> commentsMap)
            throws ConcurrencyConflictException {
        CrdaUtility.printIMapData(
                aStmt,
                commentsMap);
        long startTime = System.nanoTime();


        super.update(aStmt, commentsMap);

        LogUtil.infolog("IGNORE : Total execution time to call " + aStmt + "  in millis: " + (System.nanoTime() - startTime) / 1000000);

    }


    public Long insert(String aStmt, HashMap<String, Object> aData)
            throws DuplicateKeyException {
        CrdaUtility.printIMapData(
                aStmt,
                aData);
        long startTime = System.nanoTime();

        Long id = (Long) super.insert(aStmt, aData);

        LogUtil.infolog("IGNORE : Total execution time to call " + aStmt + "  in millis: " + (System.nanoTime() - startTime) / 1000000);
        return id;
    }

    public List<HashMap<String, Object>> queryForList(String aStmt, Map<String, Object> aMap) {
        CrdaUtility.printIMapData(
                aStmt,
                aMap);
        long startTime = System.nanoTime();
        List<HashMap<String, Object>> rsltList = super.queryForList(aStmt, aMap);

        LogUtil.infolog("IGNORE : Total execution time to call " + aStmt + "  in millis: " + (System.nanoTime() - startTime) / 1000000);
        return rsltList;
    }

    public List queryForList(String aStmt, Object aID) {
    	 
        LogUtil.traceCalllog("query stmt =" + aStmt + "ID = " + aID);
        long startTime = System.nanoTime();
        List<HashMap<String, Object>> rsltList = super.queryForList(aStmt, aID);

        LogUtil.infolog("IGNORE : Total execution time to call " + aStmt + "  in millis: " + (System.nanoTime() - startTime) / 1000000);
        return rsltList;

    }
}
