package de.b08.moodivation.intervention.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import java.util.List;

import de.b08.moodivation.R;
import de.b08.moodivation.intervention.Intervention;

public class InterventionView extends LinearLayout {

    private final int COLUMN_COUNT = 3;

    private final TextView interventionTitle;
    private final TextView interventionDescription;
    private final LinearLayout interventionContentView;

    private Intervention intervention;

    private boolean contentViewAllowed = true;

    public InterventionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.intervention_view, this);

        interventionTitle = findViewById(R.id.interventionTitle);
        interventionDescription = findViewById(R.id.interventionDescription);
        interventionContentView = findViewById(R.id.interventionContentView);
    }

    public void setIntervention(Intervention intervention) {
        this.intervention = intervention;
        initViews();
    }

    private void initViews() {
        interventionTitle.setText(intervention.getTitle());

        initDescriptionTextView();

        interventionContentView.removeAllViews();
        if (contentViewAllowed) {
            initImageViewIfAvailable();
            initVideoViewIfAvailable();
        }
    }

    private void initImageViewIfAvailable() {
        if (!intervention.getOptions().isShowOptionalImages())
            return;

        if (intervention.getInterventionMedia() == null)
            return;

        GridView imageGrid = new GridView(getContext());
        imageGrid.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        AsyncTask.execute(() -> {
            List<Drawable> imageDrawables = intervention.getInterventionMedia().getDrawableImages(getContext());
            Handler mainHandler = new Handler(getContext().getMainLooper());
            if (imageDrawables == null) {
                mainHandler.post(() -> imageGrid.setVisibility(GONE));
                return;
            }
            mainHandler.post(() -> {
                imageGrid.setVisibility(VISIBLE);
                imageGrid.setNumColumns(imageDrawables.size());
                imageGrid.setAdapter(new DrawableAdapter(getContext(), imageDrawables, imageGrid));
            });
        });

        interventionContentView.addView(imageGrid);
    }

    private void initVideoViewIfAvailable() {
        if (!intervention.getOptions().isShowOptionalVideo())
            return;

        if (intervention.getInterventionMedia() == null)
            return;

        MediaItem videoMediaItem = intervention.getInterventionMedia().getVideoMediaItem();
        if (videoMediaItem == null)
            return;

        PlayerView playerView = new PlayerView(getContext());

        ExoPlayer player = new ExoPlayer.Builder(getContext())
                .build();

        player.setMediaItem(videoMediaItem);

        playerView.setPlayer(player);
        interventionContentView.addView(playerView);
    }

    private void initDescriptionTextView() {
        if (intervention.getDescription() != null)
            interventionDescription.setText(intervention.getDescription());
        else
            interventionDescription.setVisibility(GONE);
    }

    public Intervention getIntervention() {
        return intervention;
    }

    public void setContentViewAllowed(boolean contentViewAllowed) {
        this.contentViewAllowed = contentViewAllowed;
    }

    private static class SquaredImageView extends AppCompatImageView {

        public SquaredImageView(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int size = Math.min(widthMeasureSpec, heightMeasureSpec);
            super.onMeasure(size, size);
        }

    }

    private static class DrawableAdapter extends BaseAdapter {

        private final List<Drawable> drawables;
        private final Context context;
        private final GridView gridView;

        public DrawableAdapter(Context context, List<Drawable> drawables, GridView gridView) {
            this.drawables = drawables;
            this.context = context;
            this.gridView = gridView;
        }

        @Override
        public int getCount() {
            return drawables.size();
        }

        @Override
        public Drawable getItem(int position) {
            return drawables.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new SquaredImageView(context);
                initImageView((SquaredImageView) convertView);
            }

            ((SquaredImageView) convertView).setImageDrawable(drawables.get(position));

            return convertView;
        }

        private void initImageView(SquaredImageView view) {
            view.setLayoutParams(new ViewGroup.LayoutParams(gridView.getColumnWidth(), gridView.getColumnWidth()));
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

    }

}
