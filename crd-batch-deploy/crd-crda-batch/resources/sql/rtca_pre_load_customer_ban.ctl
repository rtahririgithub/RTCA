load data
append

into table STG_CREDIT_UPDOWN_CUST_BAN 

fields terminated by '|'
trailing nullcols
(
  CUSTOMER_ID,
  BAN
)
