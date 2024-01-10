# Interventions

## How to create an intervention?

1. Create a folder (folder name must end with `.intervention`).
2. Create a bundledata.json inside this folder, with the following content:
```
{
  "id":"YOUR_CUSTOM_INTERVENTION_ID",
  "languages":["en", "de"]
}
```
The languages must be specified in ISO 639 (see language section for examples: [link](https://developer.android.com/reference/java/util/Locale)).

3. Inside of your intervention folder, create a folder for each language specified in `bundledata.json` (the folder name must match the language name).

4. Create an intervention.json file in the language folder for each language with the following content:

```
{
  "description":"Your description",
  "displayedDataTypes":["ELAPSED_TIME","TRAVELLED_DISTANCE", "SPEED"],
  "interventionMediaData": {
    "imagePaths":["../your_image.jpg"],
    "videoPath":"../your_video.mp4"
  },
  "interventionOptionsData": {
    "showOptionalImages":true,
    "showOptionalVideo":false
  },
  "title":"Your Title"
}
```

Both `interventionMediaData` and `interventionOptionsData` are optional. Also, if `interventionMediaData` is specified, both `imagePaths` and `videoPath` are optional.

With `displayedDataTypes` you can specify which values are displayed to the user. Currently only the following values can be displayed:

- `ELAPSED_TIME`
- `TRAVELLED_DISTANCE`
- `SPEED`
- `STEPS`

**Note:** The images and videos can be placed in any directory within the intervention folder.

## Where to store the interventions?

If you want the intervention to be delivered together with the app, you need to put the intervention (may not be compressed) into [`Moodivation/app/src/main/assets/interventions`](../Moodivation/app/src/main/assets/interventions).

Otherwise, create a ZIP file of your intervention folder, upload it somewhere and provide users with a download link.
