/*
    CreditGateway Activity Report By Agency
*/
set echo off
set verify off
set termout on
set heading off
set pages 0
set feedback off
set newpage none
set linesize 160
set trimspool on

whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

col spoolname new_value spoolname;
SELECT '&1' || TO_CHAR(SYSDATE-1, '_YYYYMMDD') || '.csv' spoolname from dual;
spool '&spoolname';

-- rtca_crd_bureau_activity_report
SELECT 'Agency,#Total Requests,#Successful Requests,#Unsuccessful Requests,#Failures(Bureau Down and Critical Error)' FROM dual;
SELECT
    CREDIT_REPORT_SOURCE_SYS_CD           ||','|| -- Agency name
    COUNT(CREDIT_REPORT_TRNS_LOG_ID)      ||','|| -- #Total requests
    /*
    SUM(CASE -- #Total requests
                WHEN 
                    (CREDIT_REPORT_TRNS_LOG_ID IS NOT NULL
                    -- Do not count requests that are failovered to Simulator since the critical errors
                    -- Such requests are counted in the Simulator record
                    -- But if failover = false???
                    AND TRANSACTION_ERROR_CD NOT IN ('30009','10002','10003','10004','10006','10007','20002','30001')
                    )
                THEN 1
                ELSE 0
            END
       )  ||','||
    */   
    SUM(CASE -- #Successful requests
                WHEN 
                    TRANSACTION_ERROR_CD = '0'
                THEN 1
                ELSE 0
            END
       )  ||','||
    SUM(CASE -- #Unsuccessful requests by agency: processing errors
                    /* Note in RTCA:
                        1. if processing failed by TU, Equifax will return error code
                        2. if processing failed by FICO, the error code is set with Equifax error code since Equifax is primary agency
                        3. Telus errors, e.g. EV301, EV306, .. are not included (since it is TELUS error)
                        4. so processing failed by agency are all related to Equifax codes.
                    */
                WHEN 
                    TRANSACTION_ERROR_CD IN (
                        'E0822','E0823','E0825','E0828','E1619','E0819','E0820','E0821',
                        'E0824','EF00A','EP0AA','EF02A','EF02B','EF02C','EF02F','EV1F1',
                        'EV1F2','EV1F3','EV1F4','EV101','EV102','EV103','EV104','EV107',
                        'EV2F1','EV2F2','EV2F3','EV2F4','EV3F1','EV3F2','EV3F3','EV3F4',
                        'EV303','EV4F1','EV4F2','EV4F3','EV4F4','EV404','EV5F1','EV5F2',
                        'EV5F3','EV5F4','EV503','EF021','EF022','EF023','EF025','EF026',
                        'EF027','EF028','EF029','EP0A0','EP0A1','EP0A2','EP0A3','EP0A9',
                        'EP0BB','EP0BD','EP0BE','EP0BF','EP0B7','EP0B9','EP0C6','EP00A',                
                        'EP00B','EP003','EP004','EP005','EP006','EP008','EP009','EP01A',                
                        'EP01B','EP01C','EP01D','EP01E','EP014','EP015','EP02A','EP02B',
                        'EP02C','EP02D','EP02E','EP021','EP023','EP024','EP025','EP026',
                        'EP027','EP028','EP031','EP04A','EP04D','EP044','EP045','EP046',
                        'EP047','EP048','EP049','EP057','EP06B','EP06E','EP065','EP067',
                        'EP07B','EP07E','EP070','EP075','EP076','EP077','EP078','EP082',
                        'EP084','EP086','EP087','EP09A','EP09B','EP09C','EP09D','EP09E',
                        'EP09F','EP091','EP092','EP095','EP096','EP097','EP098','EP099',                
                        'E1601','E1602','E1603','E1604','E1605','EPKI1','EPKI2','EPKI3',
                        'EPKI4','EPKI5','EPKI6','EV812','E0801','E0808','E0809','E0813',
                        'E0826','E0827','E0831','E1201','E1202','E1601','E1602','E1603',
                        'E1604','E1605','E1606','E1607','E1608','E1609','E1610','E1611',
                        'E1612','E1613','E1614','E1615','E1616','E1618','E1630','E1633',
                        'E1634','E1635','EP0DB','*****'
                        )
                THEN 1
                ELSE 0
            END
       )   ||','||  
    SUM(CASE -- #Failures due to Credit Bureau Critical Errors:
                WHEN 
                    TRANSACTION_ERROR_CD IN ('30009','10002','10003','10004','10006','10007','20002','30001')
                THEN 1
                ELSE 0
            END
       )                                               
    FROM CREDIT_REPORT_TRNS_LOG
    WHERE EXTERNAL_VENDOR_NM = 'EQFXIC'
    --AND TO_CHAR(CREATE_TS , 'WW') = TO_CHAR(SYSDATE, 'WW')
    -- Run batch job at early of Monday morning, e.g. 12:00:01 AM
    -- CREATE_TS is of timestamp, e.g. 2013/01/07 12:00:01 AM > 2013/01/07 and 2013/01/13 11:59:59 PM< 2013/01/14
    AND CREATE_TS BETWEEN TRUNC(SYSDATE-7,'DD') and TRUNC(SYSDATE, 'DD')
    GROUP BY CREDIT_REPORT_SOURCE_SYS_CD
;
spool off;
exit;
