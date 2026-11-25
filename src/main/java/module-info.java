module cz.cvut.fel.pjv.gameon {
    requires javafx.controls;
    requires org.bytedeco.javacv;
    requires org.bytedeco.ffmpeg;
    requires org.bytedeco.ffmpeg.platform;
    // Platform bundle descriptors use static dependencies; resolve the desktop native modules at runtime.
    requires org.bytedeco.ffmpeg.linux.x86_64;
    requires org.bytedeco.ffmpeg.linux.arm64;
    requires org.bytedeco.ffmpeg.windows.x86_64;
    requires org.bytedeco.ffmpeg.macosx.x86_64;
    requires org.bytedeco.ffmpeg.macosx.arm64;
    requires org.bytedeco.javacpp.linux.x86_64;
    requires org.bytedeco.javacpp.linux.arm64;
    requires org.bytedeco.javacpp.windows.x86_64;
    requires org.bytedeco.javacpp.macosx.x86_64;
    requires org.bytedeco.javacpp.macosx.arm64;
    requires com.google.gson;
    exports game;
    exports game.entity;
    exports game.gameControll;
    exports game.gameControll.screen;
}
