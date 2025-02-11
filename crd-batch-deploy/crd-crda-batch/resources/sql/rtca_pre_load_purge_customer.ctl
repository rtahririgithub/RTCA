load data
append

into table temp_rtca_purge_customers 

fields terminated by '|'
trailing nullcols
(
  CUSTOMER_ID
)
