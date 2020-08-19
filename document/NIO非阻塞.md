## NIO（非阻塞io）
-   Selector，SelectionKey，ServerScoketChannel和SocketChannel管理梳理图
-   当客户端链接时，会通过ServerSocketChannel 创建一个对应的SocketChannel
-   将得到的额SocketChannel注册到Selector上去，一个Selector上可以注册多个SocketChannel
-   注册后返回一个SelectKey，会和该Selector关联（集合）
-   之后Selector可以进行监听，通过select方法，返回他管理的所有有事件发生的通道的个数。
-   进一步通过Selector获取各个SelectionKey(有事件发生的)，SelectKey 是和SocketChannel关联起来的，所以可以通过SelectKey反向获取SocketChannel。通过channel()方法

## SelectionKey
-   SelectionKey表示，Selector和网络通道的注册关系，共四种：
    -   int OP_ACCEPT：有新的网络链接可以accept，值为 16
    -   int OP_CONNECR:代表链接已经建立，值为8
    -   int OP_READ:代表读操作，值为 1
    -   int OP_WRITE ：代表写操作 值为 4

-   SocketChannel 和ServerSocketChannel都会被注册到Selector上去，
-   Selector中，有两个Set<SelectionKey>
-   一个用来存储所有注册的SelectionKey，另外一个用来存储有事件发生的SelectionKey，使用keys()方法可以获取到所有注册的SeletionKey，使用selectKey()方法可以获取到有事件发生的SeletionKey

## ServerSocketChannel
-   服务端
    -   open(); 得到一个ServerSocketChannel通道
    -   configureBlocking(boolean block); 设置阻塞或非阻塞模式，取值false表示非阻塞模式
    -   accept();接受一个链接，返回代表这个链接的通道对象。返回的其实就是一个SocketChannel
    -   register(Selector,int ops);注册一个选择器并设置关心的监听模式;
## SocketChannel
-   客户端
    -   open(); 得到一个SocketChannel通道
    -   configureBlocking(boolean block); 设置阻塞或非阻塞模式，取值false表示非阻塞模式
    -   connection(ScoketAddress remote); 链接服务器
    -   finishConnection();  如果上面的方法链接失败，接下来就要通过该方法完成链接操作。
    -   write(ByteBuffer src);  往通道里写数据
    -   read(ByteBuffer dst);   // 从通道里读取数据
    -   register(Selector,int ops,Object att);  注册一个选择器并设置监听事件，最后一个参数可以设置共享数据；
    -   close();管理通道；