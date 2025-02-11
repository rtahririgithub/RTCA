create or replace type agg_t as object 
(
    str_agg varchar2(6000),

    static function ODCIAggregateInitialize(sctx  in out agg_t) return number,

    member function ODCIAggregateIterate(self in out agg_t, value in varchar2) return number,

    member function ODCIAggregateTerminate(self in agg_t, return_value out varchar2, flags in number) return number,

    member function ODCIAggregateMerge(self in out agg_t, ctx2 in agg_t) return number
);   
/

create or replace type body agg_t is 

    static function ODCIAggregateInitialize(sctx in out agg_t) 
        return number is 
    begin
        sctx := agg_t(null);
        return ODCIConst.Success;
    end;

    member function ODCIAggregateIterate(
      self in out agg_t, value in varchar2) 
        return number is
    begin
        if str_agg is not null then
            str_agg := str_agg || '|';
        end if;
        
        str_agg := str_agg||value;
        return ODCIConst.Success;
    end;

    member function ODCIAggregateTerminate(self in agg_t, 
        return_value out varchar2, flags in number) return number is
    begin
        return_value := str_agg;
        return ODCIConst.Success;
    end;

    member function ODCIAggregateMerge(self in out agg_t, 
        ctx2 in agg_t) return number is
    begin
        str_agg := str_agg || ctx2.str_agg;
        return ODCIConst.Success;
    end;
end;
/

create or replace function agg_concat (input varchar2) return varchar2 parallel_enable aggregate using agg_t;
/

exit;