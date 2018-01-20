package com.andy.orange.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Andy Lau on 2017/8/21.
 */

public class SubjectMovie implements Serializable {

    /**
     * rating : {"max":10,"average":8.9,"stars":"45","min":0}
     * reviews_count : 2765
     * wish_count : 56051
     * douban_site :
     * year : 2006
     * images : {"small":"https://img1.doubanio.com/view/movie_poster_cover/ipst/public/p1312700628.webp","large":"https://img1.doubanio.com/view/movie_poster_cover/lpst/public/p1312700628.webp","medium":"https://img1.doubanio.com/view/movie_poster_cover/spst/public/p1312700628.webp"}
     * alt : https://movie.douban.com/subject/1849031/
     * id : 1849031
     * mobile_url : https://movie.douban.com/subject/1849031/mobile
     * title : 当幸福来敲门
     * do_count : null
     * share_url : https://m.douban.com/movie/subject/1849031
     * seasons_count : null
     * schedule_url :
     * episodes_count : null
     * countries : ["美国"]
     * genres : ["剧情","传记","家庭"]
     * collect_count : 705104
     * casts : [{"alt":"https://movie.douban.com/celebrity/1027138/","avatars":{"small":"https://img3.doubanio.com/img/celebrity/small/31885.jpg","large":"https://img3.doubanio.com/img/celebrity/large/31885.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/31885.jpg"},"name":"威尔·史密斯","id":"1027138"},{"alt":"https://movie.douban.com/celebrity/1010532/","avatars":{"small":"https://img3.doubanio.com/img/celebrity/small/1357008358.0.jpg","large":"https://img3.doubanio.com/img/celebrity/large/1357008358.0.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/1357008358.0.jpg"},"name":"贾登·史密斯","id":"1010532"},{"alt":"https://movie.douban.com/celebrity/1040513/","avatars":{"small":"https://img1.doubanio.com/img/celebrity/small/1378018910.89.jpg","large":"https://img1.doubanio.com/img/celebrity/large/1378018910.89.jpg","medium":"https://img1.doubanio.com/img/celebrity/medium/1378018910.89.jpg"},"name":"坦迪·牛顿","id":"1040513"},{"alt":"https://movie.douban.com/celebrity/1317100/","avatars":{"small":"https://img1.doubanio.com/img/celebrity/small/38587.jpg","large":"https://img1.doubanio.com/img/celebrity/large/38587.jpg","medium":"https://img1.doubanio.com/img/celebrity/medium/38587.jpg"},"name":"布莱恩·豪威 ","id":"1317100"}]
     * current_season : null
     * original_title : The Pursuit of Happyness
     * summary : 克里斯•加纳（威尔·史密斯 Will Smith 饰）用尽全部积蓄买下了高科技治疗仪，到处向医院推销，可是价格高昂，接受的人不多。就算他多努力都无法提供一个良好的生活环境给妻儿，妻子（桑迪·牛顿 Thandie Newton 饰）最终选择离开家。从此他带着儿子克里斯托夫（贾登·史密斯 Jaden Smith 饰）相依为命。克里斯好不容易争取回来一个股票投资公司实习的机会，就算没有报酬，成功机会只有百分之五，他仍努力奋斗，儿子是他的力量。他看尽白眼，与儿子躲在地铁站里的公共厕所里，住在教堂的收容所里…… 他坚信，幸福明天就会来临。©豆瓣
     * subtype : movie
     * directors : [{"alt":"https://movie.douban.com/celebrity/1045093/","avatars":{"small":"https://img1.doubanio.com/img/celebrity/small/20409.jpg","large":"https://img1.doubanio.com/img/celebrity/large/20409.jpg","medium":"https://img1.doubanio.com/img/celebrity/medium/20409.jpg"},"name":"加布里埃莱·穆奇诺","id":"1045093"}]
     * comments_count : 106551
     * ratings_count : 535105
     * aka : ["寻找快乐的故事(港)","追求快乐","幸福追击"]
     */

    private RatingBean rating;
    private int reviews_count;
    private int wish_count;
    private String douban_site;
    private String year;
    private ImagesBean images;
    private String alt;
    private String id;
    private String mobile_url;
    private String title;
    private Object do_count;
    private String share_url;
    private Object seasons_count;
    private String schedule_url;
    private Object episodes_count;
    private int collect_count;
    private Object current_season;
    private String original_title;
    private String summary;
    private String subtype;
    private int comments_count;
    private int ratings_count;
    private List<String> countries;
    private List<String> genres;
    private List<CastsBean> casts;
    private List<DirectorsBean> directors;
    private List<String> aka;

    public RatingBean getRating() {
        return rating;
    }

    public void setRating(RatingBean rating) {
        this.rating = rating;
    }

    public int getReviews_count() {
        return reviews_count;
    }

    public void setReviews_count(int reviews_count) {
        this.reviews_count = reviews_count;
    }

    public int getWish_count() {
        return wish_count;
    }

