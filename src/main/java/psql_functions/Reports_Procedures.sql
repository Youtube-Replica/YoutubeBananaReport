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


CREATE OR REPLACE FUNCTION update_report_by_id (_reportID int, _status varchar(255))
RETURNS integer AS
$BODY$
DECLARE
  a_count integer;
BEGIN
   UPDATE Reports
   SET status = _status
   WHERE reportID = _reportID;
   GET DIAGNOSTICS a_count = ROW_COUNT;
   RETURN a_count;
END;
$BODY$
LANGUAGE 'plpgsql' VOLATILE;


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
  

CREATE OR REPLACE FUNCTION post_report_user_by_id(_submitterID int, _againstID int, _content varchar(8000))
RETURNS VOID
AS
$BODY$
BEGIN
   INSERT INTO Reports (submitterid, againstid, content)
   VALUES (_submitterID, _againstID, _content);
END;
$BODY$
LANGUAGE 'plpgsql' VOLATILE;
