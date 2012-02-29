package org.dandelion.radiot.live.core.states;

import android.media.MediaPlayer;
import org.dandelion.radiot.live.core.LiveShowQuery;
import org.dandelion.radiot.live.core.PlaybackContext;

public class BasicState {
//	private static String liveShowUrl = "http://radio10.promodeejay.net:8181/stream";
	public static String liveShowUrl = "http://icecast.bigrradio.com/80s90s";
	public static int waitTimeout = 60 * 1000;

    public static void setLiveShowUrl(String value) {
		liveShowUrl = value;
	}

	public static void setWaitTimeoutSeconds(int value) {
		waitTimeout = value * 1000;
	}

    protected PlaybackContext context;
	private long timestamp;

    public ILiveShowService getService() {
        return context.service;
    }

    public MediaPlayer getPlayer() {
        return context.player;
    }

    public interface ILiveShowService {
		void switchToNewState(BasicState newState);

		void goForeground(int stringId);

		void goBackground();

		void runAsynchronously(Runnable runnable);

		void resetTimeout();

		void setTimeout(int waitTimeout, Runnable action);

		void lockWifi();

		void unlockWifi();
	}

    public BasicState(PlaybackContext context) {
        this.context = context;
        this.timestamp = System.currentTimeMillis();
    }

    public void enter() {
	}

	public void leave() {
	}

	public void acceptVisitor(LiveShowQuery visitor) {
	}

	public void stopPlayback() {
		getService().switchToNewState(newStopping());
	}


    public void startPlayback() {
	}

	public long getTimestamp() {
		return timestamp;
	}

    private Stopping newStopping() {
        return new Stopping(context);
    }

    protected Waiting newWaiting() {
        return new Waiting(context);
    }

    protected Playing newPlaying() {
        return new Playing(context);
    }

    protected Connecting newConnecting() {
        return new Connecting(context);
    }

    protected Idle newIdle() {
        return new Idle(context);
    }
}
