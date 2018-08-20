package foxidcw.android.idcw.otc.utils;

/**
 * Created by yuzhongrong on 2018/5/8.
 */

public class OTCChatBeanTransformation {


    public static void getHistoryBeanTransform(){


        //这里需要一个对象转换成处理
//        Flowable.just(data)
//                .observeOn(Schedulers.io())
//                .map(new Function<List<OTCChatHistoryBean>, List<IMessage>>() {
//                    @Override
//                    public List<IMessage> apply(List<OTCChatHistoryBean> otcChatHistoryBeans) throws Exception {
//
//                        List<IMessage> newmessage= new ArrayList<>(otcChatHistoryBeans.size());
//
//                        for (OTCChatHistoryBean item:otcChatHistoryBeans) {
//
//                            if(!TextUtils.isEmpty(item.getMessage())) {//文字
//
//                                if(item.getChatObjectCategory()==0){//系统消息
//                                    newmessage.add(ChatMessage.createSysMessage(item.getMessage()));
//
//                                }else{
//                                    ChatMessage message;
//
//                                    if(userId.equals(item.getSendUserID())){//我的消息
////                                                             message.setDirction(true);
//                                        message= ChatMessage.createTextSendMessage(item.getMessage(),"");
//                                        message.setStatus(IMessage.STATUS_SUCCESS);
//
//                                    }else{//对方消息
//
//                                        message=ChatMessage.createTextReceiveMessage(item.getMessage(),"");
//
//                                    }
//                                    newmessage.add(message);
//                                }
//
//                            }else{//图片
//
//                                ChatMessage  message;
//                                if(!userId.equals(item.getUserID())){
//                                    message=ChatMessage.createImgReceiveMessage(item.getFileUrl(),"");
//
//                                }else{
//                                    message=ChatMessage.createImgSendMessage(item.getFileUrl(),"");
//                                    message.setStatus(ChatMessage.STATUS_SUCCESS);
//                                }
//                                newmessage.add(message);
//
//                            }
//
//
//
//                        }
//
//                        return newmessage;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new RxSubscriber<List<IMessage>>() {
//                    @Override
//                    public void onSuccess(List<IMessage> iMessages) {
//                        mMessageList.addData(iMessages);
//                    }
//
//                    @Override
//                    protected void onError(ResponseThrowable ex) {
//                        LogUtil.d("---转换失败---->"+ex.getMessage());
//                    }
//                });




    }

}
