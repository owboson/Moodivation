<?php
$host = "localhost";
$port = "5432";
$database = "moodivation";
$user = "postgres";
$password = "";
$db = pg_connect("host=$host port=$port dbname=$database user=$user password=$password");
$num_bytes = 64;

if (!$db) {
  echo json_encode(array(
    "result" => 0,
    "id" => "null"
  ));
  return;
}

$id = bin2hex(random_bytes($num_bytes));
while (pg_num_rows(pg_query_params($db, 'SELECT * FROM ids WHERE uuid=$1', array($id)))>0) {
  $id = bin2hex(random_bytes($num_bytes));
}
pg_query_params($db, 'INSERT INTO ids (uuid) VALUES ($1)', array($id));
echo json_encode(array(
  "result" => 1,
  "id" => $id
));
?>
