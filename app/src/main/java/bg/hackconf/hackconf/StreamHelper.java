package bg.hackconf.hackconf;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

public class StreamHelper {
    public static void onLiveStreamClick(Activity ctx, View view) {
        Intent stream = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=imA37MEq68A"));
        ctx.startActivity(stream);
    }
}
