-- name: list-hives-with-scope
-- Lists the details for a hive given it is in the list
SELECT *
FROM hives
WHERE uuid IN (:uuids)

-- name: lookup-hives-with-scope
-- Gets all the details for a hive given it is in the list
SELECT *
FROM hives
WHERE uuid = :hive_uuid
LIMIT 1
