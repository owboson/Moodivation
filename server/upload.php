/*
MIT License

Copyright (c) 2023 RUB-SE-LAB

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

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
