package test3.xyl.com.flutter_app3;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.BasicMessageChannel;
import io.flutter.plugin.common.StringCodec;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.view.FlutterRunArguments;
import io.flutter.plugin.common.BasicMessageChannel;
import io.flutter.plugin.common.BasicMessageChannel.MessageHandler;
import io.flutter.plugin.common.BasicMessageChannel.Reply;
import io.flutter.plugin.common.StringCodec;
import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterRunArguments;
import io.flutter.view.FlutterView;

public class MainActivity extends FlutterActivity {
  /**
   * FlutterView就是整个app flutter显示的UI
   * 以下要绑定activity的生命周期
   *  flutterView.onPause();
   *  flutterView.destroy();
   *  flutterView.onPostResume();
   *
   */
  private FlutterView flutterView;
  //信息通道
  private BasicMessageChannel<String> messageChannel;

  private int counter;
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

    //创建一个信息通道 "1"和flutter对应
    messageChannel = new BasicMessageChannel<>(flutterView, "1", StringCodec.INSTANCE);
    messageChannel.setMessageHandler(new MessageHandler<String>() {
      //接收flutter传递过来的数据
      @Override
      public void onMessage(String s, Reply<String> reply) {
        System.out.println(">]得到flutter数据="+s);
        counter++;
        TextView textView = findViewById(R.id.button_tap);
        String value = "Flutter button tapped " + counter + (counter == 1 ? " time" : " times");
        textView.setText(value);
        reply.reply("ooooo");
      }
    });

    findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //发送数据给flutter层
        messageChannel.send("这是Java数据");
      }
    });
  }


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
