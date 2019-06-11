import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'Page1.dart';
void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  /*
  创建一个信息通道
  StringCodec是services里面的
  '1'对应Java的通道
   */
  static const BasicMessageChannel<String> platform =BasicMessageChannel<String>('1', StringCodec());

  int _counter = 0;

  @override
  void initState() {
    super.initState();
    //注册回调函数
    platform.setMessageHandler(_handlePlatformIncrement);
  }
  /*
  监听接收Java传过来的数据
   */
  Future<String> _handlePlatformIncrement(String message) async {
    print(">]得到Java数据="+message);
    setState(() {
      _counter++;
    });
    return message;
  }
  /*
  点击事件，
  往Java里面发送数据,
   */
  void _sendClick() {
    platform.send("这是flutter数据");
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body:Container(
          padding: EdgeInsets.all(20),
          child:Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              SizedBox(height: 80,),
              Text('Flutter部分', style: TextStyle(fontSize: 30.0)),
              Text(
                  '被Java刷新flutter页面 $_counter',
                  style: const TextStyle(fontSize: 17.0)
              ),
              RaisedButton(
                  onPressed: _sendClick,
                  child: Text('点击刷新Java页面')
              ),
              RaisedButton(
                  onPressed: (){
                    Navigator.push(context, MaterialPageRoute(
                        builder:(context)=> new Page1()
                    ) );
                  },
                  child: Text('页面跳转')
              )
            ],
          )
      ),
    );
  }
}
