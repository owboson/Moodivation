# 23B08

* [Getting Started](#getting-started)
* [Server Setup](server/setup.md)
* [How to specify questionnaires?](questionnaire_specification.md)
* [How to create custom interventions?](intervention.md)
* [License](#license)

## Getting Started

The implementation of the wellbeing algorithm can be found [here](https://github.com/RUB-SE-LAB/23B08/blob/main/Moodivation/app/src/main/java/de/b08/moodivation/questionnaire/SimpleWellbeingAlgorithm.java). If you want to add an entirely different algorithm without losing the old one, consider adding a new class that implements [`WellbeingAlgorithm.java`](https://github.com/RUB-SE-LAB/23B08/blob/main/Moodivation/app/src/main/java/de/b08/moodivation/questionnaire/WellbeingAlgorithm.java) and set the singleton in [`WellbeingAlgorithm.java`](https://github.com/RUB-SE-LAB/23B08/blob/main/Moodivation/app/src/main/java/de/b08/moodivation/questionnaire/WellbeingAlgorithm.java) accordingly.

## License

The following files (in the folder `Moodivation/app/src/main/res`) are Google Material Icons (https://github.com/google/material-design-icons) licensed under Apache-2.0 (http://www.apache.org/licenses/LICENSE-2.0):

```
drawable/baseline_5k_plus_24_black.xml
drawable/baseline_5k_plus_24_grey.xml
drawable/baseline_cloud_sync_24.xml
drawable/baseline_directions_bike_24_achieved.xml
drawable/baseline_directions_bike_24_grey.xml
drawable/baseline_directions_run_24.xml
drawable/baseline_directions_run_24_achieved.xml
drawable/baseline_directions_run_24_grey.xml
drawable/baseline_sports_score_24.xml
drawable/baseline_star_24_achieved.xml
drawable/baseline_star_24_grey.xml
drawable/moodivation_icon_foreground.xml
drawable/round_home_24.xml
drawable/round_settings_24.xml
```

The following files contain Google Material Icons (in the folder `Moodivation/app/src/main/`):

```
moodivation_icon-playstore.png

res/mipmap-anydpi-v26/moodivation_icon.xml
res/mipmap-anydpi-v26/moodivation_icon_round.xml

res/mipmap-hdpi/moodivation_icon.png
res/mipmap-hdpi/moodivation_icon_round.png

res/mipmap-mdpi/moodivation_icon.png
res/mipmap-mdpi/moodivation_icon_round.png

res/mipmap-xhdpi/moodivation_icon.png
res/mipmap-xhdpi/moodivation_icon_round.png

res/mipmap-xxhdpi/moodivation_icon.png
res/mipmap-xxhdpi/moodivation_icon_round.png

res/mipmap-xxxhdpi/moodivation_icon.png
res/mipmap-xxxhdpi/moodivation_icon_round.png
```

All other code files are licensed under MIT License:

```
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
```
