# okhttpdemo
Hello everyOne.
实习了这么久终于开源一次了。
这次导师让我，做网络模块封装，底层采用的是okhttp。
翻了一遍网络上的，也木有找到能返回相关状态的demo，于是之自己写。
因为公司不让上传内部代码，所以说重新写了个小的demo，不能和业务重合，demo有点简陋，只有核心的几行代码，请见谅。

```java
public interface IProgressListener {
    void onStart(long contentLength);
    void onProgress(long currentLength);
    void onSuccess(String filePath);
}
//先定义一个接口

private void downloading(final OkHttpClient client, final Request request, IProgressListener listener) throws IOException {
        Response response = client.newCall(request).execute();
        //连接网络
        
        InputStream inputStream=null;
        int count ;
        byte[] bytes =  new byte[BYTESIZE];

        if (response.isSuccessful()) {
        //如果返回的状态码是成功的 获取inputstream 并且回调传入文件的长度
            inputStream = response.body().byteStream();
            listener.onStart(response.body().contentLength());
        }

       //io时 传出 目前读取进度
        if (inputStream!=null){
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            int sumCount =0;
            while ((count=bis.read(bytes))!=-1){
                sumCount+=count;
                listener.onProgress(sumCount);
            }
        }


        /**
         * todo 校验文件 是否完整等问题
         */

        listener.onSuccess("ok");


    }


```

如果上传的请使用 大神写的 [CoreProgess](https://github.com/lizhangqu/CoreProgress)，膜拜过，但在这里有个bug:sink 中获取的byteCount然后传出，这里bytecount 是 byte数组的size 是固定死的，但在io时，每次读出来的count是小于 byte数组的size的 。我也给大神fork过了。




良辰在此有理了，geckhan。


