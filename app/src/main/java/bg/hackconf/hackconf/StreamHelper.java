package bg.hackconf.hackconf;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import bg.hackconf.hackconf.services.stream.Stream;
import bg.hackconf.hackconf.services.stream.StreamServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StreamHelper {
    private Activity ctx;
    private String url;

    public StreamHelper(Activity ctx) {
        this.ctx = ctx;
        StreamServiceFactory.getInstance().getHackConfStream().enqueue(new Callback<Stream>() {
            @Override
            public void onResponse(Call<Stream> call, Response<Stream> response) {
                url = response.body().getUrl();
            }

            @Override
            public void onFailure(Call<Stream> call, Throwable t) { }
        });
    }

    public void onLiveStreamClick(Activity ctx, View view) {
        if (url != null) {
            Intent stream = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            ctx.startActivity(stream);
        }
    }
}
