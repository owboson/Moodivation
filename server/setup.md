# Setup

Requirements:
- PHP Server (for testing purposes you can use the `php` command; see https://www.php.net/downloads)
- Local PostgreSQL database
  - Script to setup `ids` table: ```create table ids (uuid varchar(128) not null primary key);```

To connect to your database, you may need to change the following parameters in id.php:

```PHP
$host = "localhost";
$port = "5432";
$database = "moodivation";
$user = "postgres";
$password = "";
```

Additionally, you need to update the URLs [here](https://github.com/RUB-SE-LAB/23B08/blob/main/Moodivation/app/src/main/java/de/b08/moodivation/utils/ExportUtils.java#L34) and [here](https://github.com/RUB-SE-LAB/23B08/blob/19efeede2592a5724dae619ec86b4587d27404ff/Moodivation/app/src/main/java/de/b08/moodivation/utils/ExportUtils.java#L64).
