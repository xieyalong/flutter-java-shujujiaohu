package test3.xyl.com.flutter_app3;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.BasicMessageChannel;
import io.flutter.plugin.common.StringCodec;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterRunArguments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import io.flutter.plugin.common.BasicMessageChannel;
import io.flutter.plugin.common.BasicMessageChannel.MessageHandler;
import io.flutter.plugin.common.BasicMessageChannel.Reply;
import io.flutter.plugin.common.StringCodec;
import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterRunArguments;
import io.flutter.view.FlutterView;
import java.util.ArrayList;
public class MainActivity extends FlutterActivity {
  private FlutterView flutterView;
  private int counter;
  private static final String CHANNEL = "increment";
  private static final String EMPTY_MESSAGE = "";
  private static final String PING = "ping";
  private BasicMessageChannel<String> messageChannel;

  private String[] getArgsFromIntent(Intent intent) {
    ArrayList<String> args = new ArrayList<>();
    if (intent.getBooleanExtra("trace-startup", false)) {
      args.add("--trace-startup");
    }
    if (intent.getBooleanExtra("start-paused", false)) {
      args.add("--start-paused");
    }
    if (intent.getBooleanExtra("enable-dart-profiling", false)) {
      args.add("--enable-dart-profiling");
    }
    if (!args.isEmpty()) {
      String[] argsArray = new String[args.size()];
      return args.toArray(argsArray);
    }
    return null;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);

    String[] args = getArgsFromIntent(getIntent());
    FlutterMain.ensureInitializationComplete(getApplicationContext(), args);
    setContentView(R.layout.flutter_view_layout);

    FlutterRunArguments runArguments = new FlutterRunArguments();
    runArguments.bundlePath = FlutterMain.findAppBundlePath(getApplicationContext());
    runArguments.entrypoint = "main";

    flutterView = findViewById(R.id.flutter_view);
    flutterView.runFromBundle(runArguments);

    messageChannel = new BasicMessageChannel<>(flutterView, CHANNEL, StringCodec.INSTANCE);
    messageChannel.setMessageHandler(new MessageHandler<String>() {
              @Override
              public void onMessage(String s, Reply<String> reply) {
                counter++;
                TextView textView = findViewById(R.id.button_tap);
                String value = "Flutter button tapped " + counter + (counter == 1 ? " time" : " times");
                textView.setText(value);
                reply.reply(EMPTY_MESSAGE);
              }
            });

    findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        messageChannel.send(PING);
      }
    });
  }


  @Override
  protected void onDestroy() {
    if (flutterView != null) {
      flutterView.destroy();
    }
    super.onDestroy();
  }

  @Override
  protected void onPause() {
    super.onPause();
    flutterView.onPause();
  }

  @Override
  protected void onPostResume() {
    super.onPostResume();
    flutterView.onPostResume();
  }
}
