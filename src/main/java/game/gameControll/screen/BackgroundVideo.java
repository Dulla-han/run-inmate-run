package game.gameControll.screen;

import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.bytedeco.ffmpeg.global.avutil.AV_PIX_FMT_RGB24;

/** Loops a silent video using bundled FFmpeg, independently of JavaFX Media. */
final class BackgroundVideo {
    private static final System.Logger LOGGER = System.getLogger(BackgroundVideo.class.getName());
    private static final long FRAME_INTERVAL_US = 33_333;

    private final URL resource;
    private final ImageView view = new ImageView();
    private final AnimationTimer renderer;
    private Playback playback;
    private WritableImage image;

    BackgroundVideo(URL resource) {
        this.resource = resource;
        renderer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (playback == null) return;
                VideoFrame frame = playback.latest.getAndSet(null);
                if (frame == null) return;
                if (image == null || image.getWidth() != frame.width || image.getHeight() != frame.height) {
                    image = new WritableImage(frame.width, frame.height);
                    view.setImage(image);
                }
                image.getPixelWriter().setPixels(0, 0, frame.width, frame.height,
                        PixelFormat.getByteRgbInstance(), frame.pixels, 0, frame.stride);
            }
        };
    }

    ImageView getView() {
        return view;
    }

    void play() {
        if (playback != null || resource == null) return;
        playback = new Playback();
        renderer.start();
        playback.thread.start();
    }

    void stop() {
        renderer.stop();
        if (playback != null) {
            playback.thread.interrupt();
            playback = null;
        }
        view.setImage(null);
        image = null;
    }

    private final class Playback {
        private final AtomicReference<VideoFrame> latest = new AtomicReference<>();
        private final Thread thread = new Thread(this::decode, "menu-video-decoder");

        Playback() {
            thread.setDaemon(true);
        }

        private void decode() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    try (InputStream input = resource.openStream();
                         FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(input)) {
                        grabber.setPixelFormat(AV_PIX_FMT_RGB24);
                        grabber.setImageWidth(960);
                        grabber.setImageHeight(540);
                        grabber.start();

                        long start = System.nanoTime();
                        long firstTimestamp = Long.MIN_VALUE;
                        long nextFrame = 0;
                        Frame frame;
                        while (!Thread.currentThread().isInterrupted() && (frame = grabber.grabImage()) != null) {
                            long timestamp = grabber.getTimestamp();
                            if (firstTimestamp == Long.MIN_VALUE) firstTimestamp = timestamp;
                            long elapsed = Math.max(0, timestamp - firstTimestamp);
                            if (elapsed < nextFrame) continue;
                            nextFrame = elapsed + FRAME_INTERVAL_US;

                            long wait = start + TimeUnit.MICROSECONDS.toNanos(elapsed) - System.nanoTime();
                            if (wait > 0) TimeUnit.NANOSECONDS.sleep(wait);
                            if (Thread.currentThread().isInterrupted()) return;

                            // FFmpeg reuses the frame buffer; copy before handing it to JavaFX.
                            int stride = frame.imageStride;
                            int size = stride * (frame.imageHeight - 1) + frame.imageWidth * 3;
                            ByteBuffer buffer = ((ByteBuffer) frame.image[0]).duplicate();
                            buffer.clear();
                            byte[] pixels = new byte[size];
                            buffer.get(pixels);
                            latest.set(new VideoFrame(frame.imageWidth, frame.imageHeight, stride, pixels));
                        }
                        if (firstTimestamp == Long.MIN_VALUE) return;
                    }
                }
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            } catch (Exception | LinkageError e) {
                if (!Thread.currentThread().isInterrupted()) {
                    LOGGER.log(System.Logger.Level.WARNING, "Menu video unavailable; using a static background", e);
                }
            }
        }
    }

    private record VideoFrame(int width, int height, int stride, byte[] pixels) { }
}
