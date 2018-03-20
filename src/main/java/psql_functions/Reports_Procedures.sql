CREATE OR REPLACE FUNCTION GetReportByID(ReportID integer, SubmitterID integer, AgainstID integer)
RETURNS TABLE(report_id int,
   submitter_id int,
   against_id int,
   content varchar(8000),
   status varchar(255)) AS
$$
BEGIN
   SELECT *
   FROM Reports
   WHERE (ReportID IS NULL OR ReportID = ReportID) AND (SubmitterID IS NULL OR SubmitterID = SubmitterID) AND (AgainstID IS NULL OR AgainstID = AgainstID);
END
$$
 LANGUAGE plpgsql;





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



CREATE OR REPLACE FUNCTION RemoveReportByID (ReportID int)
RETURNS BOOL AS
$$
BEGIN
   DELETE FROM Reports
   WHERE ReportID = ReportID;
END
$$
LANGUAGE plpgsql;
  

CREATE OR REPLACE FUNCTION ReportUserByID(SubmitterID int, AgainstID int, Content varchar(8000))
RETURNS BOOL AS $$
BEGIN
   INSERT INTO Reports (SubmitterID,AgainstID, Content)  
   VALUES (SubmitterID, AgainstID,content);
END
$$
LANGUAGE plpgsql;
