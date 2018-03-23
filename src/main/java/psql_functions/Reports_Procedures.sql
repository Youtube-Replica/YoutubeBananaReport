CREATE OR REPLACE FUNCTION get_report_by_id (_id INT = NULL)
RETURNS refcursor AS
$BODY$
DECLARE 
ref refcursor;
BEGIN
OPEN ref FOR SELECT * FROM Reports WHERE ReportID= _id;
RETURN ref;
END;    
$BODY$
LANGUAGE 'plpgsql' VOLATILE;





CREATE OR REPLACE FUNCTION UpdateReportByID (ReportID int, Status varchar(255))
RETURNS BOOL AS
$$
BEGIN
   UPDATE Reports
   SET staus = status
   WHERE ReportID = ReportID;
END
$$
LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION delete_report(_id INT = NULL)
RETURNS integer AS
$BODY$
DECLARE
  a_count integer;
BEGIN
DELETE FROM Reports
WHERE ReportID = _id;
GET DIAGNOSTICS a_count = ROW_COUNT;
RETURN a_count;
END;
$BODY$
LANGUAGE 'plpgsql' VOLATILE;
  

CREATE OR REPLACE FUNCTION ReportUserByID(SubmitterID int, AgainstID int, Content varchar(8000))
RETURNS BOOL AS $$
BEGIN
   INSERT INTO Reports (SubmitterID,AgainstID, Content)  
   VALUES (SubmitterID, AgainstID,content);
END
$$
LANGUAGE plpgsql;
