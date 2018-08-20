package com.idcg.idcw.model.params;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：hxy
 * 电话：13891436532
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 类描述：FoxIDCW com.idcg.idcw.model.params ${CLASS_NAME}
 * 备注消息：
 * 修改时间：2018/3/16 9:49
 **/

public class PhraseDataReqAndResParam implements Serializable {

    private List<RandomWordBean> RandomWord;
    private List<VerinfyWordBean> VerinfyWord;

    public List<RandomWordBean> getRandomWord() {
        return RandomWord;
    }

    public void setRandomWord(List<RandomWordBean> RandomWord) {
        this.RandomWord = RandomWord;
    }

    public List<VerinfyWordBean> getVerinfyWord() {
        return VerinfyWord;
    }

    public void setVerinfyWord(List<VerinfyWordBean> VerinfyWord) {
        this.VerinfyWord = VerinfyWord;
    }

    public static class RandomWordBean implements Serializable {
        /**
         * serial_number : 1
         * phrase : toss
         */
        private int serial_number;
        private String phrase;
        private int index;
        private boolean isSelect = false;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }
        public int getSerial_number() {
            return serial_number;
        }

        public void setSerial_number(int serial_number) {
            this.serial_number = serial_number;
        }

        public String getPhrase() {
            return phrase;
        }

        public void setPhrase(String phrase) {
            this.phrase = phrase;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    public static class VerinfyWordBean implements Serializable {
        /**
         * serial_number : 1
         * phrase : toss
         */
        private int serial_number;
        private String phrase;

        public int getSerial_number() {
            return serial_number;
        }

        public void setSerial_number(int serial_number) {
            this.serial_number = serial_number;
        }

        public String getPhrase() {
            return phrase;
        }

        public void setPhrase(String phrase) {
            this.phrase = phrase;
        }
    }
}
