package com.andy.orange.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */

public class SongListInfo {

    /**
     * error_code : 22000
     * total : 9330
     * havemore : 1
     * content : [{"listid":"497943333","listenum":"19297","collectnum":"79","title":"花的姿态|音乐诗人陈绮贞精选","pic_300":"http://musicugc.cdn.qianqian.com/ugcdiy/pic/b3061cef2a7838ab9eb81a3598249848.jpg","tag":"华语,流行","desc":"她代表了一种台湾女生的样子，认真生活，认真创作。用一把吉他走出女性的摇滚梦想，步伐温柔坚定。她所掀起的文艺风格，正影响音乐界与世代青年。她就是陈绮贞，华语乐坛中最具代表性的女歌手之一，她似乎永远有一种最简单的生活方式，摇曳出她最独特的姿态，在当下一片喧哗之中，静静坚持她最坚韧的成长。她的创作富含概念，挥洒自如的和弦、轻松自然的散文词句，每每只是几句歌词和几个音符就已是令人陶醉。你惊心动魄的人生，是否正在流浪？就请跟随陈绮贞的音乐，一起华丽的盛放青春的生命吧！\n","pic_w300":"http://musicugc.cdn.qianqian.com/ugcdiy/pic/b3061cef2a7838ab9eb81a3598249848.jpg","width":"300","height":"300","songIds":["621106","14427189","590470","745998","18350694","18349550","2044195","921706","18351223","243077281"]},{"listid":"503171155","listenum":"120","collectnum":"2","title":"一秒惊艳！无前奏的质感女声！","pic_300":"http://musicugc.cdn.qianqian.com/ugcdiy/pic/23cbc1083293af7ef2436a9f820990dc.jpg","tag":"欧美,流行","desc":"无需前奏点缀的质感女声，一秒惊艳你的耳朵。","pic_w300":"http://musicugc.cdn.qianqian.com/ugcdiy/pic/23cbc1083293af7ef2436a9f820990dc.jpg","width":"300","height":"300","songIds":["7292611","7986407","8868143","1154713","11283661","7907317","543468308","890041","288769603","269398624"]}]
     */

    private int error_code;
    private int total;
    private int havemore;
    private List<ContentBean> content;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getHavemore() {
        return havemore;
    }

    public void setHavemore(int havemore) {
        this.havemore = havemore;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * listid : 497943333
         * listenum : 19297
         * collectnum : 79
         * title : 花的姿态|音乐诗人陈绮贞精选
         * pic_300 : http://musicugc.cdn.qianqian.com/ugcdiy/pic/b3061cef2a7838ab9eb81a3598249848.jpg
         * tag : 华语,流行
         * desc : 她代表了一种台湾女生的样子，认真生活，认真创作。用一把吉他走出女性的摇滚梦想，步伐温柔坚定。她所掀起的文艺风格，正影响音乐界与世代青年。她就是陈绮贞，华语乐坛中最具代表性的女歌手之一，她似乎永远有一种最简单的生活方式，摇曳出她最独特的姿态，在当下一片喧哗之中，静静坚持她最坚韧的成长。她的创作富含概念，挥洒自如的和弦、轻松自然的散文词句，每每只是几句歌词和几个音符就已是令人陶醉。你惊心动魄的人生，是否正在流浪？就请跟随陈绮贞的音乐，一起华丽的盛放青春的生命吧！

         * pic_w300 : http://musicugc.cdn.qianqian.com/ugcdiy/pic/b3061cef2a7838ab9eb81a3598249848.jpg
         * width : 300
         * height : 300
         * songIds : ["621106","14427189","590470","745998","18350694","18349550","2044195","921706","18351223","243077281"]
         */

        private String listid;
        private String listenum;
        private String collectnum;
        private String title;
        private String pic_300;
        private String tag;
        private String desc;
        private String pic_w300;
        private String width;
        private String height;
        private List<String> songIds;

        public String getListid() {
            return listid;
        }

        public void setListid(String listid) {
            this.listid = listid;
        }

        public String getListenum() {
            return listenum;
        }

        public void setListenum(String listenum) {
            this.listenum = listenum;
        }

        public String getCollectnum() {
            return collectnum;
        }

        public void setCollectnum(String collectnum) {
            this.collectnum = collectnum;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPic_300() {
            return pic_300;
        }

        public void setPic_300(String pic_300) {
            this.pic_300 = pic_300;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPic_w300() {
            return pic_w300;
        }

        public void setPic_w300(String pic_w300) {
            this.pic_w300 = pic_w300;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public List<String> getSongIds() {
            return songIds;
        }

        public void setSongIds(List<String> songIds) {
            this.songIds = songIds;
        }
    }
}
