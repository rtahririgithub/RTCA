(:: pragma  parameter="$anyType1" type="anyType" ::)
(:: pragma  type="anyType" ::)

declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/test/test01/";

declare function xf:test01($anyType1 as element(*))
    as element(*) {
        $anyType1
};

declare variable $anyType1 as element(*) external;

xf:test01($anyType1)