    public void setWish_count(int wish_count) {
        this.wish_count = wish_count;
    }

    public String getDouban_site() {
        return douban_site;
    }

    public void setDouban_site(String douban_site) {
        this.douban_site = douban_site;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public ImagesBean getImages() {
        return images;
    }

    public void setImages(ImagesBean images) {
        this.images = images;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile_url() {
        return mobile_url;
    }

    public void setMobile_url(String mobile_url) {
        this.mobile_url = mobile_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getDo_count() {
        return do_count;
    }

    public void setDo_count(Object do_count) {
        this.do_count = do_count;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public Object getSeasons_count() {
        return seasons_count;
    }

    public void setSeasons_count(Object seasons_count) {
        this.seasons_count = seasons_count;
    }

    public String getSchedule_url() {
        return schedule_url;
    }

    public void setSchedule_url(String schedule_url) {
        this.schedule_url = schedule_url;
    }

    public Object getEpisodes_count() {
        return episodes_count;
    }

    public void setEpisodes_count(Object episodes_count) {
        this.episodes_count = episodes_count;
    }

    public int getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }

    public Object getCurrent_season() {
        return current_season;
    }

    public void setCurrent_season(Object current_season) {
        this.current_season = current_season;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getRatings_count() {
        return ratings_count;
    }

    public void setRatings_count(int ratings_count) {
        this.ratings_count = ratings_count;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<CastsBean> getCasts() {
        return casts;
    }

    public void setCasts(List<CastsBean> casts) {
        this.casts = casts;
    }

    public List<DirectorsBean> getDirectors() {
        return directors;
    }

    public void setDirectors(List<DirectorsBean> directors) {
        this.directors = directors;
    }

    public List<String> getAka() {
        return aka;
    }

    public void setAka(List<String> aka) {
        this.aka = aka;
    }

    public static class RatingBean implements Serializable {
        /**
         * max : 10
         * average : 8.9
         * stars : 45
         * min : 0
         */

        private int max;
        private double average;
        private String stars;
        private int min;

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public double getAverage() {
            return average;
        }

        public void setAverage(double average) {
            this.average = average;
        }

        public String getStars() {
            return stars;
        }

        public void setStars(String stars) {
            this.stars = stars;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }
    }

    public static class ImagesBean implements Serializable {
        /**
         * small : https://img1.doubanio.com/view/movie_poster_cover/ipst/public/p1312700628.webp
         * large : https://img1.doubanio.com/view/movie_poster_cover/lpst/public/p1312700628.webp
         * medium : https://img1.doubanio.com/view/movie_poster_cover/spst/public/p1312700628.webp
         */

        private String small;
        private String large;
        private String medium;

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }
    }

    public static class CastsBean implements Serializable {
        /**
         * alt : https://movie.douban.com/celebrity/1027138/
         * avatars : {"small":"https://img3.doubanio.com/img/celebrity/small/31885.jpg","large":"https://img3.doubanio.com/img/celebrity/large/31885.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/31885.jpg"}
         * name : 威尔·史密斯
         * id : 1027138
         */

        private String alt;
        private AvatarsBean avatars;
        private String name;
        private String id;

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public AvatarsBean getAvatars() {
            return avatars;
        }

        public void setAvatars(AvatarsBean avatars) {
            this.avatars = avatars;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public static class AvatarsBean {
            /**
             * small : https://img3.doubanio.com/img/celebrity/small/31885.jpg
             * large : https://img3.doubanio.com/img/celebrity/large/31885.jpg
             * medium : https://img3.doubanio.com/img/celebrity/medium/31885.jpg
             */

            private String small;
            private String large;
            private String medium;

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            public String getMedium() {
                return medium;
            }

            public void setMedium(String medium) {
                this.medium = medium;
            }
        }
    }

    public static class DirectorsBean implements Serializable {
        /**
         * alt : https://movie.douban.com/celebrity/1045093/
         * avatars : {"small":"https://img1.doubanio.com/img/celebrity/small/20409.jpg","large":"https://img1.doubanio.com/img/celebrity/large/20409.jpg","medium":"https://img1.doubanio.com/img/celebrity/medium/20409.jpg"}
         * name : 加布里埃莱·穆奇诺
         * id : 1045093
         */

        private String alt;
        private AvatarsBeanX avatars;
        private String name;
        private String id;

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public AvatarsBeanX getAvatars() {
            return avatars;
        }

        public void setAvatars(AvatarsBeanX avatars) {
            this.avatars = avatars;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public static class AvatarsBeanX {
            /**
             * small : https://img1.doubanio.com/img/celebrity/small/20409.jpg
             * large : https://img1.doubanio.com/img/celebrity/large/20409.jpg
             * medium : https://img1.doubanio.com/img/celebrity/medium/20409.jpg
             */

            private String small;
            private String large;
            private String medium;

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            public String getMedium() {
                return medium;
            }

            public void setMedium(String medium) {
                this.medium = medium;
            }
        }
    }
}
