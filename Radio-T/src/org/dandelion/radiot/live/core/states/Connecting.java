package org.dandelion.radiot.live.core.states;

import android.media.MediaPlayer;
import org.dandelion.radiot.live.core.LiveShowQuery;
import org.dandelion.radiot.live.core.PlaybackContext;

public class Connecting extends BasicState {
    private MediaPlayer.OnPreparedListener onPrepared = new MediaPlayer.OnPreparedListener() {
        public void onPrepared(MediaPlayer mp) {
            getService().switchToNewState(newPlaying());
        }
    };
    private MediaPlayer.OnErrorListener onError = new MediaPlayer.OnErrorListener() {
        public boolean onError(MediaPlayer mp, int what, int extra) {
            getService().switchToNewState(newWaiting());
            return false;
        }
    };

    public Connecting(PlaybackContext context) {
        super(context);
        this.getPlayer().setOnPreparedListener(onPrepared);
        this.getPlayer().setOnErrorListener(onError);
}

@Override
    public void enter() {
        try {
            getPlayer().reset();
            getPlayer().setDataSource(liveShowUrl);
            getPlayer().prepareAsync();
            getService().goForeground(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void acceptVisitor(LiveShowQuery visitor) {
        visitor.onConnecting(this);
    }
}
