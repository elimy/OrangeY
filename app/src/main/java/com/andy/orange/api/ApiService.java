package com.andy.orange.api;

import com.andy.orange.bean.Book;
import com.andy.orange.bean.Film;
import com.andy.orange.bean.GenresMovie;
import com.andy.orange.bean.RankingListDetail;
import com.andy.orange.bean.RankingListItem;
import com.andy.orange.bean.SongDetail;
import com.andy.orange.bean.SongListDetail;
import com.andy.orange.bean.SongListInfo;
import com.andy.orange.bean.SubjectMovie;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Andy Lau on 2017/8/10.
 */

public interface ApiService {

    public static final String MOVIE_BASE_URL_TYPE = "http://121.42.174.147:8080/RecommendMovie/";
    public static final String MOVIE_BASE_URL_HOT = "https://api.douban.com/v2/";
    public static final String BOOK_BASE_URL_HOT = "https://api.douban.com/v2/";
    public static final String MUSIC_BASE_URL = "http://tingapi.ting.baidu.com/v1/restserver/";
    public static final String PHOTO_BASE_URL ="http://121.42.174.147:8080/";

    //获取具体种类的电影
/*    @GET("getMoviesByPage.action")
    Observable<Map<String, List<MovieInfo>>> getMoiveByType(
            @Query("pageSize") int pageSize, @Query("pageNow") int pageNow,
            @Query("type") String type, @Query("genres") String genres);*/

    //获取热门电影
    /*@GET("getHotMovies.action")*/
    @GET("movie/top250")
    Observable<Film> getHotFilm(@Query("start") int start, @Query("count") int count);

    //获取指定图书的内容
    @GET("book/search")
    Observable<Book> getSearchBook(@Query("q") String name,
                                   @Query("tag") String tag,
                                   @Query("start") int start,
                                   @Query("count") int count);

    //根据类型获取电影
    @GET("movie/search")
    Observable<GenresMovie> getMoviesByGenres(
                                    @Query("tag") String tag,
                                    @Query("start") int start,
                                    @Query("count") int count);
    //获取指定电影的细节
    @GET("movie/subject/{id}")
    Observable<SubjectMovie> getMovieDetail(@Path("id") String id);


    //http://tingapi.ting.baidu.com/v1/restserver/ting?
    // from=qianqian&version=2.1.0&method=baidu.ting.search.catalogSug&format=json&query=%E5%B0%8F%E8%8B%B9%E6%9E%9C

    //获取全部歌单
    @GET("ting")
    @Headers("user-agent:Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
    Observable<SongListInfo> getSongListAll(@Query("format") String format,
                                                              @Query("from") String from,
                                                              @Query("method") String method,
                                                              @Query("page_size") int page_size,
                                                              @Query("page_no") int page_no);


    //http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&from=webapp_music&method=baidu.ting.billboard.billCategory&kflag=1
    //获取全部榜单
    @GET("ting")
    @Headers("user-agent:Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
    Observable<RankingListItem> getRankingListAll(@Query("format") String format,
                                                  @Query("from") String from,
                                                  @Query("method") String method,
                                                  @Query("kflag") int kflag);


    //http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&from=webapp_music&method=baidu.ting.diy.gedanInfo&listid=504873556
    //获取某个歌单中的信息
    @GET("ting")
    @Headers("user-agent:Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
    Observable<SongListDetail> getSongListDetail(@Query("format") String format,
                                                 @Query("from") String from,
                                                 @Query("method") String method,
                                                 @Query("listid") String listid);


    //http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&from=android&method=baidu.ting.song.play&version=5.6.5.6&songid=465574
    //获取某个歌曲的信息
    @GET("ting")
    @Headers("user-agent:Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
    Observable<SongDetail> getSongDetail(@Query("from") String from,
                                         @Query("version") String version,
                                         @Query("format") String format,
                                         @Query("method") String method,
                                         @Query("songid") String songid);


    //http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&from=webapp_music&method=baidu.ting.billboard.billList&
    // type=2&offset=0&size=100&fields=song_id,title,author,album_title,pic_big,pic_small,havehigh,all_rate,charge,has_mv_mobile,learn,song_source,korean_bb_song
    //获取某个榜单中歌曲信息
    @GET("ting")
    @Headers("user-agent:Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
    Observable<RankingListDetail> getRankingListDetail(@Query("format") String format,
                                                       @Query("from") String from,
                                                       @Query("method") String method,
                                                       @Query("type") int type,
                                                       @Query("offset") int offset,
                                                       @Query("size") int size,
                                                       @Query("fields") String fields);

/*
    //注册请求 暂时为GET方法 后期转换为POST方法 还有加密
    @GET("register.action")
    Observable<SignupBean> getSignUpResult(
            @Query("username") String username, @Query("password") String password);
    //登录请求 暂时为GET方法 后期转换为POST方法 还有加密
    @GET("loginValidation.action")
    Observable<LoginBean> getLogInResult(
            @Query("username") String username, @Query("password") String password);
    //提交评分
    @GET("saveRating.action")
    Observable<RatingResultBean> getRatingResult(
            @Query("userid") String userid, @Query("movie_id") int movie_id,@Query("rating") int rating);
    //上传用户信息到后台
    @GET("addUserInfo.action")

    Observable<LoginBean> uploadUserInformation(@Query("username") String username,
                                                @Query("nickname") String nickname,
                                                @Query("age") String age,
                                                @Query("sex") String sex,
                                                @Query("movie_preference") String movie_preference,
                                                @Query("music_preference") String music_preference);
    //上传图片信息
    @POST("usersPhoto/")
    @Multipart
    Observable<Response<Object>> uploadImageFile(@Part MultipartBody.Part MultipartFile);
    //推荐歌曲
    @GET("ting")
    @Headers("user-agent:Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
    Observable<RecomMusicBean> getRecomMusicList(@Query("from") String from,
                                                 @Query("version") String version,
                                                 @Query("format") String format,
                                                 @Query("method") String method,
                                                 @Query("num") int num);
                                                 */
}
