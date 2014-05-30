LEGOMind-WebSocket-Client
=========================

WebSocket Client on LEGO MindStorms

本 Maven パッケージは、デプロイ時に自動的に LEGO Mindstorms に jar ファイルを
scp を利用しデプロイします。

NetBeans でビルド時に自動的に生成された jar ファイルを LEGO Mindstorms に
デプロイするためには、LEGO Mindstorms に割りあてられている IP アドレスを
下記の、pom.xml ファイルに記載してください。

pom.xml ファイルの修正箇所
<url>scp://192.168.1.100/</url> 

また、本プロジェクトの生成物(jarファイル)を LEGO Mindsotms 上で実行するためには、
WebSocket (Tyrus) のクライアントライブラリが必要です。
事前に、LEGO Mindstorms に scp 等を利用し下記のファイルをコピーしてください。

root@EV3:/home/lejos/lib# pwd
/home/lejos/lib <---- lib ディレクトリを作成

root@EV3:/home/lejos/lib# ディレクトリ配下に下記ファイルを事前にコピー ---------->
grizzly-framework-2.3.3.jar
grizzly-http-2.3.3.jar
grizzly-http-server-2.3.3.jar
grizzly-rcm-2.3.3.jar
javax.json-1.0.1.jar 
javax.json-api-1.0.jar
javax.websocket-api-1.0.jar
javax.websocket-client-api-1.0.jar
tyrus-client-1.1.jar
tyrus-container-grizzly-1.1.jar
tyrus-core-1.1.jar
tyrus-spi-1.1.jar
tyrus-websocket-core-1.1.jar


