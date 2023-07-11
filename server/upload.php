<?php
if (isset($_POST["id"])) {
  $id = $_POST["id"];
  $new_filename = $id . '/';
  $timestamp = microtime(true);
  $loc = $new_filename . 'loc';
  $sensor = $new_filename . 'sensor';
  $intervention = $new_filename . 'intervention';
  $questionnaire = $new_filename . 'questionnaire';
  if (!file_exists($loc)) {
    mkdir($loc, 0777, true);
  }
  if (!file_exists($sensor)) {
    mkdir($sensor, 0777, true);
  }
  if (!file_exists($intervention)) {
    mkdir($intervention, 0777, true);
  }
  if (!file_exists($questionnaire)) {
    mkdir($questionnaire, 0777, true);
  }
  if (move_uploaded_file($_FILES["locationdb"]["tmp_name"], $loc . '/' .$timestamp)
    and move_uploaded_file($_FILES["sensordb"]["tmp_name"], $sensor . '/' .$timestamp)
    and move_uploaded_file($_FILES["questionnairedb"]["tmp_name"], $questionnaire . '/' .$timestamp)
    and move_uploaded_file($_FILES["interventiondb"]["tmp_name"], $intervention . '/' .$timestamp)) {
    echo json_encode(array(
      "result" => 1
    ));
    return;
  } else {
    echo json_encode(array(
      "result" => 0
    ));
    return;
  }
} else {
  echo json_encode(array(
    "result" => 4
  ));
  return;
}
?>
